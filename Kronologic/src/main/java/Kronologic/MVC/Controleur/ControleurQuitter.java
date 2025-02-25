package Kronologic.MVC.Controleur;

import Kronologic.MVC.Modele.ModeleJeu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class ControleurQuitter implements EventHandler<ActionEvent> {

    private ModeleJeu modele;
    private static final String ID_RETOUR = "retour";

    public ControleurQuitter(ModeleJeu modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        String id = ((Button) actionEvent.getSource()).getId();

        if (ID_RETOUR.equals(id)) {
            this.modele.afficherPopUpQuitter();
        }
    }
}
