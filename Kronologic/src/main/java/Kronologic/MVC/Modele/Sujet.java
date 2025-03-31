package Kronologic.MVC.Modele;

import Kronologic.MVC.Vue.Observateur;

public interface Sujet {
    void enregistrerObservateur(Observateur o);
    void supprimerObservateur(Observateur o);
    void notifierObservateurs();
}
