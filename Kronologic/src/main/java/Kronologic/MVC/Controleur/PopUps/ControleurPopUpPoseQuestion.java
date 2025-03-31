package Kronologic.MVC.Controleur.PopUps;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ControleurPopUpPoseQuestion implements EventHandler<ActionEvent> {

    private final static String CONTINUER = "continuer";

    @Override
    public void handle(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if (!(source instanceof Button button)) {
            return;  // Sécurité : on ignore si ce n'est pas un bouton
        }

        Stage stage = (Stage) button.getScene().getWindow();
        if (CONTINUER.equals(button.getId())) {  // Évite un NullPointerException
            stage.close();
        }
    }
}
