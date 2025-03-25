package Kronologic.IA.IADeduction;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Realite;
import Kronologic.Jeu.Elements.Temps;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.extension.Tuples;
import org.chocosolver.solver.variables.IntVar;

import java.math.BigInteger;
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

    public void definirContrainteCoupable() {
        Tuples tableCoupable = new Tuples(true);

        for (int t = 2; t <= 6; t++) { // Temps commence à 2 (pas de crime au temps 1)
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

            // On impose qu'il y ait **exactement** 2 personnes dans la salle au moment du crime
            IntVar nbPersonnes = model.intVar("NbPersonnes_T" + t, 0, personnages.length);
            model.sum(presences, "=", nbPersonnes).post();

            // Si deux personnes sont présentes, on **fixe** le temps et le lieu du crime
            model.ifThen(
                    model.arithm(nbPersonnes, "=", 2),
                    model.and(
                            model.arithm(coupableTemps, "=", t),
                            model.arithm(coupableLieu, "=", positions[getIndexPersonnage("D")][t - 1])
                    )
            );

            // Ajout de toutes les combinaisons possibles pour restreindre le coupable
            for (int i = 0; i < personnages.length; i++) {
                if (!personnages[i].equals("D")) { // Exclure le détective
                    for (int lieu = 1; lieu <= 6; lieu++) {
                        if (positions[i][t - 1].contains(lieu)) {
                            tableCoupable.add(i, t, lieu);
                        }
                    }
                }
            }

            // Réduction immédiate des suspects
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

        // Relier les suspects, le temps et le lieu du crime avec la tableCoupable
        model.table(new IntVar[]{coupablePersonnage, coupableTemps, coupableLieu}, tableCoupable).post();
    }

    private void propagerContraintes() {
        try {
            model.getSolver().propagate();
        } catch (Exception e) {
            e.printStackTrace();
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

    public String affichagePropagate() {
        StringBuilder historique = new StringBuilder();

        // Afficher les suspects, lieux et temps réduits
        historique.append(suspects());

        historique.append("===== 📜 Historique des Déductions =====\n\n");

        // Affichage des domaines des personnages
        for (int i = 0; i < personnages.length; i++) {
            historique.append(String.format("🕵️‍♂️ %s :\n", personnages[i]));

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


    public StringBuilder suspects() {
        StringBuilder historique = new StringBuilder();

        // Vérifier si toutes les variables coupables sont réduites à une seule valeur
        boolean coupableTrouve = coupablePersonnage.isInstantiated() &&
                coupableLieu.isInstantiated() &&
                coupableTemps.isInstantiated();

        if (coupableTrouve) {
            historique.append("===== 🎯 Coupable Identifié ! =====\n");

            String nomCoupable = personnages[coupablePersonnage.getValue()];
            int lieuCrime = coupableLieu.getValue();
            int tempsCrime = coupableTemps.getValue();

            historique.append(String.format("👤 Coupable : %s\n", nomCoupable));
            historique.append(String.format("📍 Lieu du crime : %d\n", lieuCrime));
            historique.append(String.format("⏳ Temps du crime : %d\n\n", tempsCrime));

        } else {
            historique.append("===== 🔍 Impossible d'identifier un coupable pour le moment =====\n");
            historique.append("⚠ Continuez à poser des questions pour réduire les suspects !\n\n");
        }

        return historique;
    }


    public IntVar[][] getPositions() {
        return positions;
    }

    public String[] getPersonnages() {
        return personnages;
    }

    public Model getModel() {
        return model;
    }

    public String getPersonnageNom(int i) {
        return personnages[i];
    }

    public IntVar getCoupablePersonnage() {
        System.out.println(coupablePersonnage);
        return coupablePersonnage; }
    public IntVar getCoupableLieu() { return coupableLieu; }
    public IntVar getCoupableTemps() { return coupableTemps; }

}