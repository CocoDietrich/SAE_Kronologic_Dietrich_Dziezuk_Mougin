package Kronologic.MVC.Controleur;

import Kronologic.MVC.Modele.ModeleJeu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ControleurQuitter implements EventHandler<ActionEvent> {

    private ModeleJeu modele;

    public ControleurQuitter(ModeleJeu modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        System.exit(0);
    }
}
