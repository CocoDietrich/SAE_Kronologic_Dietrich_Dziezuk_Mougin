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

    private static final String TITRE = "Effectuer votre déduction :";
    private static final String RETOUR = "Retour";
    private static final String ANNULER = "Annuler mes choix";
    private static final String VALIDER = "Valider";

    private static final String BACKGROUND_COLOR = "-fx-background-color: #800000;";
    private static final String FONT_FAMILY = "Arial";
    private static final int FONT_SIZE_TITLE = 28;

    private Button valider;
    private Button retour;
    private Button annuler;
    private List<Button> lieuButtons;
    private List<Button> tempsButtons;
    private List<Button> personnageButtons;
    private Lieu lieuMeurtre;
    private Temps tempsMeurtre;
    private Personnage meurtrier;

    public VueDeduction() {
        super();
        this.setPadding(new Insets(20));
        this.setStyle(BACKGROUND_COLOR);

        // Titre
        Text titre = new Text(TITRE);
        titre.setFont(Font.font(FONT_FAMILY, FONT_SIZE_TITLE));
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
        HBox lieuxBox = creerSectionHorizontal(creerBoutonsLieux());

        // Sections temps et personnages
        HBox tempsEtPersos = new HBox(50);
        tempsEtPersos.setAlignment(Pos.CENTER);

        VBox tempsBox = creerSectionVertical("Choisissez le Moment du meurtre :", creerBoutonsTemps());
        VBox personnagesBox = creerSectionVertical("Choisissez le Meurtrier :", creerBoutonsPersonnages());

        tempsEtPersos.getChildren().addAll(tempsBox, personnagesBox);

        contenu.getChildren().addAll(lieuxBox, tempsEtPersos);
        return contenu;
    }

    private HBox creerSectionHorizontal(HBox contenuBoutons) {
        VBox sectionBox = new VBox(10);
        sectionBox.setAlignment(Pos.CENTER);

        Text titreSection = new Text("Choisissez le Lieu du meurtre :");
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

    public Lieu getLieuMeurtre() {
        return lieuMeurtre;
    }

    public Temps getTempsMeurtre() {
        return tempsMeurtre;
    }

    public Personnage getMeurtrier() {
        return meurtrier;
    }

    public void setLieuMeurtre(Lieu lieuMeurtre) {
        this.lieuMeurtre = lieuMeurtre;
    }

    public void setTempsMeurtre(Temps tempsMeurtre) {
        this.tempsMeurtre = tempsMeurtre;
    }

    public void setMeurtrier(Personnage meurtrier) {
        this.meurtrier = meurtrier;
    }
}
