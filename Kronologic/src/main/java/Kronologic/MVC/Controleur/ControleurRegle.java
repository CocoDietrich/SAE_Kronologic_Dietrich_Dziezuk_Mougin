package Kronologic.MVC.Controleur;

import Kronologic.MVC.Modele.ModeleJeu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ControleurRegle implements EventHandler<ActionEvent> {

    private ModeleJeu modele;

    public ControleurRegle(ModeleJeu modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        System.out.println("ALLO");
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        if(this.modele.isVueCarte()){
            this.modele.retourVueCarte(stage);
        } else {
            this.modele.retourVueTableau(stage);
        }
    }
}
