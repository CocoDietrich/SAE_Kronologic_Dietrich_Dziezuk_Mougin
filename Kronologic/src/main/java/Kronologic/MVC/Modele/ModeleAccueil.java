package Kronologic.MVC.Modele;


import Kronologic.MVC.Vue.Observateur;

import java.util.ArrayList;
import java.util.List;

public class ModeleAccueil implements Sujet {

    private List<Observateur> observateurs;

    public ModeleAccueil() {
        this.observateurs = new ArrayList<>();
    }

    public void initialiserPartie(String s) {
        // TODO
    }

    public void quitterJeu() {
        System.exit(0);
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
