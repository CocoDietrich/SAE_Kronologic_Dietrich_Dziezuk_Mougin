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
    public String[] recommanderQuestionOptimaleTriche() {
        // TODO : Implémenter une heuristique pour recommander une question
        return new String[]{"Aucune recommandation", "Vous avez déjà toutes les informations."};
    }

    @Override
    public String[] recommanderQuestionOptimaleTrichePas() {
        return new String[]{"Aucune recommandation", "Vous avez déjà toutes les informations."};
    }

    public int simulerTemps(Lieu lieu, Temps temps, int infoPublic, String infoPrive) {
        // TODO : Implémenter une heuristique pour prédire le temps
        return 0;
    }

    public int simulerPersonnage(Lieu lieu, Personnage personnage, int infoPublic, int infoPrive) {
        // TODO : Implémenter une heuristique pour prédire le personnage
        return 0;
    }

    @Override
    public String corrigerDeductions() {
        // TODO : Comparer les déductions du joueur avec une logique heuristique
        return null;
    }
}
