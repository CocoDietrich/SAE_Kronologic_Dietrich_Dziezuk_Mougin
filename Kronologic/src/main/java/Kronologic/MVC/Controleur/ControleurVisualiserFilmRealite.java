package Kronologic.MVC.Controleur;

import Kronologic.MVC.Modele.ModeleJeu;
import Kronologic.MVC.Vue.VueFilmJoueur;
import Kronologic.MVC.Vue.VueFilmRealite;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ControleurVisualiserFilmRealite implements EventHandler<ActionEvent> {
    private ModeleJeu modeleJeu;

    public ControleurVisualiserFilmRealite(ModeleJeu modele) {
        this.modeleJeu = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

        VueFilmRealite vueFilmRealite = new VueFilmRealite(modeleJeu);
        ControleurFilmRealite controleurFilmRealite = new ControleurFilmRealite(modeleJeu);
        ControleurQuitterRegleFilm controleurQuitterRegleFilm = new ControleurQuitterRegleFilm(modeleJeu);
        vueFilmRealite.retour.setOnAction(controleurQuitterRegleFilm);
        vueFilmRealite.slider.valueProperty().addListener(controleurFilmRealite);
        this.modeleJeu.getModeleFilms().enregistrerObservateur(vueFilmRealite);

        BorderPane bp = new BorderPane(vueFilmRealite);

        Scene scene = new Scene(bp, stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
        stage.show();

        modeleJeu.notifierObservateurs();
    }
}
