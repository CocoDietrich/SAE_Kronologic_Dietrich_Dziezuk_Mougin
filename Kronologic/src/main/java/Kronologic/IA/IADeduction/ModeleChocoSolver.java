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
    private IntVar coupablePersonnage;
    private IntVar coupableLieu;
    private IntVar coupableTemps;

    public ModeleChocoSolver(String[] personnages, int[][] sallesAdjacentes, List<Realite> positionsInitiales) {
        this.model = new Model("Deduction IA Choco-Solver");
        this.personnages = personnages;

        definirVariables();
        definirContraintesInitiales(positionsInitiales);
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
        coupableTemps = model.intVar("CoupableTemps", 2, 6);

    }

    // Définir les contraintes initiales
    private void definirContraintesInitiales(List<Realite> positionsInitiales) {
        for (Realite position : positionsInitiales) {
            int idLieu = position.getLieu().getId();
            int indexPersonnage = getIndexPersonnage(position.getPersonnage().getNom().substring(0, 1));
            model.arithm(positions[indexPersonnage][0], "=", idLieu).post();
        }
    }

    public void definirContrainteCoupable() {
        IntVar[] suspects = new IntVar[personnages.length - 1];
        int suspectIndex = 0;

        // Initialiser les suspects (exclure le détective)
        for (String personnage : personnages) {
            if (!personnage.equals("D")) { // Exclure le détective
                suspects[suspectIndex++] = model.intVar("Suspect_" + personnage, 0, 1); // 1 si suspect, 0 sinon
            }
        }

        Tuples tableCoupable = new Tuples(true);

        for (int t = 2; t <= 6; t++) { // Temps commence à 2 car meurtre pas au temps 1
            IntVar[] presences = new IntVar[personnages.length];
            for (int i = 0; i < personnages.length; i++) {
                presences[i] = model.intVar("Presence_" + personnages[i] + "_T" + t, 0, 1);

                // Vérifier si le personnage est présent dans la même salle que le détective
                model.ifThenElse(
                        model.arithm(positions[i][t - 1], "=", positions[getIndexPersonnage("D")][t - 1]),
                        model.arithm(presences[i], "=", 1),
                        model.arithm(presences[i], "=", 0)
                );
            }

            IntVar nbPersonnes = model.intVar("NbPersonnes_T" + t, 0, personnages.length);
            model.sum(presences, "=", nbPersonnes).post();

            // Réduire immédiatement le domaine du temps et du lieu si deux personnes sont présentes
            model.ifThen(
                    model.arithm(nbPersonnes, "=", 2),
                    model.and(
                            model.arithm(coupableTemps, "=", t),
                            model.arithm(coupableLieu, "=", positions[getIndexPersonnage("D")][t - 1])
                    )
            );

            // Ajouter toutes les combinaisons possibles (personnage, temps, lieu)
            for (int i = 0; i < personnages.length; i++) {
                if (!personnages[i].equals("D")) { // Exclure le détective
                    for (int lieu = 1; lieu <= 6; lieu++) {
                        if (positions[i][t - 1].contains(lieu)) {
                            tableCoupable.add(i, t, lieu);
                        }
                    }
                }
            }

            // Réduire les suspects directement
            for (int i = 0; i < personnages.length; i++) {
                if (!personnages[i].equals("D")) {
                    model.ifThen(
                            model.and(
                                    model.arithm(nbPersonnes, "=", 2),
                                    model.arithm(positions[i][t - 1], "=", positions[getIndexPersonnage("D")][t - 1])
                            ),
                            model.arithm(coupablePersonnage, "=", i)
                    );
                }
            }
        }

        // Relier les suspects, temps et lieux avec la table coupable
        model.table(new IntVar[]{coupablePersonnage, coupableTemps, coupableLieu}, tableCoupable).post();

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
            Tuples table = new Tuples(true);
            for (int val = positions[indexPersonnage][t].getLB(); val <= positions[indexPersonnage][t].getUB(); val++) {
                table.add(val, val == lieu.getId() ? 1 : 0);
            }
            model.table(new IntVar[]{positions[indexPersonnage][t], presences[t]}, table).post();
        }

        // Contraindre la somme des présences au nombre de passages
        model.sum(presences, "=", nbPassages).post();

        propagerContraintes();
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

        // Afficher les suspects,lieux,temps
        historique.append(suspetcs());

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

    public StringBuilder suspetcs() {
        StringBuilder historique = new StringBuilder();

        // Afficher les suspects
        historique.append("===== Suspects =====\n");
        for (int i = 0; i < personnages.length; i++) {
            if (!personnages[i].equals("D")) {
                if (coupablePersonnage.contains(i)) {
                    historique.append(personnages[i]).append(",");
                }
            }
        }
        historique.deleteCharAt(historique.length() - 1);
        historique.append("\n");

        // Afficher les lieux avec leur domaine
        historique.append("===== Lieux =====\n");
        StringBuilder lieux = new StringBuilder();
        for (int lieu = 1; lieu <= 6; lieu++) {
            if (coupableLieu.contains(lieu)) {
                lieux.append("Lieu ").append(lieu).append(",");
            }
        }
        lieux.deleteCharAt(lieux.length() - 1);
        historique.append(lieux).append("\n");

        // Afficher les temps avec leur domaine
        historique.append("===== Temps =====\n");
        StringBuilder temps = new StringBuilder();
        for (int t = 1; t <= 6; t++) {
            if (coupableTemps.contains(t)) {
                temps.append("Temps ").append(t).append(",");
            }
        }
        temps.deleteCharAt(temps.length() - 1);
        historique.append(temps).append("\n");

        return historique;
    }


    public IntVar[][] getPositions() {
        return positions;
    }

}