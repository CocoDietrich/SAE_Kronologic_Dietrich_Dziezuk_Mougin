package Kronologic.MVC.Controleur;

import Kronologic.MVC.Modele.ModeleJeu;
import Kronologic.MVC.Vue.VueFilmJoueur;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ControleurVisualiserFilmJoueur implements EventHandler<ActionEvent> {
    private final ModeleJeu modeleJeu;

    public ControleurVisualiserFilmJoueur(ModeleJeu modele) {
        this.modeleJeu = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

        VueFilmJoueur vueFilmJoueur = new VueFilmJoueur(modeleJeu);
        ControleurFilmJoueur controleurFilmJoueur = new ControleurFilmJoueur(modeleJeu);
        ControleurQuitterRegleFilm controleurQuitterRegleFilm = new ControleurQuitterRegleFilm(modeleJeu);
        vueFilmJoueur.retour.setOnAction(controleurQuitterRegleFilm);
        vueFilmJoueur.slider.valueProperty().addListener(controleurFilmJoueur);
        if (!this.modeleJeu.getModeleFilms().getObservateurs().isEmpty()){
            this.modeleJeu.getModeleFilms().getObservateurs().removeLast();
        }
        this.modeleJeu.getModeleFilms().enregistrerObservateur(vueFilmJoueur);

        BorderPane bp = new BorderPane(vueFilmJoueur);
        Scene scene = new Scene(bp, stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
        stage.show();

        modeleJeu.getModeleFilms().notifierObservateurs();
        System.out.println(modeleJeu.getModeleFilms().getObservateurs());
    }
}
