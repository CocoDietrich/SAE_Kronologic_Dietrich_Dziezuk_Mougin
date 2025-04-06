package Kronologic.MVC.Controleur.Accueil;

import Kronologic.MVC.Controleur.ControleurFilmJoueur;
import Kronologic.MVC.Controleur.ControleurFilmRealite;
import Kronologic.MVC.Modele.ModeleJeu;
import Kronologic.MVC.Vue.PopUps.VuePopUpDeduction;
import Kronologic.MVC.Vue.VueFilmJoueur;
import Kronologic.MVC.Vue.VueFilmRealite;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Optional;

public class ControleurFilmIAJoueuse implements EventHandler<ActionEvent> {

    private static final String RETOUR = "retour";

    private final ModeleJeu modeleJeu;

    public ControleurFilmIAJoueuse(ModeleJeu modeleJeu) {
        this.modeleJeu = modeleJeu;
    }

    private void finDeJeu(Stage stage){
        stage.close();
        Optional<VuePopUpDeduction> vuePopUpDeduction = modeleJeu.getObservateurs().stream()
                .filter(VuePopUpDeduction.class::isInstance)
                .map(VuePopUpDeduction.class::cast)
                .findFirst();

        vuePopUpDeduction.ifPresent(vue -> modeleJeu.quitter(RETOUR, vue.getStage()));
    }

    @Override
    public void handle(ActionEvent event) {
        Object source = event.getSource();
        if (!(source instanceof Button button)) {
            return;
        }

        Stage stage = (Stage) button.getScene().getWindow();

        VueFilmJoueur vueFilmJoueur = new VueFilmJoueur(modeleJeu);
        ControleurFilmJoueur controleurFilmJoueur = new ControleurFilmJoueur(modeleJeu);
        vueFilmJoueur.retour.setOnAction(_ -> finDeJeu(stage));
        vueFilmJoueur.slider.valueProperty().addListener(controleurFilmJoueur);
        this.modeleJeu.getModeleFilms().enregistrerObservateur(vueFilmJoueur);

        vueFilmJoueur.actualiser();

        BorderPane bp = new BorderPane(vueFilmJoueur);

        Scene scene = new Scene(bp);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();

        modeleJeu.notifierObservateurs();
    }
}