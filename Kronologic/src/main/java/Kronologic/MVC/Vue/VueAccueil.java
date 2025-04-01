package Kronologic.MVC.Vue;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class VueAccueil extends BorderPane implements Observateur {

    private static final String TITRE = "Kronologic";
    private static final String JOUER = "Jouer";
    private static final String IAJOUER = "IAJoueuse";
    private static final String QUITTER = "Quitter";

    private static final String BOUTON_STYLE_NORMAL = "-fx-background-color: #FFCC66; -fx-text-fill: #800000; -fx-background-radius: 20px;";
    private static final String BOUTON_STYLE_HOVER = "-fx-background-color: #E6B85C; -fx-text-fill: #800000; -fx-background-radius: 20px; -fx-cursor: hand;";
    private static final String BAKCGROUND_TRANSPARENT = "-fx-background-color: transparent;";
    private static final String COULEUR_FOND = "#800000";
    private static final String COULEUR_TITRE = "#FFCC66";
    private static final String POLICE_ECRITURE = "Arial";

    private final Button jouer;
    private final Button IAJoueuse;
    private final Button quitter;

    public VueAccueil() {
        super();
        this.setStyle("-fx-background-color: " + COULEUR_FOND + ";");

        Text titre = new Text(TITRE);
        titre.setFont(Font.font(POLICE_ECRITURE, 60));
        titre.setFill(Color.web(COULEUR_TITRE));

        VBox titreBox = new VBox(titre);
        titreBox.setAlignment(Pos.CENTER);
        titreBox.paddingProperty().bind(Bindings.createObjectBinding(
                () -> new Insets(getHeight() * 0.05, 0, 0, 0),
                heightProperty()
        ));
        this.setTop(titreBox);

        jouer = creerBouton(JOUER);
        IAJoueuse = creerBouton(IAJOUER);
        quitter = creerBouton(QUITTER);

        VBox boutonsBox = new VBox(20);
        boutonsBox.setAlignment(Pos.CENTER);
        boutonsBox.paddingProperty().bind(Bindings.createObjectBinding(
                () -> new Insets(getHeight() * 0.02),
                heightProperty()
        ));

        boutonsBox.spacingProperty().bind(Bindings.createDoubleBinding(
                () -> getHeight() * 0.03,
                heightProperty()
        ));

        boutonsBox.getChildren().addAll(jouer, IAJoueuse, quitter);
        this.setCenter(boutonsBox);
    }

    public static Button creerBouton(String s) {
        Button bouton = new Button(s);
        bouton.setId(s);
        bouton.setFont(Font.font(POLICE_ECRITURE, 18));
        bouton.setStyle(BOUTON_STYLE_NORMAL);

        // Effets au survol de la souris
        bouton.setOnMouseEntered(_ -> bouton.setStyle(BOUTON_STYLE_HOVER));
        bouton.setOnMouseExited(_ -> bouton.setStyle(BOUTON_STYLE_NORMAL));

        bouton.setMinWidth(Button.USE_COMPUTED_SIZE);
        bouton.setPrefWidth(Button.USE_COMPUTED_SIZE);
        bouton.setMaxWidth(Button.USE_COMPUTED_SIZE);

        bouton.setMinHeight(Button.USE_COMPUTED_SIZE);
        bouton.setPrefHeight(Button.USE_COMPUTED_SIZE);
        bouton.setMaxHeight(Button.USE_COMPUTED_SIZE);

        return bouton;
    }


    public static Button creerBoutonAvecImage(String imagePath) {
        Image image = new Image(imagePath);
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.fitHeightProperty().bind(Bindings.createDoubleBinding(
                () -> imageView.getScene() == null ? 60 : imageView.getScene().getHeight() * 0.1,
                imageView.sceneProperty()
        ));

        Button bouton = creerBouton("");
        String idBouton = imagePath.substring(imagePath.lastIndexOf("/") + 1, imagePath.lastIndexOf("."));
        bouton.setId(idBouton);
        bouton.setGraphic(imageView);

        bouton.setStyle(BAKCGROUND_TRANSPARENT);
        bouton.setOnMouseEntered(_ -> {
            bouton.setStyle(BAKCGROUND_TRANSPARENT  + "-fx-cursor: hand;");
            bouton.setScaleX(1.1);
            bouton.setScaleY(1.1);
        });
        bouton.setOnMouseExited(_ -> {
            bouton.setStyle(BAKCGROUND_TRANSPARENT);
            bouton.setScaleX(1);
            bouton.setScaleY(1);
        });

        bouton.prefWidthProperty().bind(Bindings.createDoubleBinding(
                () -> bouton.getScene() == null ? 80 : bouton.getScene().getWidth() * 0.1,
                bouton.sceneProperty()
        ));

        bouton.prefHeightProperty().bind(Bindings.createDoubleBinding(
                () -> bouton.getScene() == null ? 80 : bouton.getScene().getHeight() * 0.1,
                bouton.sceneProperty()
        ));

        return bouton;
    }

    @Override
    public void actualiser() {}

    public Button getJouer() {
        return jouer;
    }

    public Button getIAJoueuse() {
        return IAJoueuse;
    }

    public Button getQuitter() {
        return quitter;
    }
}
