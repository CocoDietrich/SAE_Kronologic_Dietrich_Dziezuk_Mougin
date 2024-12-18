package Kronologic.MVC.Controleur;

import Kronologic.MVC.Modele.ModeleJeu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

public class ControleurDemanderIndice implements EventHandler<ActionEvent> {
    private ModeleJeu modele;

    public ControleurDemanderIndice(ModeleJeu modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        String id = ((javafx.scene.control.Button) actionEvent.getSource()).getId();
        if (id.equals("Demander un indice")) {
            this.modele.afficherPopUpDemanderIndice();
        }
    }
}
