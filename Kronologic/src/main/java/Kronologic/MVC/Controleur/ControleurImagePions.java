package Kronologic.MVC.Controleur;

import Kronologic.MVC.Modele.ModeleJeu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ControleurImagePions implements EventHandler<ActionEvent> {

    private ModeleJeu modele;

    public ControleurImagePions(ModeleJeu modele) {
        this.modele = modele;
    }


    @Override
    public void handle(ActionEvent actionEvent) {
        this.modele.changerImagePions();
    }
}
