package Kronologic.MVC.Controleur;

import Kronologic.MVC.Modele.ModeleJeu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ControleurVisualiserPoseQuestion implements EventHandler<ActionEvent> {

    private ModeleJeu modele;

    public ControleurVisualiserPoseQuestion(ModeleJeu modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        // TODO
    }
}
