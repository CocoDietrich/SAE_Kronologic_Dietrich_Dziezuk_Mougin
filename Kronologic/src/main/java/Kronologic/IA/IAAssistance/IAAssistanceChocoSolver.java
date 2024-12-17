package Kronologic.IA.IAAssistance;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Indice.Indice;
import Kronologic.Jeu.Partie;

public class IAAssistanceChocoSolver extends IAAssistance {
    private Partie partie;

    public IAAssistanceChocoSolver(Partie partie) {
        super();
        this.partie = partie;
    }

    @Override
    public Indice recommanderQuestionOptimal() {
        // TODO : Utiliser Choco-Solver pour identifier la question optimale
        return null;
    }

    @Override
    public void simulerQuestion(Lieu lieu, Personnage personnage) {
        // TODO : Simuler les conséquences de poser une question sur le lieu/personnage
    }

    @Override
    public void corrigerDeductions() {
        // TODO : Comparer les déductions du joueur avec celles calculées par Choco-Solver
    }
}
