package Kronologic.MVC.Controleur.PopUps;

import Kronologic.MVC.Modele.ModeleJeu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ControleurPopUpPoseQuestion implements EventHandler<ActionEvent> {

    private ModeleJeu modele;

    public ControleurPopUpPoseQuestion(ModeleJeu modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

        // On récupère l'id du bouton cliqué
        if (((Button) actionEvent.getSource()).getId().equals("continuer")) {
            stage.close();
        }
    }
}
