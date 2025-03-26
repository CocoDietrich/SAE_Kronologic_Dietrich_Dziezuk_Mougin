package Kronologic.IA.IADeduction;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Realite;
import Kronologic.Jeu.Elements.Temps;

import java.util.*;

public class ModeleHeuristiqueSolver {

    private final String[] personnages;
    private final boolean[][][] domainesPersonnages = new boolean[6][6][6]; // Temps × Personnages × Lieux
    private final int[][] sallesAdjacentes;
    private final int[][] nombrePersonnageContrainte = new int[6][6]; // Temps × Lieux × NbPersonnages
    private boolean coupableTrouve = false;
    private int coupablePersonnage = -1;
    private int coupableLieu = -1;
    private int coupableTemps = -1;

    public ModeleHeuristiqueSolver(String[] personnages, int[][] sallesAdjacentes, List<Realite> positionsInitiales) {
        this.personnages = personnages;
        this.sallesAdjacentes = sallesAdjacentes;

        initialiserNombrePassage();
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

    // Initialiser les contraintes de nombre de passages
    public void initialiserNombrePassage() {
        for (int t = 0; t < 6; t++) {
            for (int l = 0; l < 6; l++) {
                nombrePersonnageContrainte[t][l] = -1; // Nombre de passages non défini
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
        for (int iteration = 0; iteration < 2; iteration++) { // 2 passes : avant + arrière
            // ⏩ Propagation avant (t ➜ t+1)
            for (int t = 0; t < 5; t++) {
                for (int p = 0; p < personnages.length; p++) {
                    boolean[] lieuxAccessibles = new boolean[6];

                    for (int l = 0; l < 6; l++) {
                        if (domainesPersonnages[t][p][l]) {
                            for (int adj : sallesAdjacentes[l]) {
                                lieuxAccessibles[adj - 1] = true;
                            }
                        }
                    }

                    for (int l = 0; l < 6; l++) {
                        if (!lieuxAccessibles[l]) {
                            domainesPersonnages[t + 1][p][l] = false;
                        }
                    }
                }
            }

            // ⏪ Propagation arrière (t+1 ➜ t)
            for (int t = 5; t > 0; t--) {
                for (int p = 0; p < personnages.length; p++) {
                    boolean[] lieuxAccessiblesInverse = new boolean[6];

                    for (int l = 0; l < 6; l++) {
                        if (domainesPersonnages[t][p][l]) {
                            for (int adj : sallesAdjacentes[l]) {
                                lieuxAccessiblesInverse[adj - 1] = true;
                            }
                        }
                    }

                    for (int l = 0; l < 6; l++) {
                        if (!lieuxAccessiblesInverse[l]) {
                            domainesPersonnages[t - 1][p][l] = false;
                        }
                    }
                }
            }
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

        afficherUniquementLieuxAdjacents(personnageIndex, lieuIndex, temps + 1);

        appliquerContraintesDeplacements();
        appliquerContrainteNombrePersonnage();
        essayerDeTrouverCoupable();
    }

    // Ajouter une contrainte sur le nombre de passages
    public void ajouterContrainteNombreDePassages(Personnage personnage, Lieu lieu, int nbPassages) {

        int personnageIndex = getIndexPersonnage(personnage.getNom().substring(0, 1));
        int lieuIndex = lieu.getId() - 1;

        ArrayList<Integer> tempsSurs = trouverNombrePassagesSurs(personnageIndex, lieuIndex);

        if (nbPassages == 0) {
            for (int t = 0; t < 6; t++) {
                domainesPersonnages[t][personnageIndex][lieuIndex] = false;
            }
        } else {
            if (tempsSurs.size() == nbPassages) {
                for (int t = 0; t < 6; t++) {
                    if (!tempsSurs.contains(t)) {
                        domainesPersonnages[t][personnageIndex][lieuIndex] = false;
                    }
                }
            }

            if (nbPassages == 3) {
                int tempsSur = tempsSurs.getFirst() + 1;
                // Si un personnage est passé 3 fois dans une salle, on peut en déduire qu'il y est passé à 2 pas
                // de temps d'intervalle à chaque fois
                if (tempsSur % 2 == 0) {
                    for (int t = 0; t < 6; t++) {
                        if (t % 2 != 0) {
                            for (int l = 0; l < 6; l++) {
                                domainesPersonnages[t][personnageIndex][l] = l == lieuIndex;
                            }
                        }
                    }
                } else {
                    for (int t = 0; t < 6; t++) {
                        if (t % 2 == 0) {
                            for (int l = 0; l < 6; l++) {
                                domainesPersonnages[t][personnageIndex][l] = l == lieuIndex;
                            }
                        }
                    }
                }
            }
        }

        appliquerContraintesDeplacements();
        appliquerContrainteNombrePersonnage();
        essayerDeTrouverCoupable();
    }

    // Ajouter une contrainte sur le nombre de personnes dans une salle
    public void ajouterContrainteTemps(Lieu lieu, Temps temps, int nbPersonnages) {
        this.nombrePersonnageContrainte[temps.getValeur() - 1][lieu.getId() - 1] = nbPersonnages;

        int lieuIndex = lieu.getId() - 1;
        int tempsIndex = temps.getValeur() - 1;

        ArrayList<Integer> personnagesSurs = trouverPersonnagesSurs(lieuIndex, tempsIndex);

        if (nbPersonnages == 0) {
            for (int p = 0; p < 6; p++) {
                domainesPersonnages[tempsIndex][p][lieuIndex] = false;
            }
        } else {
            if (personnagesSurs.size() == nbPersonnages) {
                for (int p = 0; p < 6; p++) {
                    if (!personnagesSurs.contains(p)) {
                        domainesPersonnages[tempsIndex][p][lieuIndex] = false;
                    }
                }
            }
        }

        appliquerContraintesDeplacements();
        appliquerContrainteNombrePersonnage();

        essayerDeTrouverCoupable();
    }

    // Appliquer les contraintes de nombre de personnes dans une salle
    public void appliquerContrainteNombrePersonnage() {
        for (int t = 0; t < 6; t++) {
            for (int l = 0; l < 6; l++) {
                if (nombrePersonnageContrainte[t][l] != -1) {
                    for (int p = 0; p < 6; p++) {
                        ArrayList<Integer> personnagesSurs = trouverPersonnagesSurs(l, t);
                        if (personnagesSurs.size() == nombrePersonnageContrainte[t][l]) {
                            if (!personnagesSurs.contains(p)) {
                                domainesPersonnages[t][p][l] = false;
                            }
                        }
                    }
                }
            }
        }
    }

    // Méthode qui renvoie les temps où un personnage est sûr de se trouver dans un lieu
    public ArrayList<Integer> trouverNombrePassagesSurs(int personnage, int lieu) {
        ArrayList<Integer> tempsSurs = new ArrayList<>();
        for (int t = 0; t < 6; t++) {
            if (domainesPersonnages[t][personnage][lieu]) {
                // S'il s'agit de la seule salle à true, on est sûr
                boolean seul = true;
                for (int l = 0; l < 6; l++) {
                    if (l != lieu && domainesPersonnages[t][personnage][l]) {
                        seul = false;
                        break;
                    }
                }
                if (seul) {
                    tempsSurs.add(t);
                }
            }
        }
        return tempsSurs;
    }

    // Méthode qui renvoie les personnages dont on est sûrs de la présence dans une salle à un temps donné
    public ArrayList<Integer> trouverPersonnagesSurs(int lieu, int temps) {
        ArrayList<Integer> personnagesSurs = new ArrayList<>();
        for (int p = 0; p < 6; p++) {
            if (domainesPersonnages[temps][p][lieu]) {
                // S'il s'agit de la seule salle à true, on est sûr
                boolean seul = true;
                for (int l = 0; l < 6; l++) {
                    if (l != lieu && domainesPersonnages[temps][p][l]) {
                        seul = false;
                        break;
                    }
                }
                if (seul) {
                    personnagesSurs.add(p);
                }
            }
        }
        return personnagesSurs;
    }

    public void afficherUniquementLieuxAdjacents(int personnage, int lieu, int temps) {
        // A temps - 2 et temps, on met à false les lieux non adjacents
        boolean[] tempsPrecedent = new boolean[6];
        boolean[] tempsSuivant = new boolean[6];
        if (temps == 1) {
            tempsSuivant = domainesPersonnages[temps][personnage];
        } else if (temps == 6) {
            tempsPrecedent = domainesPersonnages[temps - 2][personnage];
        } else if (1 <= temps && temps <= 5) {
            tempsPrecedent = domainesPersonnages[temps - 2][personnage];
            tempsSuivant = domainesPersonnages[temps][personnage];
        }

        int[] sallesAdj = sallesAdjacentes[lieu];
        for (int l = 0; l < 6; l++) {
            int finalL = l;
            if (Arrays.stream(sallesAdj).noneMatch(x -> x == finalL + 1)) {
                tempsPrecedent[l] = false;
                tempsSuivant[l] = false;
            }
        }
    }

    public void essayerDeTrouverCoupable() {
        // On regarde qui peut être le coupable
        for (int t = 1; t < 6; t++) {
            for (int l = 0; l < 6; l++) {
                for (int p = 0; p < 6; p++) {
                    if (personnages[p].equals("D")) {
                        continue;
                    }
                    peutEtreCoupable(p, l, t);
                }
            }
        }
    }

    // Affichage des domaines
    public String affichagePropagate() {
        StringBuilder historique = new StringBuilder();

        if (coupableTrouve) {
            historique.append("===== 🎯 Coupable Identifié ! =====\n");

            String nomCoupable = personnages[coupablePersonnage];
            int lieuCrime = coupableLieu;
            int tempsCrime = coupableTemps;

            historique.append(String.format("👤 Coupable : %s\n", nomCoupable));
            historique.append(String.format("📍 Lieu du crime : %d\n", lieuCrime));
            historique.append(String.format("⏳ Temps du crime : %d\n\n", tempsCrime));

        } else {
            historique.append("===== 🔍 Impossible d'identifier un coupable pour le moment =====\n");
            historique.append("⚠ Continuez à poser des questions pour réduire les suspects !\n\n");
        }

        historique.append("===== 📜 Historique des Déductions =====\n\n");
        for (int p = 0; p < personnages.length; p++) {
            historique.append("🕵️‍ ").append(personnages[p]).append(" :\n");
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


    private void peutEtreCoupable(int p, int l, int t) {
        // Vérifier que le détective et le suspect sont bien présents
        if (!domainesPersonnages[t][p][l] || !domainesPersonnages[t][getIndexPersonnage("D")][l]) {
            return;
        }

        // On vérifie s'ils sont sûrs d'être là
        for (int i = 0; i < 6; i++) {
            // Cas du personnage testé présent dans un autre lieu
            if (domainesPersonnages[t][p][i] && i != l) {
                return;
            }
            // Cas du détective présent dans un autre lieu
            if (domainesPersonnages[t][getIndexPersonnage("D")][i] && i != l) {
                return;
            }
        }

        // On est sûr que le détective et le personnage sont présents
        // On regarde si d'autres personnages sont là
        boolean autrePersonnageAbsent = false;
        for (int i = 0; i < 6; i++) {
            // On regarde si un personnage est là
            if (domainesPersonnages[t][i][l] && i != p && i != getIndexPersonnage("D")) {
                // On définit que de base, le personnage n'est présent que dans le lieu
                autrePersonnageAbsent = false;
                for (int j = 0; j < 6; j++) {
                    if (domainesPersonnages[t][i][j] && j != l) {
                        // Le personnage est là dans un autre lieu
                        autrePersonnageAbsent = true;
                        break;
                    }
                }
                // Si le personnage est présent que dans un lieu, on renvoie faux
                if (!autrePersonnageAbsent) {
                    return;
                }
            }
        }

        if (!autrePersonnageAbsent) {
            afficherCoupable(p, l, t);
        }
    }

    public void afficherCoupable(int p, int l, int t) {
        String coupable = String.format("👤 Coupable : %s\n", personnages[p]) +
                String.format("📍 Lieu du crime : %d\n", l + 1) +
                String.format("⏳ Temps du crime : %d\n\n", t + 1);
        System.out.println(coupable);
        coupableTrouve = true;
        coupablePersonnage = p;
        coupableLieu = l + 1;
        coupableTemps = t + 1;
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