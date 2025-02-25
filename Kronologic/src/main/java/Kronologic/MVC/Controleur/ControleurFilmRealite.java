package Kronologic.MVC.Controleur;

import Kronologic.MVC.Modele.ModeleJeu;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class ControleurFilmRealite implements ChangeListener<Number> {
    private ModeleJeu modeleJeu;

    public ControleurFilmRealite(ModeleJeu modeleJeu) {
        this.modeleJeu = modeleJeu;
    }

    @Override
    public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
        try {
            modeleJeu.getModeleFilms().actualiserFilmRealite(modeleJeu);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'actualisation du film de réalité : " + e.getMessage());
        }
    }
}
