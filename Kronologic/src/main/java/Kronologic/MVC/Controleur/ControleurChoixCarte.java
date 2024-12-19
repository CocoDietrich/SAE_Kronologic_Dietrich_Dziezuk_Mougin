package Kronologic.MVC.Controleur;

import Kronologic.MVC.Modele.ModeleJeu;
import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;

public class ControleurChoixCarte implements EventHandler<DragEvent> {

    private ModeleJeu modeleJeu;

    public ControleurChoixCarte(ModeleJeu modeleJeu) {
        this.modeleJeu = modeleJeu;
    }

    @Override
    public void handle(DragEvent dragEvent) {
        System.out.println("salut mon pote");
    }
}
