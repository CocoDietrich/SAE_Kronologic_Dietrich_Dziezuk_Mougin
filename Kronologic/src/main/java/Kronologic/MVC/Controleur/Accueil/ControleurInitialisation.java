package Kronologic.MVC.Controleur.Accueil;

import Kronologic.MVC.InitialisationJeu;
import Kronologic.MVC.Modele.ModeleAccueil;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ControleurInitialisation implements EventHandler<ActionEvent> {

    private final ModeleAccueil modele;

    public ControleurInitialisation(ModeleAccueil modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        System.out.println("1");
        this.modele.initialiserPartie(((Button)actionEvent.getSource()).getText());
        InitialisationJeu initialisation = new InitialisationJeu((Stage)((Button)actionEvent.getSource()).getScene().getWindow());
        initialisation.initialiser();
    }
}
