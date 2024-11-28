package Kronologic.MVC.Controleur;

import Kronologic.MVC.Modele.ModeleJeu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ControleurVoirDeductionIA implements EventHandler<ActionEvent> {

    private ModeleJeu modele;

    public ControleurVoirDeductionIA(ModeleJeu modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        this.modele.voirDeductionIA();
    }
}
