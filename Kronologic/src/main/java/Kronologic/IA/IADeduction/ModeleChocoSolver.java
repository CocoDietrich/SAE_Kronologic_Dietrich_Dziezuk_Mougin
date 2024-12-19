package Kronologic.IA.IADeduction;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Realite;
import Kronologic.Jeu.Elements.Temps;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.extension.Tuples;
import org.chocosolver.solver.variables.IntVar;

import java.util.ArrayList;
import java.util.List;

public class ModeleChocoSolver {

    private final Model model;
    private final IntVar[][] positions = new IntVar[6][6];
    private final String[] personnages;
    private final List<Realite> positionsInitiales;

    public ModeleChocoSolver(String[] personnages, int[][] sallesAdjacentes, List<Realite> positionsInitiales) {
        this.model = new Model("Deduction IA Choco-Solver");
        this.personnages = personnages;
        this.positionsInitiales = positionsInitiales;

        definirVariables();
        definirContraintesInitiales();
        definirContraintesRegles(sallesAdjacentes);
        try {
            model.getSolver().propagate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void definirVariables() {
        for (int i = 0; i < personnages.length; i++) {
            for (int t = 0; t < 6; t++) {
                String variableName = personnages[i] + "_T" + (t + 1);
                positions[i][t] = model.intVar(variableName, 1, 6);
            }
        }
    }

    private void definirContraintesInitiales() {
        for (Realite position : positionsInitiales) {
            int idLieu = position.getLieu().getId();
            int indexPersonnage = getIndexPersonnage(position.getPersonnage().getNom().substring(0, 1));
            model.arithm(positions[indexPersonnage][0], "=", idLieu).post();
        }
    }

    private void definirContraintesRegles(int[][] sallesAdjacentes) {
        for (int i = 0; i < personnages.length; i++) {
            for (int t = 0; t < 5; t++) {
                IntVar salleActuelle = positions[i][t];
                IntVar salleSuivante = positions[i][t + 1];

                // Création de la table de déplacements valides
                List<int[]> tuplesValides = new ArrayList<>();

                for (int salle = 1; salle <= 6; salle++) {
                    for (int adj : sallesAdjacentes[salle - 1]) {
                        tuplesValides.add(new int[]{salle, adj});
                    }
                }

                // Conversion en tableau
                int[][] table = tuplesValides.toArray(new int[0][]);

                // Application de la contrainte table
                model.table(new IntVar[]{salleActuelle, salleSuivante}, new Tuples(table, true), "CT+").post();
            }
        }
    }

    public void ajouterContraintePersonnage(Personnage personnage, Lieu lieu, int temps) {
        int indexPersonnage = getIndexPersonnage(personnage.getNom().substring(0, 1));

        if (indexPersonnage != -1 && temps >= 1 && temps <= 6) {
            model.arithm(positions[indexPersonnage][temps - 1], "=", lieu.getId()).post();
        }

        try {
            model.getSolver().propagate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ajouterContrainteNombreDePassages(Personnage personnage, Lieu lieu, int nbPassages) {
        int indexPersonnage = getIndexPersonnage(personnage.getNom().substring(0, 1));

        if (indexPersonnage != -1) {
            IntVar[] passages = new IntVar[6];

            for (int t = 0; t < 6; t++) {
                passages[t] = model.intVar("Presence_" + personnage.getNom() + "_T" + (t + 1), 0, 1);

                model.ifThenElse(
                        model.arithm(positions[indexPersonnage][t], "=", lieu.getId()),
                        model.arithm(passages[t], "=", 1),
                        model.arithm(passages[t], "=", 0)
                );
            }
            model.sum(passages, "=", nbPassages).post();
        }

        try {
            model.getSolver().propagate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ajouterContrainteTemps(Lieu lieu, Temps temps, int nbPersonnages) {
        int tempsIndex = temps.getValeur() - 1;

        // Contraintes de présence pour chaque personnage
        for (int i = 0; i < personnages.length; i++) {
            IntVar position = positions[i][tempsIndex];
            if (nbPersonnages == 0) {
                // Supprime la salle du domaine si personne ne peut y être
                try {
                    position.removeValue(lieu.getId(), null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // Contrainte de présence sinon
                IntVar presence = model.intVar("Presence_" + personnages[i] + "_T" + temps.getValeur(), 0, 1);
                model.ifThenElse(
                        model.arithm(position, "=", lieu.getId()),
                        model.arithm(presence, "=", 1),
                        model.arithm(presence, "=", 0)
                );
            }
        }

        // Contrainte de somme globale
        if (nbPersonnages > 0) {
            IntVar[] presences = new IntVar[personnages.length];
            for (int i = 0; i < personnages.length; i++) {
                presences[i] = model.intVar("Presence_" + personnages[i] + "_T" + temps.getValeur(), 0, 1);
            }
            model.sum(presences, "=", nbPersonnages).post();
        }

        // Propagation immédiate pour appliquer la contrainte
        try {
            model.getSolver().propagate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private int getIndexPersonnage(String personnage) {
        for (int i = 0; i < personnages.length; i++) {
            if (personnages[i].equals(personnage)) {
                return i;
            }
        }
        return -1;
    }

    public String affichagePropagate() {
        StringBuilder historique = new StringBuilder();
        historique.append("===== Historique des Déductions =====\n");

        // Affichage des domaines des personnages
        for (int i = 0; i < personnages.length; i++) {
            historique.append(personnages[i]).append(" :\n");

            for (int t = 0; t < 6; t++) {
                IntVar position = positions[i][t];
                StringBuilder domaine = new StringBuilder("{");

                if (position.isInstantiated()) {
                    domaine.append(position.getValue());
                } else {
                    for (int val = position.getLB(); val <= position.getUB(); val = position.nextValue(val)) {
                        domaine.append(val).append(", ");
                    }
                    if (domaine.length() > 1) {
                        domaine.setLength(domaine.length() - 2); // Supprime la dernière virgule et l'espace
                    }
                }
                domaine.append("}");
                historique.append(String.format("     - Temps %d : %s%n", t + 1, domaine));
            }
            historique.append("\n");
        }


        return historique.toString();
    }

}
