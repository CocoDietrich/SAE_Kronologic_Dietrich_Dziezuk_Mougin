package MVC.Jeu;

import Kronologic.Elements.Lieu;
import Kronologic.Elements.Personnage;
import Kronologic.Elements.Temps;
import Kronologic.Indice.Indice;
import Kronologic.Partie;
import MVC.Observateur;

import java.util.List;

public class ModeleJeu {

    private Partie partie;
    private List<Observateur> observateurs;

    public ModeleJeu(Partie p) {
        this.partie = p;
    }

    public void enregistrerObservateur(Observateur o) {
        observateurs.add(o);
    }

    public void supprimerObservateur(Observateur o) {
        observateurs.remove(o);
    }

    public void notifierObservateurs() {
        for (Observateur o : observateurs) {
            o.actualiser();
        }
    }

    public void initialiserPartie() {
        // TODO : initialiser la partie
    }

    public Indice poserQuestion(Lieu l, Personnage p , Temps t) {
        // TODO : poser une question
        return null;
    }

    public boolean faireDeduction(Lieu l, Personnage p , Temps t) {
        // TODO : poser une question
        return false;
    }

    public Indice demanderIndice() {
        // TODO : demander un indice
        return null;
    }

    public void quitterPartie() {
        // TODO : quitter la partie
    }
}
