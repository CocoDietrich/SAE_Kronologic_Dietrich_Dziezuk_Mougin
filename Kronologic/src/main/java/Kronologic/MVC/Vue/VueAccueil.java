package Kronologic.MVC.Vue;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class VueAccueil extends BorderPane implements Observateur {

    public Button jouer;
    public Button IAJoueuse;
    public Button quitter;

    public VueAccueil() {
        super();
        this.setStyle("-fx-background-color: #800000;");

        Text titre = new Text("Kronologic");
        titre.setFont(Font.font("Arial", 60));
        titre.setFill(Color.web("#FFCC66"));

        VBox titreBox = new VBox(titre);
        titreBox.setAlignment(Pos.CENTER);
        titreBox.setPadding(new Insets(50, 0, 0, 0));
        this.setTop(titreBox);


        jouer = creerButton("Jouer");
        IAJoueuse = creerButton("IAJoueuse");
        quitter = creerButton("Quitter");

        VBox boutonsBox = new VBox(20);
        boutonsBox.setAlignment(Pos.CENTER);
        boutonsBox.setPadding(new Insets(20));
        boutonsBox.getChildren().addAll(jouer, IAJoueuse, quitter);

        this.setCenter(boutonsBox);
    }


    public static Button creerButton(String s){
        Button bouton = new Button(s);
        bouton.setId(s);
        bouton.setFont(Font.font("Arial", 18));
        bouton.setStyle(
                "-fx-background-color: #FFCC66; " +
                        "-fx-text-fill: #800000; " +
                        "-fx-background-radius: 20px;"
        );

        // Effets au survol de la souris
        bouton.setOnMouseEntered(e -> {
            bouton.setStyle(
                    "-fx-background-color: #E6B85C; " +
                            "-fx-text-fill: #800000; " +
                            "-fx-background-radius: 20px; "
            );
            bouton.setScaleX(1.1);
            bouton.setScaleY(1.1);
        });
        bouton.setOnMouseExited(e -> {
            bouton.setStyle(
                    "-fx-background-color: #FFCC66; " +
                            "-fx-text-fill: #800000; " +
                            "-fx-background-radius: 20px;"
            );
            bouton.setScaleX(1);
            bouton.setScaleY(1);
        });
        return bouton;
    }

    @Override
    public void actualiser() {
        // TODO
    }
}
