package Kronologic.IA.IAAssistance;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;

public abstract class IAAssistance {
    public abstract String[] recommanderQuestionOptimale();

    public abstract String corrigerDeductions();
}
