package Kronologic.MVC.Vue;

import Kronologic.Jeu.Indice.Indice;
import Kronologic.Jeu.Indice.IndiceTemps;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class VuePopUpPoseQuestion implements Observateur {

    private final Stage stage;
    public Stage stageGlobal;
    public Button continuer;

    public VuePopUpPoseQuestion(Stage stageGlobal) {
        this.stage = new Stage();
        this.stageGlobal = stageGlobal;
        this.continuer = new Button("Continuer");
    }

    public void afficherPopUp(Indice i) {
        stage.setTitle("Réponse à la question");

        // Texte principal
        Text message = new Text(i.toString());
        message.setStyle("-fx-font-size: 18px; -fx-fill: #7b001e; -fx-font-weight: bold;");

        // Boutons
        continuer.setId("continuer");
        continuer.setStyle("-fx-background-color: #7b001e; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        // Layout
        HBox boutonsBox = new HBox(20, continuer);
        boutonsBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(20, message, boutonsBox);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f5a623;");

        Scene scene = new Scene(layout, 600, 300);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void actualiser() {

    }
}
