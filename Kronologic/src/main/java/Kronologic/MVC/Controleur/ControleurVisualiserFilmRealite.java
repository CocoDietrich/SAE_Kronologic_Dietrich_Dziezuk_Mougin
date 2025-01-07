package Kronologic.MVC.Controleur;

import Kronologic.MVC.Modele.ModeleJeu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ControleurVisualiserFilmRealite implements EventHandler<ActionEvent> {
    private ModeleJeu modele;

    public ControleurVisualiserFilmRealite(ModeleJeu modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        System.out.println("Visualiser du déroulement réel de la partie : ");
        modele.visualiserFilmRealite();
    }
}
