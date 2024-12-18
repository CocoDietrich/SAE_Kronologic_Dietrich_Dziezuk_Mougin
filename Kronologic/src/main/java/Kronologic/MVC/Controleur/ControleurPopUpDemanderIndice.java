package Kronologic.MVC.Controleur;

import Kronologic.MVC.Modele.ModeleJeu;
import Kronologic.MVC.Vue.Observateur;
import Kronologic.MVC.Vue.VuePopUpQuitter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ControleurPopUpDemanderIndice implements EventHandler<ActionEvent> {

    private ModeleJeu modele;

    public ControleurPopUpDemanderIndice(ModeleJeu modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

        // On récupère l'id du bouton cliqué
        if (((Button) actionEvent.getSource()).getId().equals("annuler")) {
            stage.close();
        } else if (((Button) actionEvent.getSource()).getId().equals("valider")) {
            this.modele.demanderIndice();
        }
    }
}
