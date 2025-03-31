package Kronologic.MVC.Controleur.PopUps;

import Kronologic.MVC.Controleur.ControleurFilmRealite;
import Kronologic.MVC.Modele.ModeleJeu;
import Kronologic.MVC.Vue.PopUps.VuePopUpDeduction;
import Kronologic.MVC.Vue.VueFilmRealite;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Optional;

public class ControleurPopUpDeduction implements EventHandler<ActionEvent> {

    private static final String QUITTER = "Quitter";
    private static final String VOIR_FILM = "Voir le film";
    private static final String RETOUR = "retour";

    private final ModeleJeu modele;

    public ControleurPopUpDeduction(ModeleJeu modele) {
        this.modele = modele;
    }

    private void finDeJeu(Stage stage){
        stage.close();
        Optional<VuePopUpDeduction> vuePopUpDeduction = modele.getObservateurs().stream()
                .filter(VuePopUpDeduction.class::isInstance)
                .map(VuePopUpDeduction.class::cast)
                .findFirst();

        vuePopUpDeduction.ifPresent(vue -> modele.quitter(RETOUR, vue.getStage()));
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if (!(source instanceof Button button)) {
            return;
        }

        Stage stage = (Stage) button.getScene().getWindow();
        String texte = button.getText();

        if (QUITTER.equals(texte)) {
            finDeJeu(stage);
        } else if (VOIR_FILM.equals(texte)) {
            VueFilmRealite vueFilmRealite = new VueFilmRealite(modele);
            ControleurFilmRealite controleurFilmRealite = new ControleurFilmRealite(modele);
            vueFilmRealite.retour.setOnAction(_ -> finDeJeu(stage));
            vueFilmRealite.slider.valueProperty().addListener(controleurFilmRealite);
            this.modele.getModeleFilms().enregistrerObservateur(vueFilmRealite);

            BorderPane bp = new BorderPane(vueFilmRealite);

            Scene scene = new Scene(bp);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();

            modele.notifierObservateurs();
        }
    }
}
