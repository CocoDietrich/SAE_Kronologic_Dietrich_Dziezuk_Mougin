package Kronologic.MVC.Controleur.PopUps;

import Kronologic.MVC.Modele.ModeleJeu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ControleurPopUpPoseQuestion implements EventHandler<ActionEvent> {

    private final ModeleJeu modele;

    public ControleurPopUpPoseQuestion(ModeleJeu modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if (!(source instanceof Button button)) {
            return;  // Sécurité : on ignore si ce n'est pas un bouton
        }

        Stage stage = (Stage) ((Node) button).getScene().getWindow();
        if ("continuer".equals(button.getId())) {  // Évite un NullPointerException
            stage.close();
        }
    }
}
