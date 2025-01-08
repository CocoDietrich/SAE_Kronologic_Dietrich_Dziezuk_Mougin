package Kronologic.MVC.Controleur;

import Kronologic.MVC.Modele.ModeleJeu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ControleurVisualiserFilmJoueur implements EventHandler<ActionEvent> {
    private ModeleJeu modele;

    public ControleurVisualiserFilmJoueur(ModeleJeu modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        this.modele.visualiserFilmJoueur((Stage) ((Button) actionEvent.getSource()).getScene().getWindow());
    }
}
