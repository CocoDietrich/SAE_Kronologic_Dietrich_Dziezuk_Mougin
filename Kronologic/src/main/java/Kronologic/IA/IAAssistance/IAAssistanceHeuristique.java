package Kronologic.IA.IAAssistance;

import Kronologic.IA.IADeduction.IADeductionHeuristique;
import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.Jeu.Indice.Indice;
import Kronologic.Jeu.Partie;

public class IAAssistanceHeuristique extends IAAssistance {
    private IADeductionHeuristique iaDeductionHeuristique;
    private Partie partie;

    public IAAssistanceHeuristique(IADeductionHeuristique iaDeductionHeuristique,Partie partie) {
        super();
        this.iaDeductionHeuristique = iaDeductionHeuristique;
        this.partie = partie;
    }

    @Override
    public String[] recommanderQuestionOptimal() {
        // TODO : Implémenter une heuristique pour recommander une question
        return null;
    }

    @Override
    public int simulerQuestionTemps(Lieu lieu, Temps temps) {
        return 0;
    }

    @Override
    public int simulerQuestionPersonnage(Lieu lieu, Personnage personnage) {
        return 0;
    }

    @Override
    public String corrigerDeductions() {
        // TODO : Comparer les déductions du joueur avec une logique heuristique
        return null;
    }
}
