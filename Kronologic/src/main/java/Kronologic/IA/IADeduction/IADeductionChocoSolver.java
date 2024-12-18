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

    }

    @Override
    public String afficherHistoriqueDeduction() {
        StringBuilder historique = new StringBuilder();
        historique.append("===== Historique des Déductions =====\n");
        try {
            model.getSolver().propagate();
        } catch (Exception e) {
            System.out.println("Erreur de propagation : " + e.getMessage());
        }
        for (int i = 0; i < personnages.length; i++) {
            for (int t = 0; t < 6; t++) {
                IntVar position = positions[i][t];

                // Afficher les salles fixées ou exclues
                if (position.isInstantiated()) {
                    // Si la variable est instanciée, afficher la valeur certaine
                    int salleFixee = position.getValue();
                    historique.append(String.format("%s, Salle %d, Temps %d => OUI%n", personnages[i], salleFixee, t + 1));

                    // Marquer les autres salles comme impossibles
                    for (int salle = 1; salle <= 6; salle++) {
                        if (salle != salleFixee) {
                            historique.append(String.format("%s, Salle %d, Temps %d => NON%n", personnages[i], salle, t + 1));
                        }
                    }
                } else {
                    // Si la variable n'est pas instanciée, utiliser son domaine réduit
                    for (int salle = 1; salle <= 6; salle++) {
                        if (!position.contains(salle)) { // Salle exclue du domaine
                            historique.append(String.format("%s, Salle %d, Temps %d => NON%n", personnages[i], salle, t + 1));
                        }
                    }
                }
            }
        }
        return historique.toString();
    }

}