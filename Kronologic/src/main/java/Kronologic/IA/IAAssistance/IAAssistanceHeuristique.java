package Kronologic.IA.IAAssistance;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Indice.Indice;
import Kronologic.Jeu.Partie;

public class IAAssistanceHeuristique extends IAAssistance {
    private Partie partie;

    @Override
    public Indice recommanderQuestionOptimal() {
        // TODO : Implémenter une heuristique pour recommander une question
        return null;
    }

    @Override
    public void simulerQuestion(Lieu lieu, Personnage personnage) {
        // TODO : Simuler les conséquences de poser une question sur le lieu/personnage
    }

    @Override
    public void corrigerDeductions() {
        // TODO : Comparer les déductions du joueur avec une logique heuristique
    }
}
