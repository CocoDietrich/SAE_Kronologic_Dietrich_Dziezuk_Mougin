package Kronologic.IA.IADeduction;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Realite;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.Jeu.Indice.Indice;
import Kronologic.Jeu.Indice.IndicePersonnage;
import Kronologic.Jeu.Indice.IndiceTemps;
import Kronologic.MVC.Modele.ModeleJeu;

import java.util.*;
import java.util.stream.Collectors;

public class ModeleHeuristiqueSolver {

    private final String[] personnages;
    private final boolean[][][] domainesPersonnages = new boolean[6][6][6]; // Temps × Personnages × Lieux
    private final int[][] sallesAdjacentes;

    public ModeleHeuristiqueSolver(String[] personnages, int[][] sallesAdjacentes, List<Realite> positionsInitiales) {
        this.personnages = personnages;
        this.sallesAdjacentes = sallesAdjacentes;

        initialiserDomaines();
        appliquerPositionsInitiales(positionsInitiales);
    }

    // Initialiser les domaines
    public void initialiserDomaines() {
        for (int t = 0; t < 6; t++) {
            for (int p = 0; p < 6; p++) {
                for (int l = 0; l < 6; l++) {
                    domainesPersonnages[t][p][l] = true; // Tous les lieux sont initialement possibles
                }
            }
        }
    }

    // Appliquer les positions initiales
    public void appliquerPositionsInitiales(List<Realite> positionsInitiales) {
        for (Realite position : positionsInitiales) {
            ajouterContraintePersonnage(position.getPersonnage(), position.getLieu(), position.getTemps().getValeur() - 1);
        }
        appliquerContraintesDeplacements();
    }

    private void appliquerContraintesDeplacements() {
        for (int t = 0; t < 5; t++) { // Temps de 0 à 4
            for (int p = 0; p < personnages.length; p++) {
                boolean[] lieuxAccessibles = new boolean[6];

                // Réunion des lieux accessibles depuis tous les lieux possibles au temps `t`
                for (int l = 0; l < 6; l++) {
                    if (domainesPersonnages[t][p][l]) { // Si le lieu `l` est possible au temps `t`
                        for (int adj : sallesAdjacentes[l]) {
                            lieuxAccessibles[adj - 1] = true; // Marquer comme accessible au temps suivant
                        }
                    }
                }

                // Appliquer les lieux accessibles au temps `t+1`
                for (int l = 0; l < 6; l++) {
                    if (!lieuxAccessibles[l]) {
                        domainesPersonnages[t + 1][p][l] = false; // Désactiver les lieux non accessibles
                    }
                }
            }
        }
        Map<String, Integer> coupable = trouverCoupableBacktracking();

        if (coupable != null) {
            System.out.println("Coupable identifié : " + personnages[coupable.get("Coupable")]);
            System.out.println("Lieu du crime : " + (coupable.get("Lieu") + 1));
            System.out.println("Temps du crime : " + (coupable.get("Temps") + 1));
        } else {
            System.out.println("Aucune solution trouvée pour le moment.");
        }

    }


    // Ajouter une contrainte sur un personnage
    public void ajouterContraintePersonnage(Personnage personnage, Lieu lieu, int temps) {
        int personnageIndex = getIndexPersonnage(personnage.getNom().substring(0, 1));
        int lieuIndex = lieu.getId() - 1;

        // Réduire le domaine du personnage à un seul lieu
        for (int l = 0; l < 6; l++) {
            domainesPersonnages[temps][personnageIndex][l] = (l == lieuIndex);
        }

        appliquerContraintesDeplacements();
    }

    // Ajouter une contrainte sur le nombre de passages
    public void ajouterContrainteNombreDePassages(Personnage personnage, Lieu lieu, int nbPassages) {
        int personnageIndex = getIndexPersonnage(personnage.getNom().substring(0, 1));

        // On récupère l'intervalle de temps où le personnage est passé dans le lieu
        IndicePersonnage i = (IndicePersonnage) ModeleJeu.getPartie().getIndicesDecouverts().getLast();
        int temps = i.getInfoPrive();

        // On met à false à temps - 2 et temps pour les salles non adjacentes à la salle voulue
        afficherUniquementLieuxAdjacents(personnageIndex, lieu.getId() - 1, temps);

        // On raisonne sur le nombre de passages
        // ex : si nbPassages = 3 et que temps = 2, alors le personnage est aussi dans le lieu aux temps 4 et 6
        // ex : si nbPassages = 1 et que temps = 2, alors le personnage n'est pas dans le lieu pour le reste du temps
        if (nbPassages == 1) {
            // On regarde si on a déjà trouvé tous les passages du personnage
            int nbPassagesTrouves = 0;
            ArrayList<Integer> tempsPassagesTrouves = new ArrayList<>();
            for (int tempsActuel = 0; tempsActuel < 6; tempsActuel++) {
                if (domainesPersonnages[tempsActuel][personnageIndex][lieu.getId() - 1]) {
                    // Si il s'agit de la seule salle à true, on incrémente le nombre de passages trouvés
                    int compteurTrue = 0;
                    for (int l = 0; l < 6; l++) {
                        if (domainesPersonnages[tempsActuel][personnageIndex][l]) {
                            compteurTrue++;
                        }
                    }
                    if (compteurTrue == 1) {
                        nbPassagesTrouves++;
                        tempsPassagesTrouves.add(tempsActuel);
                    }
                }
            }

            // Si on a trouvé tous les passages, on en déduit que le personnage n'est pas dans la salle aux autres temps
            if (nbPassagesTrouves == nbPassages) {
                for (int tempsActuel = 0; tempsActuel < 6; tempsActuel++) {
                    if (!tempsPassagesTrouves.contains(tempsActuel)) {
                        domainesPersonnages[tempsActuel][personnageIndex][lieu.getId() - 1] = false;
                    }
                }
            }

            if (temps > 0) {
                for (int t = 0; t < 6; t++) {
                    if (t != temps-1) {
                        domainesPersonnages[t][personnageIndex][lieu.getId() - 1] = false;
                    }
                }
            }

        } else if (nbPassages == 3) {
            if (temps%2 == 0) {
                for (int t = 0; t < 6; t++) {
                    if (t%2 == 0) {
                        // Le personnage n'est pas dans le lieu voulu
                        domainesPersonnages[t][personnageIndex][lieu.getId() - 1] = false;
                    } else {
                        // On ne garde que le lieu voulu
                        for (int l = 0; l < 6; l++) {
                            domainesPersonnages[t][personnageIndex][l] = (l == lieu.getId() - 1);
                        }
                    }
                }
            } else {
                for (int t = 0; t < 6; t++) {
                    if (t%2 != 0) {
                        // Le personnage n'est pas dans le lieu voulu
                        domainesPersonnages[t][personnageIndex][lieu.getId() - 1] = false;
                    } else {
                        // On ne garde que le lieu voulu
                        for (int l = 0; l < 6; l++) {
                            domainesPersonnages[t][personnageIndex][l] = (l == lieu.getId() - 1);
                        }
                    }
                }
            }
        } else if (nbPassages == 0) {
            // Le personnage n'est passé aucune fois dans ce lieu
            for (int t = 0; t < 6; t++) {
                domainesPersonnages[t][personnageIndex][lieu.getId() - 1] = false;
            }
        } else if (nbPassages == 2) {
            int nbPassagesTrouves = 0;
            ArrayList<Integer> tempsPassagesTrouves = new ArrayList<>();
            // On cherche le nombre de passages dont on est déjà sûr
            for (int tempsActuel = 0; tempsActuel < 6; tempsActuel++) {
                if (domainesPersonnages[tempsActuel][personnageIndex][lieu.getId() - 1]) {
                    // Si il s'agit de la seule salle à true, on incrémente le nombre de passages trouvés
                    int compteurTrue = 0;
                    for (int l = 0; l < 6; l++) {
                        if (domainesPersonnages[tempsActuel][personnageIndex][l]) {
                            compteurTrue++;
                        }
                    }
                    if (compteurTrue == 1) {
                        nbPassagesTrouves++;
                        tempsPassagesTrouves.add(tempsActuel);
                    }
                }
            }

            // Si on a trouvé tous les passages, on en déduit que le personnage n'est pas dans la salle aux autres temps
            if (nbPassagesTrouves == nbPassages) {
                for (int tempsActuel = 0; tempsActuel < 6; tempsActuel++) {
                    if (!tempsPassagesTrouves.contains(tempsActuel)) {
                        domainesPersonnages[tempsActuel][personnageIndex][lieu.getId() - 1] = false;
                    }
                }
            }
        }

        appliquerContraintesDeplacements();
    }


    // Ajouter une contrainte sur le nombre de personnes dans une salle
    public void ajouterContrainteTemps(Lieu lieu, Temps temps, int nbPersonnages) {
        int lieuIndex = lieu.getId() - 1;

        // On récupère le personnage qui est passé dans le lieu au temps donné
        IndiceTemps i = (IndiceTemps) ModeleJeu.getPartie().getIndicesDecouverts().getLast();
        String nomPersonnage = i.getInfoPrive();

        // On récupère l'index du personnage
        int personnageIndex = getIndexPersonnage(nomPersonnage.substring(0, 1));

        // Si il n'y a personne dans la sale au temps donné, on met à false pour tous les personnages
        if (nbPersonnages == 0) {
            for (int p = 0; p < personnages.length; p++) {
                domainesPersonnages[temps.getValeur() - 1][p][lieuIndex] = false;
            }
        }

        // On supprime des lieux possibles aux temps précédents et suivants la salle où on se trouve
        if (personnageIndex != -1) {
            for (int t = 0; t < 6; t++) {
                if (temps.getValeur() == 1) {
                    if (t == temps.getValeur()) {
                        domainesPersonnages[t][personnageIndex][lieuIndex] = false;
                    }
                } else if (temps.getValeur() == 6) {
                    if (t == temps.getValeur() - 2) {
                        domainesPersonnages[t][personnageIndex][lieuIndex] = false;
                    }
                } else {
                    if (t == temps.getValeur() - 2 || t == temps.getValeur()) {
                        domainesPersonnages[t][personnageIndex][lieuIndex] = false;
                    }
                }
            }
        }

        if (personnageIndex != -1) {
            afficherUniquementLieuxAdjacents(personnageIndex, lieuIndex, temps.getValeur());
        }

        // On vérifie si on a déjà trouvé tous les personnages présents dans la salle au temps donné
        // Il s'agit des personnages pour lesquelles on a un seul booléen à true pour le temps donné
        int nbPersonnagesTrouves = 0;
        ArrayList<Integer> indexPersonnages = new ArrayList<>();
        for (int p = 0; p < personnages.length; p++) {
            boolean[] domaine = domainesPersonnages[temps.getValeur() - 1][p];
            int compteurTrue = 0;
            for (int l = 0; l < 6; l++) {
                if (domaine[l]) {
                    compteurTrue++;
                }
            }
            if (compteurTrue == 1) {
                nbPersonnagesTrouves++;
                indexPersonnages.add(p);
            }
        }

        // Si on a trouvé tous les personnages présents, on en déduit que les autres ne sont pas là
        if (nbPersonnagesTrouves == nbPersonnages) {
            for (int p = 0; p < personnages.length; p++) {
                if (!indexPersonnages.contains(p)) {
                    domainesPersonnages[temps.getValeur() - 1][p][lieuIndex] = false;
                }
            }
        }

        appliquerContraintesDeplacements();
    }

    public void afficherUniquementLieuxAdjacents(int personnage, int lieu, int temps) {
        // A temps - 2 et temps, on met à false les lieux non adjacents
        boolean[] tempsPrecedent = new boolean[6];
        boolean[] tempsSuivant = new boolean[6];
        if (temps == 1) {
            tempsSuivant = domainesPersonnages[temps][personnage];
        } else if (temps == 6){
            tempsPrecedent = domainesPersonnages[temps - 2][personnage];
        } else if (1 <= temps && temps <= 5) {
            tempsPrecedent = domainesPersonnages[temps - 2][personnage];
            tempsSuivant = domainesPersonnages[temps][personnage];
        }

        int[] sallesAdj = sallesAdjacentes[lieu];
        for (int l = 0; l < 6; l++) {
            int finalL = l;
            if (Arrays.stream(sallesAdj).noneMatch(x -> x == finalL +1)) {
                tempsPrecedent[l] = false;
                tempsSuivant[l] = false;
            }
        }
    }

    public Map<String, Integer> trouverCoupableBacktracking() {
        // Initialiser les listes avec toutes les valeurs possibles
        Set<Integer> coupablesPotentiels = new HashSet<>();
        Set<Integer> lieuxPotentiels = new HashSet<>();
        Set<Integer> tempsPotentiels = new HashSet<>();

        // Ajouter tous les personnages sauf le détective
        for (int i = 0; i < personnages.length; i++) {
            if (!personnages[i].equals("D")) {
                coupablesPotentiels.add(i);
            }
        }

        // Ajouter tous les lieux (1 à 6)
        for (int i = 0; i < 6; i++) {
            lieuxPotentiels.add(i);
        }

        // Ajouter tous les temps sauf le temps 1
        for (int i = 1; i < 6; i++) {
            tempsPotentiels.add(i);
        }

        // Effectuer le backtracking
        return backtrackTrouverCoupable(coupablesPotentiels, lieuxPotentiels, tempsPotentiels);
    }

    private Map<String, Integer> backtrackTrouverCoupable(Set<Integer> coupables, Set<Integer> lieux, Set<Integer> temps) {
        for (int c : coupables) {
            for (int l : lieux) {
                for (int t : temps) {
                    if (estConfigurationValide(c, l, t)) {
                        // Si on trouve une seule possibilité valide, on la retourne
                        Map<String, Integer> resultat = new HashMap<>();
                        resultat.put("Coupable", c);
                        resultat.put("Lieu", l);
                        resultat.put("Temps", t);
                        return resultat;
                    }
                }
            }
        }
        // Si aucune configuration valide n'est trouvée
        return null;
    }

    private boolean estConfigurationValide(int c, int l, int t) {
        // Vérifier si le coupable pouvait être présent au bon moment
        if (!domainesPersonnages[t][c][l]) {
            return false;
        }

        // Vérifier que le détective était bien dans la même pièce au temps donné
        int indexDetective = getIndexPersonnage("D");
        if (!domainesPersonnages[t][indexDetective][l]) {
            return false;
        }

        // Vérifier qu'il n'y avait que 2 personnes dans cette salle à ce moment-là
        int nombrePersonnes = 0;
        for (int p = 0; p < personnages.length; p++) {
            if (domainesPersonnages[t][p][l]) {
                nombrePersonnes++;
            }
        }
        return nombrePersonnes == 2; // Exactement 2 personnes dans la salle
    }

    // Affichage des domaines
    public String affichagePropagate() {
        StringBuilder historique = new StringBuilder();

        historique.append("===== Historique des Déductions =====\n");
        for (int p = 0; p < personnages.length; p++) {
            historique.append(personnages[p]).append(" :\n");
            for (int t = 0; t < 6; t++) {
                StringBuilder domaine = new StringBuilder("{");
                boolean hasValidDomain = false;

                for (int l = 0; l < 6; l++) {
                    if (domainesPersonnages[t][p][l]) {
                        domaine.append(l + 1).append(", ");
                        hasValidDomain = true;
                    }
                }

                if (!hasValidDomain) {
                    domaine.append("Incohérence !");
                } else if (domaine.length() > 1) {
                    domaine.setLength(domaine.length() - 2); // Supprime la dernière virgule
                }

                domaine.append("}");
                historique.append(String.format("     - Temps %d : %s%n", t + 1, domaine));
            }
            historique.append("\n");
        }

        return historique.toString();
    }


    public int getIndexPersonnage(String personnage) {
        for (int i = 0; i < personnages.length; i++) {
            if (personnages[i].equals(personnage)) {
                return i;
            }
        }
        return -1;
    }

    public String[] getPersonnages() {
        return personnages;
    }

    public boolean[][][] getDomainesPersonnages() {
        return domainesPersonnages;
    }
}