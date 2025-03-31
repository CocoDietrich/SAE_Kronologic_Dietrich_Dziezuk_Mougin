package Kronologic.MVC.Vue.PopUps;

import Kronologic.Jeu.Indice.Indice;
import Kronologic.MVC.Vue.Observateur;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class VuePopUpPoseQuestion implements Observateur {

    private final static String CONTINUER = "Continuer";

    private final static int LARGEUR = 600;
    private final static int HAUTEUR = 300;
    private final static int INSETS = 20;

    private final Stage stage;
    private final Button continuer;

    public VuePopUpPoseQuestion() {
        this.stage = new Stage();
        this.continuer = new Button(CONTINUER);
    }

    public void afficherPopUp(Indice i) {
        stage.setTitle("Réponse à la question");

        // Texte principal
        Text message = new Text(i.toString());
        message.setStyle("-fx-font-size: 18px; -fx-fill: #7b001e; -fx-font-weight: bold;");

        // Boutons
        continuer.setId(CONTINUER.toLowerCase());
        continuer.setStyle("-fx-background-color: #7b001e; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        // Layout
        HBox boutonsBox = new HBox(INSETS, continuer);
        boutonsBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(INSETS, message, boutonsBox);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(INSETS));
        layout.setStyle("-fx-background-color: #f5a623;");

        Scene scene = new Scene(layout, LARGEUR, HAUTEUR);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void actualiser() {}

    public Button getContinuer() {return continuer;}
}
