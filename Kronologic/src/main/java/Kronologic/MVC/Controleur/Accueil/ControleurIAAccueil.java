package Kronologic.MVC.Controleur.Accueil;

import Kronologic.MVC.InitialisationJeu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ControleurIAAccueil implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent actionEvent) {
        InitialisationJeu init = new InitialisationJeu((Stage)((Button)actionEvent.getSource()).getScene().getWindow());
        init.initialiserAvecIA();
    }
}