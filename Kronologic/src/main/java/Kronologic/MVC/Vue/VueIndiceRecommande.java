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

    private final static String TITRE = "Indice et Correction";
    private final static String FERMER = "Fermer";

    private final static int LARGEUR = 600;
    private final static int HAUTEUR = 400;

    private final static String FONT = "Arial";

    private final Stage stage;

    public VueIndiceRecommande(String indice, String erreurs) {
        this.stage = new Stage();
        this.stage.setTitle(TITRE);

        Text titre = creerTitre();
        Text messageIndice = creerMessageIndice(indice);
        TextArea zoneErreurs = creerChampErreur(erreurs);

        VBox encadreMessage = creerContenaireMessage(messageIndice);
        VBox encadreErreurs = creerContenaireErreur(zoneErreurs);

        Button fermer = creerBoutonQuitter();

        VBox layout = creerStructure(titre, encadreMessage, encadreErreurs, fermer);
        Scene scene = new Scene(layout, LARGEUR, HAUTEUR);
        stage.setScene(scene);
    }

    private Text creerTitre() {
        Text title = new Text("Indice Recommandé");
        title.setFont(Font.font(FONT, 24));
        title.setFill(Color.GOLD);
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.BLACK);
        shadow.setOffsetX(3);
        shadow.setOffsetY(3);
        title.setEffect(shadow);
        return title;
    }

    private Text creerMessageIndice(String indice) {
        Text message = new Text(indice);
        message.setFont(Font.font(FONT, 18));
        message.setFill(Color.BLACK);
        message.setWrappingWidth(450);
        return message;
    }

    private TextArea creerChampErreur(String erreurs) {
        TextArea textArea = new TextArea(erreurs);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setStyle("-fx-control-inner-background: #FFFDD0; -fx-font-family:" +
                " 'Courier New'; -fx-font-size: 14px; -fx-border-color: #A52A2A; -fx-border-width: 2px;");
        textArea.setPrefHeight(150);
        return textArea;
    }

    private VBox creerContenaireMessage(Text message) {
        VBox container = new VBox(message);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: white; -fx-border-color: #B22222;" +
                " -fx-border-width: 3px; -fx-border-radius: 10px;");
        return container;
    }

    private VBox creerContenaireErreur(TextArea textArea) {
        VBox container = new VBox(new Text("🔍 Vérification de vos hypothèses :"), textArea);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(10));
        return container;
    }

    private Button creerBoutonQuitter() {
        Button button = new Button(FERMER);
        button.setStyle("-fx-background-color: #7b001e; -fx-text-fill: white; -fx-font-size: 16px;" +
                " -fx-font-weight: bold; -fx-border-radius: 8px;");
        button.setOnMouseEntered(_ -> button.setStyle("-fx-background-color: #a5001e;" +
                " -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-border-radius: 8px;"));
        button.setOnMouseExited(_ -> button.setStyle("-fx-background-color: #7b001e;" +
                " -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-border-radius: 8px;"));
        button.setOnAction(_ -> stage.close());
        return button;
    }

    private VBox creerStructure(Text titre, VBox encadreMessage, VBox encadreErreurs, Button fermer) {
        VBox layout = new VBox(20, titre, encadreMessage, encadreErreurs, fermer);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(25));
        layout.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.LIGHTYELLOW),
                        new Stop(1, Color.ORANGERED)),
                CornerRadii.EMPTY, Insets.EMPTY
        )));
        return layout;
    }

    public void afficher() {
        stage.show();
    }
}