package Kronologic.IA.IADeduction;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.Jeu.Indice.Indice;
import Kronologic.Jeu.Partie;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import Kronologic.Jeu.Elements.Realite;

import java.util.List;

public class IADeductionChocoSolver extends IADeduction {

    private final Model model = new Model("Deduction IA Choco-Solver");
    private final String[] personnages = {"A", "B", "C", "D", "J", "S"};
    private final IntVar[][] positions = new IntVar[6][6];
    private final Partie partie;

    public IADeductionChocoSolver(Partie partie) {
        this.partie = partie;
        definirVariables();
        definirContraintesInitiales();
        definirContraintesRegles();
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
        List<Realite> positionsInitiales = partie.getDeroulement().positionsAuTemps(new Temps(1));
        for (Realite position : positionsInitiales) {
            int idLieu = position.getLieu().getId();  // Lieu initial du personnage
            int indexPersonnage = getIndexPersonnage(position.getPersonnage().getNom().substring(0, 1));
            model.arithm(positions[indexPersonnage][0], "=", idLieu).post();
        }
    }

    private void definirContraintesRegles() {
        int[][] sallesAdjacentes = {
                {2, 3},    // Salle 1 est adjacente à 2 et 3
                {1, 3},    // Salle 2 est adjacente à 1 et 3
                {1, 2, 4}, // Salle 3 est adjacente à 1, 2 et 4
                {3, 5, 6}, // Salle 4 est adjacente à 3, 5 et 6
                {4, 6},    // Salle 5 est adjacente à 4 et 6
                {4, 5}     // Salle 6 est adjacente à 4 et 5
        };

        for (int i = 0; i < personnages.length; i++) {
            for (int t = 0; t < 5; t++) {
                // Déplacement obligatoire : Pi,t ≠ Pi,t+1
                model.arithm(positions[i][t], "!=", positions[i][t + 1]).post();

                // Salle actuelle et suivante
                IntVar salleActuelle = positions[i][t];
                IntVar salleSuivante = positions[i][t + 1];

                // Définir les contraintes de salles adjacentes
                for (int salle = 1; salle <= 6; salle++) {
                    model.ifThen(
                            model.arithm(salleActuelle, "=", salle),
                            model.member(salleSuivante, sallesAdjacentes[salle - 1])
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

            // Contraintes de présence
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


    @Override
    public void analyserIndices(List<Indice> indices) {
        // Poster les contraintes déjà définies
        model.getSolver().reset();

        // Lancer la résolution
        if (model.getSolver().solve()) {
            System.out.println("===== Déduction Basée sur les Indices =====");

            // Extraction des résultats après résolution
            for (int i = 0; i < personnages.length; i++) {
                for (int t = 0; t < 6; t++) {
                    int salleTrouvee = positions[i][t].getValue();
                    System.out.printf("%s_T%d = Salle %d%n", personnages[i], t + 1, salleTrouvee);
                }
            }

            System.out.println("=== Exclusion des Suspects et Lieux Impossibles ===");
            exclureSuspectsEtLieuxImpossibles();
        } else {
            System.out.println("Aucune solution trouvée avec les indices actuels.");
        }
    }

    private void exclureSuspectsEtLieuxImpossibles() {
        for (int i = 0; i < personnages.length; i++) {
            boolean suspectElimine = true;

            // Vérifier si le personnage a été présent dans toutes les salles impossibles
            for (int salle = 1; salle <= 6; salle++) {
                boolean estPresent = false;

                for (int t = 0; t < 6; t++) {
                    if (positions[i][t].contains(salle)) {
                        estPresent = true;
                        break;
                    }
                }

                if (!estPresent) {
                    System.out.printf("Le personnage %s n'a jamais été dans la salle %d.%n", personnages[i], salle);
                } else {
                    suspectElimine = false;
                }
            }

            if (suspectElimine) {
                System.out.printf("Le personnage %s est exclu de l'enquête.%n", personnages[i]);
            }
        }
    }

    @Override
    public String afficherHistoriqueDeduction() {
        // TODO : Retourner l'historique des conclusions logiques et une proposition d'hypothèse
        return null;
    }
}
