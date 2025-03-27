package Kronologic.MVC.Controleur.Accueil;

import Kronologic.MVC.InitialisationJeu;
import Kronologic.MVC.Modele.ModeleAccueil;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ControleurIAAccueil implements EventHandler<ActionEvent> {

    private final ModeleAccueil modele;

    public ControleurIAAccueil(ModeleAccueil modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        System.out.println("3");
        InitialisationJeu init = new InitialisationJeu((Stage)((Button)actionEvent.getSource()).getScene().getWindow());
        init.initialiserAvecIA();
    }
}
