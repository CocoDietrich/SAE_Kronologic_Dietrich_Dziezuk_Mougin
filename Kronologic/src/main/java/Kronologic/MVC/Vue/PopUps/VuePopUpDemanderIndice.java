package Kronologic.MVC.Vue.PopUps;

import Kronologic.MVC.Vue.Observateur;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class VuePopUpDemanderIndice implements Observateur {

    private final Stage stage;
    public Stage stageGlobal;
    public Button annuler;
    public Button valider;
    public Button boutonChoco;
    public Button boutonHeuristique;
    public Button boutonTriche;
    public Button boutonSansTriche;

    public VuePopUpDemanderIndice(Stage stageGlobal) {
        this.stage = new Stage();
        this.stageGlobal = stageGlobal;
        this.annuler = new Button("Annuler");
        this.valider = new Button("Valider");
    }

    public void afficherPopUp() {
        stage.setTitle("Quitter la Partie");

        // Texte principal
        Text message = new Text("Êtes-vous sûr de vouloir demander un indice ?");
        message.setStyle("-fx-font-size: 18px; -fx-fill: #7b001e; -fx-font-weight: bold;");

        // Boutons
        annuler.setId("annuler");
        annuler.setStyle("-fx-background-color: #7b001e; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        valider.setId("valider");
        valider.setStyle("-fx-background-color: #7b001e; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        // Boutons choix
        boutonChoco = new Button("Choco");
        boutonHeuristique = new Button("Heuristique");
        boutonTriche = new Button("Triche");
        boutonSansTriche = new Button("Sans triche");

        // Style + ID
        boutonChoco.setId("choixChoco");
        boutonHeuristique.setId("choixHeuristique");
        boutonTriche.setId("choixTriche");
        boutonSansTriche.setId("choixSansTriche");

        String styleBouton = "-fx-background-color: #7b001e; -fx-text-fill: white; " +
                "-fx-font-size: 16px; -fx-font-weight: bold; -fx-border-radius: 8px;";

        boutonChoco.setStyle(styleBouton);
        boutonHeuristique.setStyle(styleBouton);
        boutonTriche.setStyle(styleBouton);
        boutonSansTriche.setStyle(styleBouton);


        HBox choixBox = new HBox(20, boutonChoco, boutonHeuristique);
        choixBox.setAlignment(Pos.CENTER);
        HBox choixTricheBox = new HBox(20, boutonTriche, boutonSansTriche);
        choixTricheBox.setAlignment(Pos.CENTER);

        // Layout
        HBox boutonsBox = new HBox(20, annuler, valider);
        boutonsBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(20, message, choixBox, choixTricheBox, boutonsBox);
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
