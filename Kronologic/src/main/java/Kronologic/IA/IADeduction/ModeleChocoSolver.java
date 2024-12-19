package Kronologic.IA.IADeduction;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Realite;
import Kronologic.Jeu.Elements.Temps;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

import java.util.List;

public class ModeleChocoSolver {

    private final Model model;
    private final IntVar[][] positions = new IntVar[6][6];
    private IntVar coupablePersonnage;
    private IntVar coupableSalle;
    private IntVar coupableTemps;
    private final String[] personnages;
    private final List<Realite> positionsInitiales;

    public ModeleChocoSolver(String[] personnages, int[][] sallesAdjacentes, List<Realite> positionsInitiales) {
        this.model = new Model("Deduction IA Choco-Solver");
        this.personnages = personnages;
        this.positionsInitiales = positionsInitiales;

        definirVariables();
        definirContraintesInitiales();
        definirContraintesRegles(sallesAdjacentes);
    }

    private void definirVariables() {
        for (int i = 0; i < personnages.length; i++) {
            for (int t = 0; t < 6; t++) {
                String variableName = personnages[i] + "_T" + (t + 1);
                positions[i][t] = model.intVar(variableName, 1, 6);
            }
        }
        coupablePersonnage = model.intVar("CoupablePersonnage", 1, 6);
        coupableSalle = model.intVar("CoupableSalle", 1, 6);
        coupableTemps = model.intVar("CoupableTemps", 1, 6);
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

                // Déplacement obligatoire
                model.arithm(salleActuelle, "!=", salleSuivante).post();

                // Salles adjacentes
                for (int salle = 1; salle <= 6; salle++) {
                    model.ifThen(
                            model.arithm(salleActuelle, "=", salle),
                            model.member(salleSuivante, sallesAdjacentes[salle - 1])
                    );
                }
            }
        }

        // Contrainte sur le coupable : seul avec le détective
        int indexDetective = getIndexPersonnage("D");

        for (int t = 0; t < 6; t++) {
            IntVar salleDetective = positions[indexDetective][t];

            // Vérifie que le coupable était seul avec le détective
            for (int i = 0; i < personnages.length; i++) {
                if (i != indexDetective) {
                    IntVar sallePerso = positions[i][t];

                    model.ifThen(
                            model.and(
                                    model.arithm(salleDetective, "=", sallePerso),
                                    model.sum(
                                            new IntVar[]{
                                                    model.arithm(positions[getIndexPersonnage("A")][t], "=", salleDetective).reify(),
                                                    model.arithm(positions[getIndexPersonnage("B")][t], "=", salleDetective).reify(),
                                                    model.arithm(positions[getIndexPersonnage("C")][t], "=", salleDetective).reify(),
                                                    model.arithm(positions[getIndexPersonnage("J")][t], "=", salleDetective).reify(),
                                                    model.arithm(positions[getIndexPersonnage("S")][t], "=", salleDetective).reify()
                                            }, "=", 1)
                            ),
                            model.and(
                                    model.arithm(coupablePersonnage, "=", i + 1),
                                    model.arithm(coupableSalle, "=", salleDetective),
                                    model.arithm(coupableTemps, "=", t + 1)
                            )
                    );
                }
            }
        }
    }

    public void ajouterContraintePersonnage(Personnage personnage, Lieu lieu, int temps) {
        int indexPersonnage = getIndexPersonnage(personnage.getNom().substring(0, 1));

        if (indexPersonnage != -1 && temps >= 1 && temps <= 6) {
            model.arithm(positions[indexPersonnage][temps - 1], "=", lieu.getId()).post();
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
    }

    public void ajouterContrainteTemps(Lieu lieu, Temps temps, int nbPersonnages) {
        IntVar[] personnagesAuTempsT = new IntVar[personnages.length];

        for (int i = 0; i < personnages.length; i++) {
            personnagesAuTempsT[i] = model.intVar("Presence_" + personnages[i] + "_T" + temps.getValeur(), 0, 1);

            // Contraintes de presence
            model.ifThenElse(
                    model.arithm(positions[i][temps.getValeur() - 1], "=", lieu.getId()),
                    model.arithm(personnagesAuTempsT[i], "=", 1),
                    model.arithm(personnagesAuTempsT[i], "=", 0)
            );
        }
        model.sum(personnagesAuTempsT, "=", nbPersonnages).post();
    }

    private int getIndexPersonnage(String personnage) {
        for (int i = 0; i < personnages.length; i++) {
            if (personnages[i].equals(personnage)) {
                return i;
            }
        }
        return -1;
    }

    public String affichagePropagate(){
        StringBuilder historique = new StringBuilder();
        historique.append("===== Historique des Déductions =====\n");

        try {
            model.getSolver().propagate();
        } catch (Exception e) {
            historique.append("Erreur de propagation : ").append(e.getMessage()).append("\n");
            return historique.toString();
        }

        // Affichage des déductions
        for (int i = 0; i < personnages.length; i++) {
            for (int t = 0; t < 6; t++) {
                IntVar position = positions[i][t];

                if (position.isInstantiated()) {
                    int salleFixee = position.getValue();
                    historique.append(String.format("%s, Salle %d, Temps %d => OUI%n", personnages[i], salleFixee, t + 1));

                    for (int salle = 1; salle <= 6; salle++) {
                        if (salle != salleFixee) {
                            historique.append(String.format("%s, Salle %d, Temps %d => NON%n", personnages[i], salle, t + 1));
                        }
                    }
                } else {
                    for (int salle = 1; salle <= 6; salle++) {
                        if (!position.contains(salle)) {
                            historique.append(String.format("%s, Salle %d, Temps %d => NON%n", personnages[i], salle, t + 1));
                        }
                    }
                }
            }
        }

        // Affichage du coupable si trouvé
        if (coupablePersonnage.isInstantiated() && coupableSalle.isInstantiated() && coupableTemps.isInstantiated()) {
            String nomCoupable = personnages[coupablePersonnage.getValue() - 1];
            historique.append("\n===== Coupable Identifié =====\n");
            historique.append(String.format("Coupable: %s, Salle %d, Temps %d%n",
                    nomCoupable, coupableSalle.getValue(), coupableTemps.getValue()));
        } else {
            historique.append("\nCoupable non déterminé pour le moment.\n");
        }

        return historique.toString();
    }
}
