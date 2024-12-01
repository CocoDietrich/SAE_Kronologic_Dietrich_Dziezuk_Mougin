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
        // On passe à la vue de pose de question
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        this.modele.visualiserPoseQuestion(stage);
    }
}
