package org.example.kronologic.MVC.Accueil;

public class ControleurAccueil {

    private ModeleAccueil modele;
    private VueAccueil vue;

    public ControleurAccueil(ModeleAccueil modele, VueAccueil vue) {
        this.modele = modele;
        this.vue = vue;
    }

    public void gererChoix(String option){
        // TODO : Gérer les choix de l'utilisateur
    }
}
