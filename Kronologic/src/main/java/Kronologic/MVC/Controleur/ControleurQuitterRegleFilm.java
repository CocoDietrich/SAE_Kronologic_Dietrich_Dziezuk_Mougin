package Kronologic.MVC.Controleur;

import Kronologic.MVC.Modele.ModeleJeu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ControleurQuitterRegleFilm implements EventHandler<ActionEvent> {

    private final ModeleJeu modele;

    public ControleurQuitterRegleFilm(ModeleJeu modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        if(this.modele.estVueCarte()){
            this.modele.retourVueCarte(stage);
        } else {
            this.modele.retourVueTableau(stage);
        }
    }
}
