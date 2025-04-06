package Kronologic.IA.IAAssistance;

import Kronologic.IA.IADeduction.ModeleChocoSolver;
import Kronologic.Jeu.Partie;

public abstract class IAAssistance {
    protected Partie partie;

    protected int modeRecommandation = 0; // 0 = par défaut (min), 1 = max, 2 = moyenne)

    // Méthodes pour recommander des actions au joueur
    public abstract String[] recommanderQuestionOptimale();

    // Méthode pour corriger les déductions du joueur
    public abstract String corrigerDeductions();

    public void setModeRecommandation(int modeRecommandation) {this.modeRecommandation = modeRecommandation;}

    public int getModeRecommandation() {return modeRecommandation;}

    public Partie getPartie() {
        return partie;
    }

    public ModeleChocoSolver getIADeduction() {
        if (this instanceof IAAssistanceChocoSolver) {
            return ((IAAssistanceChocoSolver) this).getDeductionChocoSolver().getModele();
        }
        return null;
    }
}
