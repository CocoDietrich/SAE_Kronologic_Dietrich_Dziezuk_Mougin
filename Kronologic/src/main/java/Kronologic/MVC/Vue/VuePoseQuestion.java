package Kronologic.MVC.Vue;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.Jeu.Enums.ImageLieux;
import Kronologic.Jeu.Enums.ImagePersonnages;
import Kronologic.Jeu.Enums.ImageTemps;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

import static Kronologic.MVC.Vue.VueAccueil.creerBouton;
import static Kronologic.MVC.Vue.VueAccueil.creerBoutonAvecImage;

public class VuePoseQuestion extends BorderPane implements Observateur {

    private final static String TITRE = "Posez une question :";
    private final static String RETOUR = "Retour";
    private final static String ANNULER = "Annuler mes choix";
    private final static String VALIDER = "Valider";

    private final static String FONT_FAMILY = "Arial";

    private Button valider;
    private Button retour;
    private Button annuler;
    private List<Button> lieuButtons;
    private List<Button> tempsButtons;
    private List<Button> personnageButtons;
    private Lieu lieuChoisi;
    private Temps tempsChoisi;
    private Personnage personnageChoisi;

    public VuePoseQuestion() {
        super();
        this.setPadding(new Insets(20));
        this.setStyle("-fx-background-color: #800000;");

        // Ajouter le titre
        Text titre = new Text(TITRE);
        titre.setFont(Font.font(FONT_FAMILY, 28));
        titre.setFill(Color.WHITE);

        VBox titreBox = new VBox(titre);
        titreBox.setAlignment(Pos.CENTER);
        this.setTop(titreBox);

        // Afficher le contenu principal
        VBox contenuPrincipal = afficherContenu();

        // Ajouter les boutons directement après le contenu principal
        HBox boutons = afficherBoutons();

        VBox centre = new VBox(10); // Réduire l'espacement entre le contenu et les boutons
        centre.setAlignment(Pos.CENTER);
        centre.getChildren().addAll(contenuPrincipal, boutons);

        this.setCenter(centre);
    }

    private VBox afficherContenu() {
        VBox contenu = new VBox(30);
        contenu.setAlignment(Pos.CENTER);

        // Lieux
        HBox lieuxBox = creerSectionHorizontal(creerBoutonsLieux());

        // Temps et personnages
        HBox tempsEtPersos = new HBox(50);
        tempsEtPersos.setAlignment(Pos.CENTER);

        // Temps (vertical, 2 colonnes)
        VBox tempsBox = creerSectionVertical("Choisissez un Temps :", creerBoutonsTemps());

        // Personnages (vertical, 2 colonnes)
        VBox personnagesBox = creerSectionVertical("Choisissez un Personnage :", creerBoutonsPersonnages());

        tempsEtPersos.getChildren().addAll(tempsBox, personnagesBox);

        contenu.getChildren().addAll(lieuxBox, tempsEtPersos);
        return contenu;
    }

    private HBox creerSectionHorizontal(HBox contenuBoutons) {
        VBox sectionBox = new VBox(10);
        sectionBox.setAlignment(Pos.CENTER);

        Text titreSection = new Text("Choisissez un Lieu :");
        titreSection.setFont(Font.font(FONT_FAMILY, 20));
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
        titreSection.setFont(Font.font(FONT_FAMILY, 20));
        titreSection.setFill(Color.WHITE);

        sectionBox.getChildren().addAll(titreSection, contenuBoutons);
        return sectionBox;
    }

    private HBox creerBoutonsLieux() {
        HBox lieuxBox = new HBox(15);
        lieuxBox.setAlignment(Pos.CENTER);

        List<String> lieuxImagesPaths = List.of(
                ImageLieux.LIEU1.getUrl(),
                ImageLieux.LIEU2.getUrl(),
                ImageLieux.LIEU3.getUrl(),
                ImageLieux.LIEU4.getUrl(),
                ImageLieux.LIEU5.getUrl(),
                ImageLieux.LIEU6.getUrl()
        );

        lieuButtons = new ArrayList<>();
        for (String path : lieuxImagesPaths) {
            Button lieuButton = creerBoutonAvecImage(path);
            lieuButtons.add(lieuButton);
            lieuxBox.getChildren().add(lieuButton);

            // Définir une taille relative (en pourcentage) basée sur la taille de la scène ou d'un conteneur parent stable
            DoubleBinding largeurConteneur = this.widthProperty().multiply(0.15); // 15% de la largeur de la scène
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
                ImageTemps.TEMPS1.getUrl(),
                ImageTemps.TEMPS2.getUrl(),
                ImageTemps.TEMPS3.getUrl(),
                ImageTemps.TEMPS4.getUrl(),
                ImageTemps.TEMPS5.getUrl(),
                ImageTemps.TEMPS6.getUrl()
        );

        tempsButtons = new ArrayList<>();
        for (int i = 0; i < tempsImagesPaths.size(); i++) {
            String path = tempsImagesPaths.get(i);
            Button tempsButton = creerBoutonAvecImage(path);
            tempsButtons.add(tempsButton);
            tempsGrid.add(tempsButton, i % 2, i / 2);

            // Lier les tailles des boutons à un pourcentage de la largeur du conteneur parent
            DoubleBinding largeurConteneur = tempsGrid.widthProperty().multiply(0.2); // 20% de la largeur
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
                ImagePersonnages.PERSONNAGE1.getUrl(),
                ImagePersonnages.PERSONNAGE2.getUrl(),
                ImagePersonnages.PERSONNAGE3.getUrl(),
                ImagePersonnages.PERSONNAGE4.getUrl(),
                ImagePersonnages.PERSONNAGE5.getUrl(),
                ImagePersonnages.PERSONNAGE6.getUrl()
        );

        personnageButtons = new ArrayList<>();
        for (int i = 0; i < personnagesImagesPaths.size(); i++) {
            String path = personnagesImagesPaths.get(i);
            Button personnageButton = creerBoutonAvecImage(path);
            personnageButtons.add(personnageButton);
            persosGrid.add(personnageButton, i % 2, i / 2);

            // Lier les tailles des boutons à un pourcentage de la largeur du conteneur parent
            DoubleBinding largeurConteneur = persosGrid.widthProperty().multiply(0.2); // 20% de la largeur
            personnageButton.prefWidthProperty().bind(largeurConteneur);
        }

        return persosGrid;
    }

    private HBox afficherBoutons() {
        HBox boutonsBox = new HBox(40);
        boutonsBox.setAlignment(Pos.CENTER);
        boutonsBox.setPadding(new Insets(60, 0, 0, 0)); // Réduction de l'espacement supérieur

        retour = creerBouton(RETOUR);
        retour.setPrefSize(120, 40);

        annuler = creerBouton(ANNULER);
        annuler.setPrefSize(200, 40);

        valider = creerBouton(VALIDER);
        valider.setPrefSize(120, 40);

        boutonsBox.getChildren().addAll(retour, annuler, valider);
        return boutonsBox;
    }

    @Override
    public void actualiser() {}

    public Button getValider() {
        return valider;
    }

    public Button getRetour() {
        return retour;
    }

    public Button getAnnuler() {
        return annuler;
    }

    public List<Button> getLieuButtons() {
        return lieuButtons;
    }

    public List<Button> getTempsButtons() {
        return tempsButtons;
    }

    public List<Button> getPersonnageButtons() {
        return personnageButtons;
    }

    public Lieu getLieuChoisi() {
        return lieuChoisi;
    }

    public Temps getTempsChoisi() {
        return tempsChoisi;
    }

    public Personnage getPersonnageChoisi() {
        return personnageChoisi;
    }

    public void setLieuChoisi(Lieu lieuChoisi) {
        this.lieuChoisi = lieuChoisi;
    }

    public void setTempsChoisi(Temps tempsChoisi) {
        this.tempsChoisi = tempsChoisi;
    }

    public void setPersonnageChoisi(Personnage personnageChoisi) {
        this.personnageChoisi = personnageChoisi;
    }
}