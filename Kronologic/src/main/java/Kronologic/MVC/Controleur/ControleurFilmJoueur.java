package Kronologic.MVC.Controleur;

import Kronologic.MVC.Modele.ModeleJeu;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class ControleurFilmJoueur implements ChangeListener<Number> {
    private ModeleJeu modele;

    public ControleurFilmJoueur(ModeleJeu modele) {
        this.modele = modele;
    }

    @Override
    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {

    }
}