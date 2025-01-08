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
        //TODO
    }


    // Ajouter une contrainte sur le nombre de personnes dans une salle
    public void ajouterContrainteTemps(Lieu lieu, Temps temps, int nbPersonnages) {
        //TODO
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