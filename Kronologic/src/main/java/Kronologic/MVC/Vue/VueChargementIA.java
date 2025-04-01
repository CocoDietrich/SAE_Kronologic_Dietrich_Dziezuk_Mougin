package Kronologic.MVC.Vue;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VueChargementIA implements Observateur {

    private final static int LARGEUR = 500;
    private final static int HAUTEUR = 200;
    private final static String STYLE_BOUTON = "-fx-background-color: #7b001e; -fx-text-fill: white; " +
            "-fx-font-size: 16px; -fx-font-weight: bold; -fx-border-radius: 8px;";
    private final static String VOIR_FILM = "Voir le film";

    private final Stage stage;
    private final Button voirFilm;

    public VueChargementIA() {
        this.stage = new Stage();

        // Bouton pour visualiser le film
        this.voirFilm = new Button(VOIR_FILM);
        this.voirFilm.setStyle(STYLE_BOUTON);
        this.voirFilm.setAlignment(Pos.CENTER);

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

        // Zone de texte pour afficher l'historique
        TextArea zoneTexte = new TextArea(historique);
        zoneTexte.setWrapText(true);
        zoneTexte.setEditable(false);
        zoneTexte.setPrefSize(500, 400);
        zoneTexte.setStyle("-fx-font-size: 14px; -fx-control-inner-background: #fffbe6;");

        VBox layout = new VBox(15, zoneTexte, this.voirFilm);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f5a623;");

        Scene scene = new Scene(layout);
        popup.setScene(scene);
        popup.show();
    }

    public Button getVoirFilm() {return this.voirFilm;}

    @Override
    public void actualiser() {

    }
}
