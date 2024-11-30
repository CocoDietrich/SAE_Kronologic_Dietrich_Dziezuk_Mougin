package Kronologic.MVC.Controleur.Accueil;

import Kronologic.MVC.Modele.ModeleAccueil;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ControleurInitialisation implements EventHandler<ActionEvent> {

    private ModeleAccueil modele;

    public ControleurInitialisation(ModeleAccueil modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        // TODO : à modifier pour récupérer le titre du bouton
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        this.modele.initialiserPartie(((Button)actionEvent.getSource()).getText(), stage);
    }
}
