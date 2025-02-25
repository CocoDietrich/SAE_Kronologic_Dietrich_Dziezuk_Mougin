package Kronologic.MVC.Vue;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.Jeu.Images;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

import static Kronologic.MVC.Vue.VueAccueil.creerBouton;
import static Kronologic.MVC.Vue.VueAccueil.creerBoutonAvecImage;

public class VueDeduction extends BorderPane implements Observateur {

    public Button valider;
    public Button retour;
    public Button annuler;
    public List<Button> lieuButtons;
    public List<Button> tempsButtons;
    public List<Button> personnageButtons;
    public Lieu lieuMeurtre;
    public Temps tempsMeurtre;
    public Personnage meurtrier;

    public VueDeduction() {
        super();
        this.setPadding(new Insets(20));
        this.setStyle("-fx-background-color: #800000;");

        // Titre
        Text titre = new Text("Effectuer votre déduction :");
        titre.setFont(Font.font("Arial", 28));
        titre.setFill(Color.WHITE);

        VBox titreBox = new VBox(titre);
        titreBox.setAlignment(Pos.CENTER);
        this.setTop(titreBox);

        // Contenu principal
        VBox contenuPrincipal = afficherContenu();

        // Boutons
        HBox boutons = afficherBoutons();

        VBox centre = new VBox(10);
        centre.setAlignment(Pos.CENTER);
        centre.getChildren().addAll(contenuPrincipal, boutons);

        this.setCenter(centre);
    }

    private VBox afficherContenu() {
        VBox contenu = new VBox(30);
        contenu.setAlignment(Pos.CENTER);

        // Section lieux
        HBox lieuxBox = creerSectionHorizontal("Choisissez le Lieu du meurtre :", creerBoutonsLieux());

        // Sections temps et personnages
        HBox tempsEtPersos = new HBox(50);
        tempsEtPersos.setAlignment(Pos.CENTER);

        VBox tempsBox = creerSectionVertical("Choisissez le Moment du meurtre :", creerBoutonsTemps());
        VBox personnagesBox = creerSectionVertical("Choisissez le Meurtrier :", creerBoutonsPersonnages());

        tempsEtPersos.getChildren().addAll(tempsBox, personnagesBox);

        contenu.getChildren().addAll(lieuxBox, tempsEtPersos);
        return contenu;
    }

    private HBox creerSectionHorizontal(String titre, HBox contenuBoutons) {
        VBox sectionBox = new VBox(10);
        sectionBox.setAlignment(Pos.CENTER);

        Text titreSection = new Text(titre);
        titreSection.setFont(Font.font("Arial", 20));
        titreSection.setFill(Color.WHITE);

        sectionBox.getChildren().addAll(titreSection, contenuBoutons);

        HBox container = new HBox(sectionBox);
        container.setAlignment(Pos.CENTER);
        return container;
    }

    private VBox creerSectionVertical(String titre, GridPane contenuBoutons) {
        VBox sectionBox = new VBox(10);
        sectionBox.setAlignment(Pos.CENTER);

        Text titreSection = new Text(titre);
        titreSection.setFont(Font.font("Arial", 20));
        titreSection.setFill(Color.WHITE);

        sectionBox.getChildren().addAll(titreSection, contenuBoutons);
        return sectionBox;
    }

    private HBox creerBoutonsLieux() {
        HBox lieuxBox = new HBox(15);
        lieuxBox.setAlignment(Pos.CENTER);

        List<String> lieuxImagesPaths = List.of(
                Images.Lieux.LIEU1.getUrl(),
                Images.Lieux.LIEU2.getUrl(),
                Images.Lieux.LIEU3.getUrl(),
                Images.Lieux.LIEU4.getUrl(),
                Images.Lieux.LIEU5.getUrl(),
                Images.Lieux.LIEU6.getUrl()
        );

        lieuButtons = new ArrayList<>();
        for (String path : lieuxImagesPaths) {
            Button lieuButton = creerBoutonAvecImage(path);
            lieuButtons.add(lieuButton);
            lieuxBox.getChildren().add(lieuButton);

            // Lier les tailles des boutons
            DoubleBinding largeurConteneur = this.widthProperty().multiply(0.15);
            lieuButton.prefWidthProperty().bind(largeurConteneur);
        }

        return lieuxBox;
    }

    private GridPane creerBoutonsTemps() {
        GridPane tempsGrid = new GridPane();
        tempsGrid.setAlignment(Pos.CENTER);
        tempsGrid.setHgap(10);
        tempsGrid.setVgap(10);

        List<String> tempsImagesPaths = List.of(
                Images.Temps.TEMPS1.getUrl(),
                Images.Temps.TEMPS2.getUrl(),
                Images.Temps.TEMPS3.getUrl(),
                Images.Temps.TEMPS4.getUrl(),
                Images.Temps.TEMPS5.getUrl(),
                Images.Temps.TEMPS6.getUrl()
        );

        tempsButtons = new ArrayList<>();
        for (int i = 0; i < tempsImagesPaths.size(); i++) {
            String path = tempsImagesPaths.get(i);
            Button tempsButton = creerBoutonAvecImage(path);
            tempsButtons.add(tempsButton);
            tempsGrid.add(tempsButton, i % 2, i / 2);

            // Lier les tailles des boutons
            DoubleBinding largeurConteneur = tempsGrid.widthProperty().multiply(0.2);
            tempsButton.prefWidthProperty().bind(largeurConteneur);
        }

        return tempsGrid;
    }

    private GridPane creerBoutonsPersonnages() {
        GridPane persosGrid = new GridPane();
        persosGrid.setAlignment(Pos.CENTER);
        persosGrid.setHgap(10);
        persosGrid.setVgap(10);

        List<String> personnagesImagesPaths = List.of(
                Images.Personnages.PERSONNAGE1.getUrl(),
                Images.Personnages.PERSONNAGE2.getUrl(),
                Images.Personnages.PERSONNAGE3.getUrl(),
                Images.Personnages.PERSONNAGE4.getUrl(),
                Images.Personnages.PERSONNAGE5.getUrl(),
                Images.Personnages.PERSONNAGE6.getUrl()
        );

        personnageButtons = new ArrayList<>();
        for (int i = 0; i < personnagesImagesPaths.size(); i++) {
            String path = personnagesImagesPaths.get(i);
            Button personnageButton = creerBoutonAvecImage(path);
            personnageButtons.add(personnageButton);
            persosGrid.add(personnageButton, i % 2, i / 2);

            // Lier les tailles des boutons
            DoubleBinding largeurConteneur = persosGrid.widthProperty().multiply(0.2);
            personnageButton.prefWidthProperty().bind(largeurConteneur);
        }

        return persosGrid;
    }

    private HBox afficherBoutons() {
        HBox boutonsBox = new HBox(40);
        boutonsBox.setAlignment(Pos.CENTER);
        boutonsBox.setPadding(new Insets(60, 0, 0, 0));

        retour = creerBouton("Retour");
        retour.setPrefSize(120, 40);

        annuler = creerBouton("Annuler mes choix");
        annuler.setPrefSize(200, 40);

        valider = creerBouton("Valider");
        valider.setPrefSize(120, 40);

        boutonsBox.getChildren().addAll(retour, annuler, valider);
        return boutonsBox;
    }

    @Override
    public void actualiser() {
        // Mettre à jour l'interface si nécessaire
    }
}
