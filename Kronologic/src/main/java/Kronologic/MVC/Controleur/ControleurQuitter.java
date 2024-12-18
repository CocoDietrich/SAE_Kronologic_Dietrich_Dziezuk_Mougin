package Kronologic.MVC.Controleur;

import Kronologic.MVC.Modele.ModeleJeu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ControleurQuitter implements EventHandler<ActionEvent> {

    private ModeleJeu modele;

    public ControleurQuitter(ModeleJeu modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        String id = ((Button)actionEvent.getSource()).getId();
        if (id.equals("retour")) {
            this.modele.afficherPopUpQuitter();
        }
    }
}
