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
    private final boolean[][][] solutionsMeurtre = new boolean[6][6][6]; // [Personnages sauf détective] x [Lieux] x [Temps]

    public ModeleHeuristiqueSolver(String[] personnages, int[][] sallesAdjacentes, List<Realite> positionsInitiales) {
        this.personnages = personnages;
        this.sallesAdjacentes = sallesAdjacentes;

        System.out.println("INIT");
        initialiserDomaines();
        initialiserSolutionsMeurtre();
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

    public void initialiserSolutionsMeurtre() {
        int indexDetective = getIndexPersonnage("D");

        for (int p = 0; p < 6; p++) {
            if (p == indexDetective) continue; // Exclure le détective

            for (int l = 0; l < 6; l++) {
                for (int t = 0; t < 6; t++) { // Temps 1 exclu
                    solutionsMeurtre[p][l][t] = t != 0; // Temps 0 exclu
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
        mettreAJourSolutionsMeurtre();
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
                int tempsSur = tempsSurs.get(0) + 1;
                // Si un personnage est passé 3 fois dans une salle, on peut en déduire qu'il y est passé à 2 pas
                // de temps d'intervalle à chaque fois
                if (tempsSur%2 == 0) {
                    for (int t = 0; t < 6; t++) {
                        if (t%2 != 0) {
                            for (int l = 0; l < 6; l++) {
                                domainesPersonnages[t][personnageIndex][l] = l == lieuIndex;
                            }
                        }
                    }
                } else {
                    for (int t = 0; t < 6; t++) {
                        if (t%2 == 0) {
                            for (int l = 0; l < 6; l++) {
                                domainesPersonnages[t][personnageIndex][l] = l == lieuIndex;
                            }
                        }
                    }
                }
            }
        }

        appliquerContraintesDeplacements();
        mettreAJourSolutionsMeurtre();
    }

    // Ajouter une contrainte sur le nombre de personnes dans une salle
    public void ajouterContrainteTemps(Lieu lieu, Temps temps, int nbPersonnages) {

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
        mettreAJourSolutionsMeurtre();
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

    public void mettreAJourSolutionsMeurtre() {
        boolean auMoinsUneSolution = false;

        for (int p = 0; p < 6; p++) {
            for (int l = 0; l < 6; l++) {
                for (int t = 1; t < 6; t++) { // On exclut le temps 1
                    System.out.println("🔍 Vérification pour " + personnages[p] + " au temps " + (t + 1) + " dans le lieu " + (l + 1));
                    if (!peutEtreCoupable(p, l, t)) {
                        System.out.println("❌ " + personnages[p] + " ne peut pas être le coupable !");
                        solutionsMeurtre[p][l][t] = false;
                    } else {
                        System.out.println("✅ " + personnages[p] + " peut être le coupable !");
                        auMoinsUneSolution = true;
                    }
                }
            }
        }

        // 🔹 Vérification pour éviter d'éliminer toutes les solutions d’un coup
        if (!auMoinsUneSolution) {
            System.out.println("⚠️ Attention : Toutes les solutions ont été éliminées ! Vérification nécessaire.");
        } else {
            afficherSolutionsMeurtre();
        }
    }


    private boolean peutEtreCoupable(int p, int l, int t) {
        if (personnages[p].equals("C") && l == 3 && t == 6) {
            System.out.println("ZONE OMBRE");
            System.out.println("VERIF SI C ET D SONT LA");
            System.out.println("C " + domainesPersonnages[t][p][l]);
            System.out.println("D " + domainesPersonnages[t][getIndexPersonnage("D")][l]);
        }

        // Vérifier que le détective et le suspect sont bien présents
        if (!domainesPersonnages[t][p][l] || !domainesPersonnages[t][getIndexPersonnage("D")][l]) {
            return false;
        }

        if (personnages[p].equals("C") && l == 3 && t == 6) {
            System.out.println("VERIF LIEUX SURS");
        }

        // On vérifie s'ils sont sûrs d'être là
        for (int i = 0; i < 6; i++) {
            if (personnages[p].equals("D") && l == 3 && t == 6) {
                System.out.println("DE C");
                System.out.println("Lieu " + i + " : " + domainesPersonnages[t][p][i]);
                System.out.println("DE D");
                System.out.println("Lieu " + i + " : " + domainesPersonnages[t][p][i]);
            }
            // Cas du personnage testé présent dans un autre lieu
            if (domainesPersonnages[t][p][i] && i != l) {
                return true;
            }
            // Cas du détective présent dans un autre lieu
            if (domainesPersonnages[t][getIndexPersonnage("D")][i] && i != l) {
                return true;
            }
        }

        if (personnages[p].equals("C") && l == 3 && t == 6) {
            System.out.println("VERIF AUTRES PERSONNAGES");
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
                    }
                }
                // Si le personnage est présent que dans un lieu, on renvoie faux
                if (!autrePersonnageAbsent) {
                    return false;
                }
            }
        }

        if (!autrePersonnageAbsent) {
            // Méthode c'est lui
        }
        return true;
    }

    public void afficherSolutionsMeurtre() {
        List<Map<String, Integer>> solutionsRestantes = new ArrayList<>();

        for (int p = 0; p < 6; p++) {
            for (int t = 0; t < 6; t++) {
                for (int l = 1; l < 6; l++) {
                    if (solutionsMeurtre[p][l][t]) {
                        Map<String, Integer> solution = new HashMap<>();
                        solution.put("Coupable", p);
                        solution.put("Lieu", l);
                        solution.put("Temps", t);
                        solutionsRestantes.add(solution);
                    }
                }
            }
        }

        // On affiche toutes les positions
        for (int p = 0; p < 6; p++) {
            for (int t = 0; t < 6; t++) {
                for (int l = 0; l < 6; l++) {
                    if (personnages[p].equals("C") || personnages[p].equals("D")) {
                        System.out.println("DOMAINE : " + personnages[p] + " - Temps " + (t + 1) + " - Lieu " + (l + 1) + " : " + domainesPersonnages[t][p][l]);
                        System.out.println("SOLUTIONS : " + personnages[p] + " - Temps " + (t + 1) + " - Lieu " + (l + 1) + " : " + solutionsMeurtre[t][p][l]);
                    }
                }
            }
        }

        if (solutionsRestantes.isEmpty()) {
            System.out.println("❌ Aucune solution valide restante !");
        } else if (solutionsRestantes.size() == 1) {
            Map<String, Integer> solution = solutionsRestantes.getFirst();
            System.out.println("✅ Coupable trouvé !");
            System.out.println("Coupable : " + personnages[solution.get("Coupable")]);
            System.out.println("Lieu du crime : " + (solution.get("Lieu") + 1));
            System.out.println("Temps du crime : " + (solution.get("Temps") + 1));
        } else {
            System.out.println("🔍 Solutions encore possibles (" + solutionsRestantes.size() + ") :");
            for (Map<String, Integer> sol : solutionsRestantes) {
                System.out.println("- Coupable : " + personnages[sol.get("Coupable")] +
                        ", Lieu : " + (sol.get("Lieu") + 1) +
                        ", Temps : " + (sol.get("Temps") + 1));
                // On regarde si le coupable est bien dans un lieu où est le détective (OK)
                // On regarde si le détective est obligatoirement dans cette salle
                int indexDetective = getIndexPersonnage("D");
                int nombreFalse = 0;
                for (int l = 0; l < 6; l++) {
                    if (!domainesPersonnages[sol.get("Temps")][indexDetective][l]){
                        nombreFalse++;
                    }
                }
                if (nombreFalse != 5) {
                    System.out.print("// PAS SUR");
                    System.out.println();
                }
                else {
                    int nombrePersonnes = 0;
                    for (int i = 0; i < personnages.length; i++) {
                        if (domainesPersonnages[sol.get("Temps")][i][sol.get("Lieu")]) {
                            nombrePersonnes++;
                        }
                    }
                    if (nombrePersonnes != 2) {
                        System.out.println("// TOUJOURS FAUX");
                    }
                    else {
                        System.out.println("OH LALA LE COUPABLE LA C'EST LUI REGARDE ICI LAAAAAAAAAAAAAAAAAAA");
                    }
                }
            }
        }
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