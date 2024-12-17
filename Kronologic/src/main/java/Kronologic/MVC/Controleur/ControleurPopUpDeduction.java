package Kronologic.MVC.Controleur;

import Kronologic.MVC.Modele.ModeleJeu;
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
        String id = ((Button) actionEvent.getSource()).getId();

        // Si on clique sur le bouton de retour, on ferme le stage du pop-up
        if (id.equals("Quitter")) {
            stage.close();
            System.out.println("Fermeture du pop-up de déduction.");
        }
    }
}
