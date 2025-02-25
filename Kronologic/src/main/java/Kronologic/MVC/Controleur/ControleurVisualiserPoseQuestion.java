package Kronologic.MVC.Controleur;

import Kronologic.MVC.Modele.ModeleJeu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ControleurVisualiserPoseQuestion implements EventHandler<ActionEvent> {

    private ModeleJeu modele;

    public ControleurVisualiserPoseQuestion(ModeleJeu modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        try {
            this.modele.visualiserPoseQuestion(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
