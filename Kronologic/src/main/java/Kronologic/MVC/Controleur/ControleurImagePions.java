package Kronologic.MVC.Controleur;

import Kronologic.MVC.Modele.ModeleJeu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ControleurImagePions implements EventHandler<ActionEvent> {

    private final ModeleJeu modele;

    public ControleurImagePions(ModeleJeu modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        try {
            this.modele.notifierObservateurs();
        } catch (Exception e) {
            System.err.println("Erreur lors de la notification des observateurs : " + e.getMessage());
        }
    }
}
