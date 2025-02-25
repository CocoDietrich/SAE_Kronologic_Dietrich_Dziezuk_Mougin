package Kronologic.MVC.Controleur;

import Kronologic.MVC.Modele.ModeleJeu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ControleurAffichage implements EventHandler<ActionEvent> {

    private final ModeleJeu modele;

    public ControleurAffichage(ModeleJeu modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if (!(source instanceof Button button)) {
            return;
        }

        Stage stage = (Stage) button.getScene().getWindow();
        if (stage != null) {
            modele.changerAffichage(stage);
        }
    }
}
