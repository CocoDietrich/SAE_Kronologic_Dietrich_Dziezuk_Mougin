package Kronologic.IA.IADeduction;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Realite;
import Kronologic.Jeu.Elements.Temps;

import java.util.*;

public class ModeleHeuristiqueSolver {

    private final String[] personnages;
    private final boolean[][][] domainesPersonnages = new boolean[6][6][6]; // Temps × Personnages × Lieux

    public ModeleHeuristiqueSolver(String[] personnages, int[][] sallesAdjacentes, List<Realite> positionsInitiales) {
        this.personnages = personnages;

        initialiserDomaines();
        appliquerPositionsInitiales(positionsInitiales);
        //appliquerContraintesDeplacements(sallesAdjacentes);
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
    }

    private void appliquerContraintesDeplacements(int[][] sallesAdjacentes) {
        for (int t = 0; t < 5; t++) { // Temps de 0 à 4
            for (int p = 0; p < personnages.length; p++) {
                for (int l = 0; l < 6; l++) {
                    if (domainesPersonnages[t][p][l]) { // Si le lieu est possible au temps `t`
                        // Vérifier les salles adjacentes
                        boolean[] adjacents = new boolean[6];
                        for (int adj : sallesAdjacentes[l]) {
                            adjacents[adj - 1] = true; // `adj - 1` pour convertir en index
                        }

                        // Désactiver les lieux non adjacents au temps suivant
                        for (int nextL = 0; nextL < 6; nextL++) {
                            if (!adjacents[nextL]) {
                                domainesPersonnages[t + 1][p][nextL] = false;
                            }
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

        // Supprimer ce personnage des autres lieux pour ce temps
        for (int otherP = 0; otherP < personnages.length; otherP++) {
            if (otherP != personnageIndex) {
                domainesPersonnages[temps][otherP][lieuIndex] = false;
            }
        }
        propagerContraintes();
    }

    private boolean verifierCohérence(int temps) {
        for (int p = 0; p < personnages.length; p++) {
            boolean hasValidDomain = false;
            for (int l = 0; l < 6; l++) {
                if (domainesPersonnages[temps][p][l]) {
                    hasValidDomain = true;
                    break;
                }
            }
            if (!hasValidDomain) {
                return false;
            }
        }
        return true;
    }


    // Ajouter une contrainte sur le nombre de passages
    public void ajouterContrainteNombreDePassages(Personnage personnage, Lieu lieu, int nbPassages) {
        int personnageIndex = getIndexPersonnage(personnage.getNom().substring(0, 1));
        int lieuIndex = lieu.getId() - 1;

        int passages = 0;
        for (int t = 0; t < 6; t++) {
            if (domainesPersonnages[t][personnageIndex][lieuIndex]) {
                passages++;
            }
        }

        if (passages != nbPassages) {
            for (int t = 0; t < 6; t++) {
                domainesPersonnages[t][personnageIndex][lieuIndex] = false;
            }
        }

        propagerContraintes();
    }


    // Ajouter une contrainte sur le nombre de personnes dans une salle
    public void ajouterContrainteTemps(Lieu lieu, Temps temps, int nbPersonnages) {
        int tempsIndex = temps.getValeur() - 1;
        int lieuIndex = lieu.getId() - 1;

        int occupants = 0;
        for (int p = 0; p < personnages.length; p++) {
            if (domainesPersonnages[tempsIndex][p][lieuIndex]) {
                occupants++;
            }
        }

        if (occupants != nbPersonnages) {
            for (int p = 0; p < personnages.length; p++) {
                domainesPersonnages[tempsIndex][p][lieuIndex] = false;
            }
        }

        propagerContraintes();
    }

    private void propagerContraintes() {
        for (int t = 0; t < 6; t++) {
            for (int p = 0; p < personnages.length; p++) {
                int possibleLieuxCount = 0;
                int dernierLieuPossible = -1;

                for (int l = 0; l < 6; l++) {
                    if (domainesPersonnages[t][p][l]) {
                        possibleLieuxCount++;
                        dernierLieuPossible = l;
                    }
                }

                // Si un seul lieu est possible, réduire directement le domaine
                if (possibleLieuxCount == 1) {
                    for (int l = 0; l < 6; l++) {
                        domainesPersonnages[t][p][l] = (l == dernierLieuPossible);
                    }

                    // Supprimer ce personnage des autres domaines pour ce lieu
                    for (int otherP = 0; otherP < personnages.length; otherP++) {
                        if (otherP != p) {
                            domainesPersonnages[t][otherP][dernierLieuPossible] = false;
                        }
                    }
                }
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


    private int getIndexPersonnage(String personnage) {
        for (int i = 0; i < personnages.length; i++) {
            if (personnages[i].equals(personnage)) {
                return i;
            }
        }
        return -1;
    }
}
