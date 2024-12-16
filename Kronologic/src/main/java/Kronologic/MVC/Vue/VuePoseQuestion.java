package Kronologic.MVC.Vue;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

import static Kronologic.MVC.Vue.VueAccueil.creerButton;
import static Kronologic.MVC.Vue.VueAccueil.creerButtonAvecImage;

public class VuePoseQuestion extends BorderPane implements Observateur {

    public Button valider;
    public Button retour;
    public Button annuler;
    public List<Button> lieuButtons;
    public List<Button> tempsButtons;
    public List<Button> personnageButtons;
    public Lieu lieuChoisi;
    public Temps tempsChoisi;
    public Personnage personnageChoisi;

    public VuePoseQuestion() {
        super();
        this.setPadding(new Insets(20));
        this.setStyle("-fx-background-color: #800000;");

        // Ajouter le titre
        Text titre = new Text("Posez une question :");
        titre.setFont(Font.font("Arial", 28));
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
        HBox lieuxBox = creerSectionHorizontal("Choisissez un Lieu :", creerBoutonsLieux());

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
                "file:img/Lieu_Salle_3.png",
                "file:img/Lieu_Scène_4.png",
                "file:img/Lieu_Foyer de la danse_5.png",
                "file:img/Lieu_Foyer du chant_6.png",
                "file:img/Lieu_Grand escalier_2.png",
                "file:img/Lieu_Grand foyer_1.png"
        );

        lieuButtons = new ArrayList<>();
        for (String path : lieuxImagesPaths) {
            Button lieuButton = creerButtonAvecImage(path);
            lieuButtons.add(lieuButton);
            lieuxBox.getChildren().add(lieuButton);
        }

        return lieuxBox;
    }

    private GridPane creerBoutonsTemps() {
        GridPane tempsGrid = new GridPane();
        tempsGrid.setAlignment(Pos.CENTER);
        tempsGrid.setHgap(10);
        tempsGrid.setVgap(10);

        List<String> tempsImagesPaths = List.of(
                "file:img/temps1.png",
                "file:img/temps2.png",
                "file:img/temps3.png",
                "file:img/temps4.png",
                "file:img/temps5.png",
                "file:img/temps6.png"
        );

        tempsButtons = new ArrayList<>();
        for (int i = 0; i < tempsImagesPaths.size(); i++) {
            String path = tempsImagesPaths.get(i);
            Button tempsButton = creerButtonAvecImage(path);
            tempsButtons.add(tempsButton);
            tempsGrid.add(tempsButton, i % 2, i / 2);
        }

        return tempsGrid;
    }

    private GridPane creerBoutonsPersonnages() {
        GridPane persosGrid = new GridPane();
        persosGrid.setAlignment(Pos.CENTER);
        persosGrid.setHgap(10);
        persosGrid.setVgap(10);

        List<String> personnagesImagesPaths = List.of(
                "file:img/Journaliste.png",
                "file:img/Baronne.png",
                "file:img/Servante.png",
                "file:img/Aventurière.png",
                "file:img/Chauffeur.png",
                "file:img/Détective.png"
        );

        personnageButtons = new ArrayList<>();
        for (int i = 0; i < personnagesImagesPaths.size(); i++) {
            String path = personnagesImagesPaths.get(i);
            Button personnageButton = creerButtonAvecImage(path);
            personnageButtons.add(personnageButton);
            persosGrid.add(personnageButton, i % 2, i / 2);
        }

        return persosGrid;
    }

    private HBox afficherBoutons() {
        HBox boutonsBox = new HBox(40);
        boutonsBox.setAlignment(Pos.CENTER);
        boutonsBox.setPadding(new Insets(60, 0, 0, 0)); // Réduction de l'espacement supérieur

        retour = creerButton("Retour");
        retour.setPrefSize(120, 40);

        annuler = creerButton("Annuler mes choix");
        annuler.setPrefSize(200, 40);

        valider = creerButton("Valider");
        valider.setPrefSize(120, 40);

        boutonsBox.getChildren().addAll(retour, annuler, valider);
        return boutonsBox;
    }



    @Override
    public void actualiser() {
        // Mettre à jour l'interface si nécessaire
    }
}
