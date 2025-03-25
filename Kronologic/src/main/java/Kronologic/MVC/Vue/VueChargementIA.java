package Kronologic.MVC.Vue;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class VueChargementIA {

    private final Stage stage;
    private final Label message;
    private final ProgressBar barreChargement;

    public VueChargementIA() {
        this.stage = new Stage();

        message = new Label("L’IA tente de résoudre l’enquête...");
        message.setFont(Font.font("Arial", 20));

        barreChargement = new ProgressBar();
        barreChargement.setPrefWidth(400);
        barreChargement.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        barreChargement.setStyle("-fx-accent: #7b001e;");

        VBox layout = new VBox(30, message, barreChargement);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #fff3e0;");
        layout.setPrefSize(500, 200);

        Scene scene = new Scene(layout);
        stage.setScene(scene);
        stage.setTitle("IA Joueuse en cours...");
    }

    public void afficher() {
        stage.show();
    }

    public void afficherResultat(String texte) {
        message.setText(texte);
        barreChargement.setProgress(1.0);
    }
}
