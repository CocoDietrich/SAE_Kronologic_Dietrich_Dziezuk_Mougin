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

public class VuePopUpDemanderIndice implements Observateur {

    private final static String ANNULER = "Annuler";
    private final static String VALIDER = "Valider";

    private final static String STYLE_BOUTON = "-fx-background-color: #7b001e; -fx-text-fill: white; " +
            "-fx-font-size: 16px; -fx-font-weight: bold; -fx-border-radius: 8px;";

    private final static int LARGEUR = 600;
    private final static int HAUTEUR = 300;
    private final static int INSETS = 20;

    private final Stage stage;
    private final Button annuler;
    private final Button valider;
    private Button boutonChoco;
    private Button boutonHeuristique;
    private Button boutonTriche;
    private Button boutonSansTriche;
    private Button boutonMax;
    private Button boutonMin;
    private Button boutonMoyenne;

    public VuePopUpDemanderIndice() {
        this.stage = new Stage();
        this.annuler = new Button(ANNULER);
        this.valider = new Button(VALIDER);
    }

    public void afficherPopUp() {
        stage.setTitle("Indice");

        // Texte principal
        Text message = new Text("Êtes-vous sûr de vouloir demander un indice ?");
        message.setStyle("-fx-font-size: 18px; -fx-fill: #7b001e; -fx-font-weight: bold;");

        // Boutons
        annuler.setId(ANNULER.toLowerCase());
        annuler.setStyle(STYLE_BOUTON);
        valider.setId(VALIDER.toLowerCase());
        valider.setStyle(STYLE_BOUTON);

        // Boutons choix
        boutonChoco = new Button("Choco");
        boutonHeuristique = new Button("Heuristique");
        boutonTriche = new Button("Triche");
        boutonSansTriche = new Button("Sans triche");
        boutonMax = new Button("Max");
        boutonMin = new Button("Min");
        boutonMoyenne = new Button("Moyenne");

        // Style + ID
        boutonChoco.setId("choixChoco");
        boutonHeuristique.setId("choixHeuristique");
        boutonTriche.setId("choixTriche");
        boutonSansTriche.setId("choixSansTriche");
        boutonMin.setId("choixMin");
        boutonMax.setId("choixMax");
        boutonMoyenne.setId("choixMoyenne");

        boutonChoco.setStyle(STYLE_BOUTON);
        boutonHeuristique.setStyle(STYLE_BOUTON);
        boutonTriche.setStyle(STYLE_BOUTON);
        boutonSansTriche.setStyle(STYLE_BOUTON);
        boutonMin.setStyle(STYLE_BOUTON);
        boutonMax.setStyle(STYLE_BOUTON);
        boutonMoyenne.setStyle(STYLE_BOUTON);

        HBox choixBox = new HBox(INSETS, boutonChoco, boutonHeuristique);
        choixBox.setAlignment(Pos.CENTER);
        HBox choixTricheBox = new HBox(INSETS, boutonTriche, boutonSansTriche);
        choixTricheBox.setAlignment(Pos.CENTER);
        HBox choixMaxMinMoyBox = new HBox(INSETS, boutonMin, boutonMax, boutonMoyenne);
        choixMaxMinMoyBox.setAlignment(Pos.CENTER);

        // Layout
        HBox boutonsBox = new HBox(INSETS, annuler, valider);
        boutonsBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(INSETS, message, choixBox, choixTricheBox, choixMaxMinMoyBox, boutonsBox);
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

    public Button getAnnuler() {
        return annuler;
    }

    public Button getValider() {
        return valider;
    }

    public Button getBoutonChoco() {
        return boutonChoco;
    }

    public Button getBoutonHeuristique() {
        return boutonHeuristique;
    }

    public Button getBoutonTriche() {
        return boutonTriche;
    }

    public Button getBoutonSansTriche() {
        return boutonSansTriche;
    }

    public Button getBoutonMax() {
        return boutonMax;
    }

    public Button getBoutonMin() {
        return boutonMin;
    }

    public Button getBoutonMoyenne() {
        return boutonMoyenne;
    }
}
