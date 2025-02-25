package Kronologic.MVC.Controleur;

import Kronologic.MVC.Modele.ModeleJeu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ControleurVisualiserDeduction implements EventHandler<ActionEvent> {

    private ModeleJeu modele;

    public ControleurVisualiserDeduction(ModeleJeu modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        if (actionEvent.getSource() instanceof Button) {
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            this.modele.visualiserDeduction(stage);
        }
    }
}
