package Kronologic.MVC.Vue.PopUps;

import Kronologic.MVC.Vue.Observateur;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class VuePopUpQuitter implements Observateur {

    private final static String TITRE = "Quitter la Partie";
    private final static String ANNULER = "Annuler";
    private final static String VALIDER = "Valider";

    private final static int LARGEUR = 600;
    private final static int HAUTEUR = 300;
    private final static int INSETS = 20;

    private final Stage stage;
    private final Stage stageGlobal;
    private final Button annuler;
    private final Button valider;

    public VuePopUpQuitter(Stage stageGlobal) {
        this.stage = new Stage();
        this.stageGlobal = stageGlobal;
        this.annuler = new Button(ANNULER);
        this.valider = new Button(VALIDER);
    }

    public void afficherPopUp() {
        stage.setTitle(TITRE);

        // Texte principal
        Text message = new Text("Êtes-vous sûr de vouloir quitter la partie en cours ?" +
                "\n(Cette dernière ne sera pas sauvegardée).");
        message.setStyle("-fx-font-size: 18px; -fx-fill: #7b001e; -fx-font-weight: bold;");

        // Boutons
        annuler.setId(ANNULER.toLowerCase());
        annuler.setStyle("-fx-background-color: #7b001e; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        valider.setId(VALIDER.toLowerCase());
        valider.setStyle("-fx-background-color: #7b001e; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        // Layout
        HBox boutonsBox = new HBox(INSETS, annuler, valider);
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

    public Stage getStage() {
        return stage;
    }

    public Stage getStageGlobal() {
        return stageGlobal;
    }

    public Button getAnnuler() {
        return annuler;
    }

    public Button getValider() {
        return valider;
    }
}
