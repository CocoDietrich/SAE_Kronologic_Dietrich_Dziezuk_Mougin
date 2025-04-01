package Kronologic.IA.IAAssistance;

public abstract class IAAssistance {
    // Méthodes pour recommander des actions au joueur
    public abstract String[] recommanderQuestionOptimale();

    // Méthode pour corriger les déductions du joueur
    public abstract String corrigerDeductions();
}
