package Kronologic.MVC.Vue;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class VuePopUpQuitter implements Observateur {

    private final Stage stage;
    public Stage stageGlobal;
    public Button annuler;
    public Button valider;

    public VuePopUpQuitter(Stage stageGlobal) {
        this.stage = new Stage();
        this.stageGlobal = stageGlobal;
        this.annuler = new Button("Annuler");
        this.valider = new Button("Valider");
    }

    public void afficherPopUp(Stage parentStage) {
        stage.setTitle("Quitter la Partie");

        // Texte principal
        Text message = new Text("Êtes-vous sûr de vouloir quitter la partie en cours ?\n(Cette dernière ne sera pas sauvegardée).");
        message.setStyle("-fx-font-size: 18px; -fx-fill: #7b001e; -fx-font-weight: bold;");

        // Boutons
        annuler.setId("annuler");
        annuler.setStyle("-fx-background-color: #7b001e; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        valider.setId("valider");
        valider.setStyle("-fx-background-color: #7b001e; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        // Layout
        HBox boutonsBox = new HBox(20, annuler, valider);
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
