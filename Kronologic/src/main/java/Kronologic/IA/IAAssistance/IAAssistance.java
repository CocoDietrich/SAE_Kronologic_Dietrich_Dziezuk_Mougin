package Kronologic.IA.IAAssistance;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.Jeu.Indice.Indice;

public abstract class IAAssistance {
    // Méthode pour recommander une question optimale (par exemple, sur un lieu et un personnage)
    public abstract String[] recommanderQuestionOptimal();

    // Méthode pour simuler les conséquences de poser une question sur un temps et un lieu
    public abstract int simulerQuestionTemps(Lieu lieu, Temps temps);

    // Méthode pour simuler les conséquences de poser une question sur un personnage et un lieu
    public abstract int simulerQuestionPersonnage(Lieu lieu, Personnage personnage);

    // Méthode pour corriger les déductions fausses faites par le joueur
    public abstract String corrigerDeductions();
}
