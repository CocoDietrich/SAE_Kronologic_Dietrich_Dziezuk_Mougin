package Kronologic.IA.IADeduction;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;

public abstract class IADeduction {
    // Méthode pour poser une question sur le temps
    public abstract void poserQuestionTemps(Lieu lieu, Temps temps, int infoPublic, String infoPrive);

    // Méthode pour poser une question sur un personnage
    public abstract void poserQuestionPersonnage(Personnage personnage, Lieu lieu, int infoPublic, int infoPrive);

    // Méthode pour afficher l'historique des déductions
    public abstract String afficherHistoriqueDeduction();
}