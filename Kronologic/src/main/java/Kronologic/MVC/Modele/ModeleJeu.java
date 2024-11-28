package Kronologic.MVC.Modele;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.Jeu.Indice.Indice;
import Kronologic.Jeu.Partie;
import Kronologic.MVC.Vue.Observateur;

import java.util.ArrayList;
import java.util.List;

public class ModeleJeu implements Sujet {

    private List<Observateur> observateurs;
    private Partie partie;
    private boolean vueCarte;
    private Personnage deduPersonnage;
    private Lieu deduLieu;
    private Temps deduTemps;

    public ModeleJeu(){
        this.observateurs = new ArrayList<>();
        this.partie = null;
        this.vueCarte = true;
        this.deduPersonnage = null;
        this.deduLieu = null;
        this.deduTemps = null;
    }

    public void changerAffiichage(){
        //TODO
    }

    public Indice poserQuestion(){
        //TODO
        return null;
    }

    public void visualiserPoseQuestion(){
        //TODO
    }

    public void choix(Lieu l, Personnage p, Temps t){
        //TODO
    }

    public boolean faireDeduction(){
        //TODO
        return false;
    }

    public void visualiserDeduction(){
        //TODO
    }

    public void voirDeductionIA(){
        //TODO
    }

    public void demanderIndice(){
        //TODO
    }

    public void visualiserFilm(){
        //TODO
    }

    public void visualiserRegle(){
        //TODO
    }

    public void valider(){
        //TODO
    }

    public void quitter() {
        // TODO
    }

    @Override
    public void enregistrerObservateur(Observateur o) {

    }

    @Override
    public void supprimerObservateur(Observateur o) {

    }

    @Override
    public void notifierObservateurs() {

    }
}
