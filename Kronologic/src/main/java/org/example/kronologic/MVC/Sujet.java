package org.example.kronologic.MVC;

public interface Sujet {
    void ajouterObservateur(Observateur o);
    void supprimerObservateur(Observateur o);
    void notifierObservateurs();
}
