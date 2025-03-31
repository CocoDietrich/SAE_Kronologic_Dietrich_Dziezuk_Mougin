package Kronologic.MVC.Vue.PopUps;

import Kronologic.Jeu.Enquete;
import Kronologic.MVC.Vue.Observateur;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class VuePopUpEnigme extends BorderPane implements Observateur {

        private final static String VALIDER = "Valider";

        private final static int LARGEUR = 500;
        private final static int INSETS = 20;

        private final Stage stage;
        private final Button valider;

        public VuePopUpEnigme() {
                this.stage = new Stage();
                this.valider = new Button(VALIDER);
        }

        public void afficherPopUp(Enquete enquete) {
                stage.setTitle(enquete.getNomEnquete());

                // Synopsis de l'enquête
                Text synopsis = new Text(enquete.getSynopsis());
                synopsis.setStyle("-fx-font-size: 18px; -fx-fill: #7b001e;");
                synopsis.setTextAlignment(TextAlignment.CENTER);
                synopsis.setWrappingWidth(LARGEUR); // Largeur maximale pour le texte multi-lignes

                // Enigme
                Text enigme = new Text(enquete.getEnigme());
                enigme.setStyle("-fx-font-size: 18px; -fx-fill: #7b001e; -fx-font-weight: bold;");
                enigme.setTextAlignment(TextAlignment.CENTER);
                enigme.setWrappingWidth(LARGEUR); // Largeur maximale pour le texte multi-lignes

                // Bouton
                valider.setId(VALIDER.toLowerCase());
                valider.setStyle("-fx-background-color: #7b001e; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
                valider.setOnAction(_ -> stage.close());

                // Layout pour le bouton
                HBox boutonsBox = new HBox(INSETS, valider);
                boutonsBox.setAlignment(Pos.CENTER);

                // Layout principal
                VBox layout = new VBox(INSETS, synopsis, enigme, boutonsBox);
                layout.setAlignment(Pos.TOP_CENTER); // Centrer en haut
                layout.setPadding(new Insets(INSETS));
                layout.setStyle("-fx-background-color: #f5a623;");

                // Scène adaptative
                Scene scene = new Scene(layout);
                stage.setScene(scene);

                // Adapter la taille de la fenêtre en fonction du contenu
                stage.sizeToScene();
                stage.show();
        }

        @Override
        public void actualiser() {}
}
