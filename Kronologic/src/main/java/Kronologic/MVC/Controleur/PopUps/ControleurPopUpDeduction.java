package Kronologic.MVC.Controleur.PopUps;

import Kronologic.MVC.Modele.ModeleJeu;
import Kronologic.MVC.Vue.Observateur;
import Kronologic.MVC.Vue.PopUps.VuePopUpDeduction;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ControleurPopUpDeduction implements EventHandler<ActionEvent> {

    private ModeleJeu modele;

    public ControleurPopUpDeduction(ModeleJeu modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

        // On récupère l'id du bouton
        String texte = ((Button) actionEvent.getSource()).getText();

        // Si on clique sur le bouton de retour, on ferme le stage du pop-up
        if (texte.equals("Quitter")) {
            stage.close();
            System.out.println("Fermeture du pop-up de déduction.");

            VuePopUpDeduction vuePopUpDeduction = null;
            // On récupère la vue du pop-up de déduction
            for (Observateur o : modele.getObservateurs()) {
                if (o instanceof VuePopUpDeduction) {
                    vuePopUpDeduction = (VuePopUpDeduction) o;
                    break;
                }
            }
            // On retourne à la vue de la carte
            assert vuePopUpDeduction != null;
            this.modele.quitter("retour", vuePopUpDeduction.getStage());
        } else if (texte.equals("Voir le film")) {
            this.modele.visualiserFilmRealite(stage);
        }
    }
}
