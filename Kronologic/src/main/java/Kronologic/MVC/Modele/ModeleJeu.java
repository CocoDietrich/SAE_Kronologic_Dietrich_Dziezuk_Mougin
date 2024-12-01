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
import Kronologic.MVC.Vue.VueCarte;
import Kronologic.MVC.Vue.VuePoseQuestion;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ModeleJeu implements Sujet {

    private List<Observateur> observateurs;
    public static Partie partie;
    private boolean vueCarte;
    private Personnage deduPersonnage;
    private Lieu deduLieu;
    private Temps deduTemps;

    public ModeleJeu(Partie partie){
        this.observateurs = new ArrayList<>();
        ModeleJeu.partie = partie;
        this.vueCarte = true;
        this.deduPersonnage = null;
        this.deduLieu = null;
        this.deduTemps = null;
    }

    // Méthode permettant de retourner à la vue de la carte
    public void retourVueCarte(Stage stage){
        VueCarte vueCarte = null;
        for (Observateur o : observateurs){
            if (o instanceof VueCarte){
                vueCarte = (VueCarte) o;
                break;
            }
        }

        BorderPane bp = new BorderPane(vueCarte);
        Scene scene = new Scene(bp, stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
        stage.show();
    }

    // Méthode permettant de stocker le lieu choisi pour la question posée par le joueur
    public void setLieuChoisi(Lieu lieu){
        VuePoseQuestion vuePoseQuestion = null;
        for (Observateur o : observateurs){
            if (o instanceof VuePoseQuestion){
                vuePoseQuestion = (VuePoseQuestion) o;
                break;
            }
        }
        assert vuePoseQuestion != null;
        vuePoseQuestion.lieuChoisi = lieu;
    }

    // Méthode permettant de stocker le temps choisi pour la question posée par le joueur
    public void setTempsChoisi(Temps temps){
        VuePoseQuestion vuePoseQuestion = null;
        for (Observateur o : observateurs){
            if (o instanceof VuePoseQuestion){
                vuePoseQuestion = (VuePoseQuestion) o;
                break;
            }
        }
        assert vuePoseQuestion != null;
        vuePoseQuestion.tempsChoisi = temps;
    }

    // Méthode permettant de stocker le personnage choisi pour la question posée par le joueur
    public void setPersonnageChoisi(Personnage personnage){
        VuePoseQuestion vuePoseQuestion = null;
        for (Observateur o : observateurs){
            if (o instanceof VuePoseQuestion){
                vuePoseQuestion = (VuePoseQuestion) o;
                break;
            }
        }
        assert vuePoseQuestion != null;
        vuePoseQuestion.personnageChoisi = personnage;
    }

    public void changerAffichage(){
        //TODO
    }

    public Indice poserQuestion(Stage stage){
        VuePoseQuestion vuePoseQuestion = null;
        for (Observateur o : observateurs){
            if (o instanceof VuePoseQuestion){
                vuePoseQuestion = (VuePoseQuestion) o;
                break;
            }
        }
        assert vuePoseQuestion != null;
        Indice i;
        if (vuePoseQuestion.personnageChoisi != null){
            i = partie.poserQuestionPersonnage(vuePoseQuestion.lieuChoisi, vuePoseQuestion.personnageChoisi);
        } else {
            i = partie.poserQuestionTemps(vuePoseQuestion.lieuChoisi, vuePoseQuestion.tempsChoisi);
        }
        partie.ajouterIndice(i);
        notifierObservateurs();
        System.out.println("Réponse à la question posée : " + i);
        retourVueCarte(stage);
        return i;
    }

    // Méthode permettant d'afficher la vue de pose de question
    public void visualiserPoseQuestion(Stage stage){
        // On récupère la vuePoseQuestion dans la liste des observateurs
        VuePoseQuestion vuePoseQuestion = null;
        for (Observateur o : observateurs){
            if (o instanceof VuePoseQuestion){
                vuePoseQuestion = (VuePoseQuestion) o;
                break;
            }
        }

        BorderPane bp = new BorderPane(vuePoseQuestion);

        Scene scene = new Scene(bp, stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
        stage.show();
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

    public List<Observateur> getObservateurs() {
        return observateurs;
    }
}
