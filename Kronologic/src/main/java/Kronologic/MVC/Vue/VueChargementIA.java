package Kronologic.MVC.Vue;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VueChargementIA {

    private final static int LARGEUR = 500;
    private final static int HAUTEUR = 200;

    private final Stage stage;

    public VueChargementIA() {
        this.stage = new Stage();

        Label message = new Label("🤖 L’IA tente de résoudre l’enquête...");
        message.setFont(Font.font("Arial", 20));
        message.setTextFill(Color.web("#7b001e")); // Rouge sombre

        ProgressBar barreChargement = new ProgressBar();
        barreChargement.setPrefWidth(400);
        barreChargement.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        barreChargement.setStyle("-fx-accent: #ff9800;"); // Orange dynamique

        VBox layout = new VBox(30, message, barreChargement);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #fff3e0; -fx-padding: 20;");
        layout.setPrefSize(LARGEUR, HAUTEUR);

        Scene scene = new Scene(layout);
        stage.setScene(scene);
        stage.setTitle("IA Joueuse en cours...");
    }

    public void afficher() {
        stage.show();
    }

    public void fermer() {
        stage.close();
    }

    public void afficherHistoriqueIA(String historique) {
        Stage popup = new Stage();
        popup.initOwner(stage);
        popup.initModality(Modality.WINDOW_MODAL);
        popup.setTitle("🧠 Historique des questions IA");

        TextArea zoneTexte = new TextArea(historique);
        zoneTexte.setWrapText(true);
        zoneTexte.setEditable(false);
        zoneTexte.setPrefSize(500, 400);
        zoneTexte.setStyle("-fx-font-size: 14px; -fx-control-inner-background: #fffbe6;");

        VBox layout = new VBox(15, zoneTexte);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f5a623;");

        Scene scene = new Scene(layout);
        popup.setScene(scene);
        popup.show();
    }
}
