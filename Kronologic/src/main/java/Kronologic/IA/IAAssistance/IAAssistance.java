package Kronologic.IA.IAAssistance;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;

public abstract class IAAssistance {
    // Méthode pour recommander une question optimale (par exemple, sur un lieu et un personnage)
    public abstract String[] recommanderQuestionOptimal();

    public abstract int predireTemps(Lieu lieu, Temps temps);

    public abstract int predirePersonnage(Lieu lieu, Personnage personnage);

    // Méthode pour corriger les déductions fausses faites par le joueur
    public abstract String corrigerDeductions();
}
