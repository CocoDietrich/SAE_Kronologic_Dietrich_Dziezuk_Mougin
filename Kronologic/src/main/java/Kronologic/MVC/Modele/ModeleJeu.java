package Kronologic.MVC.Modele;

import Kronologic.Jeu.Partie;
import Kronologic.MVC.Controleur.Accueil.ControleurInitialisation;
import Kronologic.MVC.Controleur.Accueil.ControleurQuitterJeu;
import Kronologic.MVC.Modele.SousModeleJeu.ModeleFilms;
import Kronologic.MVC.Modele.SousModeleJeu.ModeleIA;
import Kronologic.MVC.Modele.SousModeleJeu.ModeleNotes;
import Kronologic.MVC.Modele.SousModeleJeu.ModeleQuestionDeduction;
import Kronologic.MVC.Vue.*;
import Kronologic.MVC.Vue.PopUps.VuePopUpQuitter;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.*;


public class ModeleJeu implements Sujet {

    private final List<Observateur> observateurs;

    // TODO : A terme, inutile
    private static Partie partie;

    private boolean estVueCarte;

    // Sous-modèles
    private final ModeleNotes modeleNotes;
    private final ModeleQuestionDeduction modeleQuestionDeduction;
    private final ModeleIA modeleIA;
    private final ModeleFilms modeleFilms;

    public ModeleJeu(Partie partie) {
        this.observateurs = new ArrayList<>();
        ModeleJeu.partie = partie;
        this.estVueCarte = true;
        this.modeleNotes = new ModeleNotes(partie);
        this.modeleQuestionDeduction = new ModeleQuestionDeduction(partie);
        this.modeleIA = new ModeleIA(partie);
        this.modeleFilms = new ModeleFilms(partie);
    }

    public void changerAffichage(Stage stage) {
        this.estVueCarte = !this.estVueCarte;
        if (this.estVueCarte) {
            retourVueCarte(stage);
        } else {
            retourVueTableau(stage);
        }
    }

    // Méthode permettant de retourner à la vue de la carte
    public void retourVueCarte(Stage stage) {
        VueCarte vueCarte = null;
        for (Observateur o : observateurs) {
            if (o instanceof VueCarte) {
                vueCarte = (VueCarte) o;
                break;
            }
        }

        notifierObservateurs();
        BorderPane bp = new BorderPane(vueCarte);
        Scene scene = new Scene(bp, stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
        stage.show();
    }

    // Méthode permettant de retourner à la vue de la carte
    public void retourVueTableau(Stage stage) {
        VueTableau vueTableau = null;
        for (Observateur o : observateurs) {
            if (o instanceof VueTableau) {
                vueTableau = (VueTableau) o;
                break;
            }
        }

        notifierObservateurs();
        BorderPane bp = new BorderPane(vueTableau);
        Scene scene = new Scene(bp, stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
        stage.show();
    }

    public void visualiserRegle(Stage stage) {
        VueRegle vueRegle = new VueRegle();
        for (Observateur o : observateurs) {
            if (o instanceof VueRegle) {
                vueRegle = (VueRegle) o;
                break;
            }
        }

        BorderPane bp = new BorderPane(vueRegle);

        Scene scene = new Scene(bp, stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
        stage.show();
    }

    // Méthode affichant le pop-up de confirmation de quitter la partie
    public void afficherPopUpQuitter() {
        VuePopUpQuitter vuePopUpQuitter = null;

        for (Observateur o : observateurs) {
            if (o instanceof VuePopUpQuitter) {
                vuePopUpQuitter = (VuePopUpQuitter) o;
                break;
            }
        }

        assert vuePopUpQuitter != null;
        vuePopUpQuitter.afficherPopUp();
    }

    // Méthode permettant de quitter la partie
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
        for (Observateur o : observateurs) {
            o.actualiser();
        }
    }

    public List<Observateur> getObservateurs() {
        return observateurs;
    }

    public static Partie getPartie() {
        return partie;
    }

    public boolean estVueCarte() {
        return estVueCarte;
    }

    public ModeleNotes getModeleNotes() {
        return modeleNotes;
    }

    public ModeleQuestionDeduction getModeleQuestionDeduction() {
        return modeleQuestionDeduction;
    }

    public ModeleIA getModeleIA() {
        return modeleIA;
    }

    public ModeleFilms getModeleFilms() {
        return modeleFilms;
    }

    // Méthode permettant d'afficher la vue de pose de question
    public void visualiserPoseQuestion(Stage stage) {
        // On récupère la vuePoseQuestion dans la liste des observateurs
        VuePoseQuestion vuePoseQuestion = null;
        for (Observateur o : observateurs) {
            if (o instanceof VuePoseQuestion) {
                vuePoseQuestion = (VuePoseQuestion) o;
                break;
            }
        }

        BorderPane bp = new BorderPane(vuePoseQuestion);

        Scene scene = new Scene(bp, stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
        stage.show();
    }

    public void visualiserDeduction(Stage stage) {
        // On récupère la vuePoseQuestion dans la liste des observateurs
        VueDeduction vueDeduction = null;
        for (Observateur o : observateurs) {
            if (o instanceof VueDeduction) {
                vueDeduction = (VueDeduction) o;
                break;
            }
        }

        BorderPane bp = new BorderPane(vueDeduction);

        Scene scene = new Scene(bp, stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
        stage.show();
    }


}
