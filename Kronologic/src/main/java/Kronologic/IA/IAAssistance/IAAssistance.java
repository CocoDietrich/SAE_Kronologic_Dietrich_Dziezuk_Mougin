package Kronologic.IA.IAAssistance;

public abstract class IAAssistance {
    protected int modeRecommandation = 0; // 0 = par défaut (min), 1 = max, 2 = moyenne)

    // Méthodes pour recommander des actions au joueur
    public abstract String[] recommanderQuestionOptimale();

    // Méthode pour corriger les déductions du joueur
    public abstract String corrigerDeductions();

    public void setModeRecommandation(int modeRecommandation) {this.modeRecommandation = modeRecommandation;}

    public int getModeRecommandation() {return modeRecommandation;}
}
