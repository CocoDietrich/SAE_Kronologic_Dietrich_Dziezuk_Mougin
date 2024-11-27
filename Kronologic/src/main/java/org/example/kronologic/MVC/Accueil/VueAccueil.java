package org.example.kronologic.MVC.Accueil;

import org.example.kronologic.MVC.Observateur;
import org.example.kronologic.MVC.Vue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.awt.*;

public class VueAccueil extends Vue implements Observateur {

    private Button boutonJouer;
    private Button boutonIAJoueuse;
    private Button boutonQuitter;

    @Override
    public void afficher(Stage stage) {

        // Titre
        Text titre = new Text("Kronologic");

        // Boutons
        boutonJouer = new Button("Jouer");
        boutonIAJoueuse = new Button("IA Joueuse");
        boutonQuitter = new Button("Quitter");

        // Conteneur Principal
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #800000;");
        layout.getChildren().add(titre);

        // Configuration de la scène et de la fenêtre
        Scene scene = new Scene(layout, 400, 400);
        stage.setTitle("Kronologic");
        stage.setScene(scene);
        stage.show();
    }

    private Button creerBouton(String texte) {
        Button bouton = new Button(texte);
        bouton.setStyle("-fx-background-color: #FFCC66; -fx-text-fill: #800000;");
        return bouton;
    }

    @Override
    public void actualiser() {
        // TODO : Mettre à jour la vue
    }


}
