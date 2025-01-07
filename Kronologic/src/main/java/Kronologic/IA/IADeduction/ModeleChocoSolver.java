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
    private IntVar coupablePersonnage;
    private IntVar coupableLieu;
    private IntVar coupableTemps;

    public ModeleChocoSolver(String[] personnages, int[][] sallesAdjacentes, List<Realite> positionsInitiales) {
        this.model = new Model("Deduction IA Choco-Solver");
        this.personnages = personnages;
        this.positionsInitiales = positionsInitiales;

        definirVariables();
        definirContraintesInitiales();
        definirContraintesRegles(sallesAdjacentes);
        definirContrainteCoupable();

        propagerContraintes();
    }

    // Définir les variables
    private void definirVariables() {
        for (int i = 0; i < personnages.length; i++) {
            for (int t = 0; t < 6; t++) {
                String variableName = personnages[i] + "_T" + (t + 1);
                positions[i][t] = model.intVar(variableName, 1, 6);
            }
        }

        coupablePersonnage = model.intVar("CoupablePersonnage", 0, personnages.length - 1);
        coupableLieu = model.intVar("CoupableLieu", 1, 6);
        coupableTemps = model.intVar("CoupableTemps", 1, 6);

    }

    // Définir les contraintes initiales
    private void definirContraintesInitiales() {
        for (Realite position : positionsInitiales) {
            int idLieu = position.getLieu().getId();
            int indexPersonnage = getIndexPersonnage(position.getPersonnage().getNom().substring(0, 1));
            model.arithm(positions[indexPersonnage][0], "=", idLieu).post();
        }
    }

    // Définir les contraintes du coupable
    public void definirContrainteCoupable() {
        IntVar[] suspects = new IntVar[personnages.length - 1];
        int suspectIndex = 0;

        // Initialiser les suspects (exclure le détective)
        for (String personnage : personnages) {
            if (!personnage.equals("D")) { // Exclure le détective
                suspects[suspectIndex++] = model.intVar("Suspect_" + personnage, 0, 1); // 1 si suspect, 0 sinon
            }
        }

        for (int t = 0; t < 6; t++) { // Pour chaque temps
            IntVar[] presences = new IntVar[personnages.length];
            for (int i = 0; i < personnages.length; i++) {
                presences[i] = model.intVar("Presence_" + personnages[i] + "_T" + (t + 1), 0, 1);

                // Vérifier si le personnage est présent dans la même salle que le détective
                model.ifThenElse(
                        model.arithm(positions[i][t], "=", positions[getIndexPersonnage("D")][t]),
                        model.arithm(presences[i], "=", 1),
                        model.arithm(presences[i], "=", 0)
                );
            }

            // Vérifier si seulement deux personnes (le détective et un autre) sont dans la salle
            IntVar nbPersonnes = model.intVar("NbPersonnes_T" + (t + 1), 0, personnages.length);
            model.sum(presences, "=", nbPersonnes).post();

            for (int i = 0; i < personnages.length; i++) {
                if (!personnages[i].equals("D")) { // Exclure le détective
                    IntVar estSeulAvecDetective = model.intVar("SeulAvecDetective_" + personnages[i] + "_T" + (t + 1), 0, 1);
                    IntVar suspect = suspects[i < suspectIndex ? i : i - 1];

                    // Vérifier si le personnage est seul avec le détective
                    model.ifThenElse(
                            model.and(
                                    model.arithm(nbPersonnes, "=", 2), // Deux personnes dans la salle
                                    model.arithm(positions[i][t], "=", positions[getIndexPersonnage("D")][t])
                            ),
                            model.arithm(estSeulAvecDetective, "=", 1),
                            model.arithm(estSeulAvecDetective, "=", 0)
                    );

                    // Si le personnage est seul avec le détective, il est coupable
                    model.ifThen(
                            model.arithm(estSeulAvecDetective, "=", 1),
                            model.and(
                                    model.arithm(coupablePersonnage, "=", i),
                                    model.arithm(coupableLieu, "=", positions[i][t]),
                                    model.arithm(coupableTemps, "=", t + 1),
                                    model.arithm(suspect, "=", 1)
                            )
                    );

                    // Si le personnage n'est pas seul avec le détective, il est innocenté
                    model.ifThen(
                            model.or(
                                    model.arithm(estSeulAvecDetective, "=", 0),
                                    model.arithm(nbPersonnes, "!=", 2)
                            ),
                            model.arithm(suspect, "=", 0)
                    );
                }
            }

            // Réduire le domaine de coupableTemps si on a un indice sur deux personnes présentes
            model.ifThen(
                    model.arithm(nbPersonnes, "=", 2),
                    model.arithm(coupableTemps, "=", t + 1)
            );
        }

        // Ajouter une contrainte pour réduire le domaine du lieu du coupable si le détective est seul avec quelqu'un
        if (coupableTemps.isInstantiated()) {
            model.ifThen(
                    model.arithm(coupableTemps, "!=", 1),
                    model.arithm(coupableLieu, "=", positions[getIndexPersonnage("D")][coupableTemps.getValue() - 1])
            );
        }

        propagerContraintes();
    }


    // Définir les contraintes de déplacements
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

    // Ajouter une contrainte de position pour un personnage
    public void ajouterContraintePersonnage(Personnage personnage, Lieu lieu, int temps) {
        int indexPersonnage = getIndexPersonnage(personnage.getNom().substring(0, 1));
        if (temps >= 1 && temps <= 6) {
            model.arithm(positions[indexPersonnage][temps - 1], "=", lieu.getId()).post();
        }
        propagerContraintes();
    }

    // Ajouter une contrainte de nombre de passages pour un personnage
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
        if (nbPassages == 3) {
            gererTempsInstancies(indexPersonnage, lieu);
        }
        propagerContraintes();
    }

    // Gérer les temps instanciés
    private void gererTempsInstancies(int indexPersonnage, Lieu lieu) {
        for (int t = 0; t < 6; t++) {
            if (positions[indexPersonnage][t].isInstantiated()) {
                int confirmedSalle = positions[indexPersonnage][t].getValue();

                // S'il y a une salle déjà instanciée qui correspond au lieu
                if (confirmedSalle == lieu.getId()) {
                    int[] tempsPattern = ((t + 1) % 2 == 0) ? new int[]{2, 4, 6} : new int[]{1, 3, 5};
                    for (int i = 0; i < 6; i++) {
                        if (i != t) {
                            if (contains(tempsPattern, i + 1)) {
                                model.arithm(positions[indexPersonnage][i], "=", lieu.getId()).post();
                            } else {
                                model.arithm(positions[indexPersonnage][i], "!=", lieu.getId()).post();
                            }
                        }
                    }
                    return;
                }
            }
        }
    }

    // Vérifier si un tableau contient une valeur
    private boolean contains(int[] array, int value) {
        for (int element : array) {
            if (element == value) {
                return true;
            }
        }
        return false;
    }

    // Ajouter une contrainte de nombre de personnes dans une salle à un temps donné
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

        // Afficher les suspects
        if (coupablePersonnage.isInstantiated() &&
                coupableLieu.isInstantiated() &&
                coupableTemps.isInstantiated()) {
            historique.append("===== Coupable Identifié =====\n")
                    .append("Coupable: ").append(personnages[coupablePersonnage.getValue()])
                    .append(", Salle ").append(coupableLieu.getValue())
                    .append(", Temps ").append(coupableTemps.getValue())
                    .append("\n");
        } else {
            historique.append("===== Suspects =====\n");
            for (int i = 0; i < personnages.length; i++) {
                if (!personnages[i].equals("D")) { // Exclure le détective
                    historique.append(personnages[i]);
                    if (i < personnages.length - 1) {
                        historique.append(",");
                    }
                }
            }
            historique.append("\n");
        }


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

    public IntVar[][] getPositions() {
        return positions;
    }

}
