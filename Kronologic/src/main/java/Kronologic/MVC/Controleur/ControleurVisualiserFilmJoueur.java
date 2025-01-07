package Kronologic.MVC.Controleur;

import Kronologic.MVC.Modele.ModeleJeu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ControleurVisualiserFilmJoueur implements EventHandler<ActionEvent> {
    private ModeleJeu modele;

    public ControleurVisualiserFilmJoueur(ModeleJeu modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        System.out.println("Visualiser l'historique du joueur : ");
        modele.visualiserFilmJoueur();
    }
}
