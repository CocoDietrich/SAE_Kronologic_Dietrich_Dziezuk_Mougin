package Kronologic.IA.IADeduction;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Realite;
import Kronologic.Jeu.Elements.Temps;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.extension.Tuples;
import org.chocosolver.solver.variables.IntVar;

import java.util.List;

public class ModeleChocoSolver {

    private final Model model;
    private final IntVar[][] positions = new IntVar[6][6];
    private final String[] personnages;
    private final List<Realite> positionsInitiales;
    private final int[][] sallesAdjacentes;

    public ModeleChocoSolver(String[] personnages, int[][] sallesAdjacentes, List<Realite> positionsInitiales) {
        this.model = new Model("Deduction IA Choco-Solver");
        this.personnages = personnages;
        this.positionsInitiales = positionsInitiales;
        this.sallesAdjacentes = sallesAdjacentes;

        definirVariables();
        definirContraintesInitiales();
        definirContraintesRegles(sallesAdjacentes);

        propagerContraintes();
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
                Tuples table = new Tuples(true);
                for (int salle = 1; salle <= 6; salle++) {
                    for (int adj : sallesAdjacentes[salle - 1]) {
                        table.add(salle, adj);
                    }
                }

                model.table(new IntVar[]{salleActuelle, salleSuivante}, table, "CT+").post();
            }
        }
    }

    public void ajouterContraintePersonnage(Personnage personnage, Lieu lieu, int temps) {
        int indexPersonnage = getIndexPersonnage(personnage.getNom().substring(0, 1));
        if (temps >= 1 && temps <= 6) {
            model.arithm(positions[indexPersonnage][temps - 1], "=", lieu.getId()).post();
        }
        propagerContraintes();
    }

    public void ajouterContrainteNombreDePassages(Personnage personnage, Lieu lieu, int nbPassages) {
        int indexPersonnage = getIndexPersonnage(personnage.getNom().substring(0, 1));
        IntVar[] presences = new IntVar[6];

        // Variables binaires pour indiquer la présence dans le lieu
        for (int t = 0; t < 6; t++) {
            presences[t] = model.intVar("Presence_" + personnage.getNom() + "_T" + (t + 1), 0, 1);
            // Table pour associer position et présence
            Tuples table = new Tuples(true);
            for (int val = positions[indexPersonnage][t].getLB(); val <= positions[indexPersonnage][t].getUB(); val++) {
                table.add(val, val == lieu.getId() ? 1 : 0);
            }
            model.table(new IntVar[]{positions[indexPersonnage][t], presences[t]}, table).post();
        }

        // Contraindre la somme des présences au nombre de passages
        model.sum(presences, "=", nbPassages).post();

        // Si le nombre de passages est strictement positif
        if (nbPassages > 0) {
            // Étape 1 : Vérifier les temps instanciés
            boolean foundInstantiated = false;
            for (int t = 0; t < 6; t++) {
                if (positions[indexPersonnage][t].isInstantiated()) {
                    foundInstantiated = true;
                    int confirmedSalle = positions[indexPersonnage][t].getValue();
                    // Si la salle confirmée correspond à celle de l'indice
                    if (confirmedSalle == lieu.getId()) {
                        int[] tempsPattern = ((t + 1) % 2 == 0) ? new int[]{2, 4, 6} : new int[]{1, 3, 5};

                        // Réduction des domaines pour les autres temps
                        for (int i = 0; i < 6; i++) {
                            if (i != t) {
                                if (contains(tempsPattern, i + 1)) {
                                    model.arithm(positions[indexPersonnage][i], "=", lieu.getId()).post();
                                } else {
                                    model.arithm(positions[indexPersonnage][i], "!=", lieu.getId()).post();
                                }
                            }
                        }
                        break;
                    }
                }
            }

            // Étape 2 : Si aucune salle n'est instanciée, appliquer les patterns temporels
            if (!foundInstantiated) {
                Tuples validPatterns = new Tuples(true);
                validPatterns.add(1, 3, 5);
                validPatterns.add(2, 4, 6);
                IntVar[] timeIndices = new IntVar[nbPassages];
                for (int i = 0; i < nbPassages; i++) {
                    timeIndices[i] = model.intVar("Time_" + (i + 1), 1, 6);
                }
                model.table(timeIndices, validPatterns).post();
            }

            // Étape 3 : Ajouter des contraintes de déplacements cohérents
            Tuples validMoves = new Tuples(true);
            for (int salle = 1; salle <= 6; salle++) {
                for (int adj : sallesAdjacentes[salle - 1]) {
                    validMoves.add(salle, adj);
                }
            }
            for (int i = 1; i < 6; i++) {
                model.table(new IntVar[]{positions[indexPersonnage][i - 1], positions[indexPersonnage][i]}, validMoves).post();
            }

            // Étape 4 : Lier les positions aux présences
            for (int t = 0; t < 6; t++) {
                model.ifThenElse(
                        model.arithm(positions[indexPersonnage][t], "=", lieu.getId()),
                        model.arithm(presences[t], "=", 1),
                        model.arithm(presences[t], "=", 0)
                );
            }
        }
        propagerContraintes();
    }

    private boolean contains(int[] array, int value) {
        for (int element : array) {
            if (element == value) {
                return true;
            }
        }
        return false;
    }

    public void ajouterContrainteTemps(Lieu lieu, Temps temps, int nbPersonnages) {
        IntVar[] presences = new IntVar[personnages.length];

        // Présences dans le lieu pour chaque personnage
        for (int i = 0; i < personnages.length; i++) {
            presences[i] = model.intVar("Presence_" + personnages[i] + "_T" + temps.getValeur(), 0, 1);
            Tuples table = new Tuples(true);
            for (int val = positions[i][temps.getValeur() - 1].getLB(); val <= positions[i][temps.getValeur() - 1].getUB(); val++) {
                table.add(val, val == lieu.getId() ? 1 : 0);
            }
            model.table(new IntVar[]{positions[i][temps.getValeur() - 1], presences[i]}, table).post();
        }

        // Contraindre le nombre total de personnes présentes dans le lieu
        model.sum(presences, "=", nbPersonnages).post();

        // Si le nombre de personnes est strictement positif
        if (nbPersonnages > 0) {
            for (int i = 0; i < personnages.length; i++) {
                if (positions[i][temps.getValeur() - 1].isInstantiated()) {
                    int confirmedSalle = positions[i][temps.getValeur() - 1].getValue();

                    if (confirmedSalle == lieu.getId()) {
                        model.arithm(presences[i], "=", 1).post();
                    } else {
                        model.arithm(presences[i], "=", 0).post();
                    }
                }
            }
        }

        propagerContraintes();
    }

    private void propagerContraintes() {
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
        propagerContraintes();

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

    public IntVar[][] getPositions() {
        return positions;
    }

}
