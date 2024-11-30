package Kronologic.MVC.Modele;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.Jeu.Indice.Indice;
import Kronologic.Jeu.Partie;
import Kronologic.MVC.Controleur.Accueil.ControleurInitialisation;
import Kronologic.MVC.Controleur.Accueil.ControleurQuitterJeu;
import Kronologic.MVC.Vue.Observateur;
import Kronologic.MVC.Vue.VueAccueil;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ModeleJeu implements Sujet {

    private List<Observateur> observateurs;
    private Partie partie;
    private boolean vueCarte;
    private Personnage deduPersonnage;
    private Lieu deduLieu;
    private Temps deduTemps;

    public ModeleJeu(Partie partie){
        this.observateurs = new ArrayList<>();
        this.partie = partie;
        this.vueCarte = true;
        this.deduPersonnage = null;
        this.deduLieu = null;
        this.deduTemps = null;
    }

    public void changerAffichage(){
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

    public void quitter(String idBouton, Stage stage) {
        switch (idBouton) {
            case "retour":
                ModeleAccueil modeleAccueil = new ModeleAccueil();

                VueAccueil vueAccueil = new VueAccueil();

                modeleAccueil.enregistrerObservateur(vueAccueil);

                BorderPane bp = new BorderPane();
                bp.setCenter(vueAccueil);

                ControleurInitialisation controleurInitialisation = new ControleurInitialisation(modeleAccueil);
                vueAccueil.jouer.setOnAction(controleurInitialisation);
                vueAccueil.IAJoueuse.setOnAction(controleurInitialisation);

                ControleurQuitterJeu controleurQuitterJeu = new ControleurQuitterJeu(modeleAccueil);
                vueAccueil.quitter.setOnAction(controleurQuitterJeu);


                Scene scene = new Scene(bp, stage.getWidth(), stage.getHeight());
                stage.setScene(scene);
                break;
            case "quitter":
                System.exit(0);
                break;
        }
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
