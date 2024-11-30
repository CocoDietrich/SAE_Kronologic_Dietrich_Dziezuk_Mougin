package Kronologic.MVC.Controleur.Accueil;

import Kronologic.MVC.Modele.ModeleAccueil;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

public class ControleurQuitterJeu implements EventHandler<ActionEvent> {

    private ModeleAccueil modele;

    public ControleurQuitterJeu(ModeleAccueil modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        this.modele.quitterJeu();
    }
}
