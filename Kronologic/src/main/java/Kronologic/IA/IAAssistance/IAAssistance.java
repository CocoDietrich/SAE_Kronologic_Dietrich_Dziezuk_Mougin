package Kronologic.IA.IAAssistance;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;

public abstract class IAAssistance {
    // Méthode pour recommander une question optimale (par exemple, sur un lieu et un personnage) en trichant
    public abstract String[] recommanderQuestionOptimaleTriche();

    // Méthode pour recommander une question optimale (par exemple, sur un lieu et un personnage) sans tricher
    public abstract String[] recommanderQuestionOptimaleTrichePas();

    // Méthode pour corriger les déductions fausses faites par le joueur
    public abstract String corrigerDeductions();
}
