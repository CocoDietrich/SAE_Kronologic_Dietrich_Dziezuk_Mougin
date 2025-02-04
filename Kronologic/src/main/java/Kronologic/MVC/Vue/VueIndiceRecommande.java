package Kronologic.MVC.Vue;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;

public class VueIndiceRecommande {

    private final Stage stage;
    private final Text messageIndice;
    private final TextArea zoneErreurs;

    public VueIndiceRecommande(String indice, String erreurs) {
        this.stage = new Stage();
        this.stage.setTitle("Indice et Correction");

        // Titre de la fenêtre avec effet d'ombre et couleur dorée
        Text titre = new Text("Indice Recommandé");
        titre.setFont(Font.font("Arial", 24));
        titre.setFill(Color.GOLD);
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.BLACK);
        shadow.setOffsetX(3);
        shadow.setOffsetY(3);
        titre.setEffect(shadow);

        // Message contenant l'indice recommandé
        this.messageIndice = new Text(indice);
        messageIndice.setFont(Font.font("Arial", 18));
        messageIndice.setFill(Color.BLACK);
        messageIndice.setWrappingWidth(450);

        // Encadré pour l'indice
        VBox encadreMessage = new VBox(messageIndice);
        encadreMessage.setAlignment(Pos.CENTER);
        encadreMessage.setPadding(new Insets(20));
        encadreMessage.setStyle("-fx-background-color: white; -fx-border-color: #B22222; -fx-border-width: 3px; -fx-border-radius: 10px;");

        // Zone pour afficher les erreurs du joueur (comme l'IA Déduction)
        this.zoneErreurs = new TextArea(erreurs);
        zoneErreurs.setEditable(false);
        zoneErreurs.setWrapText(true);
        zoneErreurs.setStyle("-fx-control-inner-background: #FFFDD0; -fx-font-family: 'Courier New'; -fx-font-size: 14px; -fx-border-color: #A52A2A; -fx-border-width: 2px;");
        zoneErreurs.setPrefHeight(150);

        VBox encadreErreurs = new VBox(new Text("🔍 Vérification de vos hypothèses :"), zoneErreurs);
        encadreErreurs.setAlignment(Pos.CENTER);
        encadreErreurs.setPadding(new Insets(10));

        // Bouton de fermeture avec effet de survol
        Button fermer = new Button("Fermer");
        fermer.setStyle("-fx-background-color: #7b001e; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-border-radius: 8px;");
        fermer.setOnMouseEntered(e -> fermer.setStyle("-fx-background-color: #a5001e; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-border-radius: 8px;"));
        fermer.setOnMouseExited(e -> fermer.setStyle("-fx-background-color: #7b001e; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-border-radius: 8px;"));
        fermer.setOnAction(e -> stage.close());

        // Mise en page avec un fond dégradé
        VBox layout = new VBox(20, titre, encadreMessage, encadreErreurs, fermer);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(25));
        layout.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.LIGHTYELLOW),
                        new Stop(1, Color.ORANGERED)),
                CornerRadii.EMPTY, Insets.EMPTY
        )));

        Scene scene = new Scene(layout, 600, 400);
        stage.setScene(scene);
    }

    public void afficher() {
        stage.show();
    }
}
