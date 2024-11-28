package Kronologic.MVC.Controleur;

import Kronologic.MVC.Modele.ModeleJeu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ControleurVisualiserDeduction implements EventHandler<ActionEvent> {

    private ModeleJeu modele;

    public ControleurVisualiserDeduction(ModeleJeu modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        // TODO
    }
}
