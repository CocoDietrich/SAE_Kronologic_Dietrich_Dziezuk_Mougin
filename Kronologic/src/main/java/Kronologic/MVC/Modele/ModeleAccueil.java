package Kronologic.MVC.Modele;

import Kronologic.MVC.Vue.Observateur;

import java.util.ArrayList;
import java.util.List;

public class ModeleAccueil implements Sujet {

    private final List<Observateur> observateurs;

    public ModeleAccueil() {
        this.observateurs = new ArrayList<>();
    }

    public void initialiserPartie(String s) {
        if (s.equals("Jouer")) {
            notifierObservateurs();
        } else if (s.equals("IAJoueuse")) {
            // TODO : à implémenter quand l'IA le sera
        }
    }

    public void quitterJeu() {
        System.exit(0);
    }

    @Override
    public void enregistrerObservateur(Observateur o) {
        this.observateurs.add(o);
    }

    @Override
    public void supprimerObservateur(Observateur o) {
        this.observateurs.remove(o);
    }

    @Override
    public void notifierObservateurs() {
        for (Observateur o : observateurs){
            o.actualiser();
        }
    }
}
