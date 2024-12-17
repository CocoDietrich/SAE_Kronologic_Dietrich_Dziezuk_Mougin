package Kronologic.MVC.Vue;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class VueDeductionIA extends BorderPane implements Observateur {

    private TextArea affichage;

    public VueDeductionIA() {
        super();

        // Titre
        Text titre = new Text("Déductions de l'IA");
        titre.setFont(Font.font("Arial", 24));

        // Zone d'affichage
        affichage = new TextArea();
        affichage.setEditable(false);  // Lecture seule
        affichage.setWrapText(true);   // Ajuster le texte

        VBox centre = new VBox(20, titre, affichage);
        centre.setAlignment(Pos.CENTER);

        this.setCenter(centre);
    }

    // Méthode pour afficher les déductions
    public void afficherDeduction(String deductions) {
        affichage.setText(deductions);
    }

    @Override
    public void actualiser() {

    }
}
