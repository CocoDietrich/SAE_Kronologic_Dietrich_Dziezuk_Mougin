package Kronologic.MVC.Controleur;

import Kronologic.MVC.Modele.ModeleJeu;
import Kronologic.MVC.Vue.Observateur;
import Kronologic.MVC.Vue.VuePopUpQuitter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ControleurPopUpQuitter implements EventHandler<ActionEvent> {

    private ModeleJeu modele;

    public ControleurPopUpQuitter(ModeleJeu modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

        // On récupère l'id du bouton cliqué
        if (((Button) actionEvent.getSource()).getId().equals("annuler")) {
            stage.close();
        } else if (((Button) actionEvent.getSource()).getId().equals("valider")) {
            // On récupère le stage global depuis l'observateur VuePopUpQuitter
            VuePopUpQuitter vuePopUpQuitter = null;

            for (Observateur obs : this.modele.getObservateurs()) {
                if (obs instanceof VuePopUpQuitter) {
                    vuePopUpQuitter = (VuePopUpQuitter) obs;
                    break;
                }
            }

            Stage stageGlobal = vuePopUpQuitter.stageGlobal;
            this.modele.quitter("retour", stageGlobal);
            stage.close();
        }
    }
}
