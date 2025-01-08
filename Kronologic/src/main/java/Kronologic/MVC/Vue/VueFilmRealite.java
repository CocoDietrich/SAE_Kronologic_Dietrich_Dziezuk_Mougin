package Kronologic.MVC.Vue;

import Kronologic.Jeu.Elements.Pion;
import Kronologic.Jeu.Images;
import Kronologic.MVC.Controleur.ControleurChoixCarte;
import Kronologic.MVC.Modele.ModeleJeu;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.List;

public class VueFilmRealite extends GridPane implements Observateur {

    public Button retour;
    public Slider slider;
    public List<Pion> pions;
    public List<Polygon> zonesDePions;
    private static final double FACTEUR_ECHELLE = 3.0;

    public VueFilmRealite(ModeleJeu modeleJeu) {
        super();
        afficher(modeleJeu);
    }

    public void afficher(ModeleJeu modeleJeu) {
        this.setStyle("-fx-background-color: #800000; -fx-padding: 30;");
        this.setHgap(175);
        this.setVgap(50);

        // Flèche de retour
        HBox retourBox = afficherRetour();
        this.add(retourBox, 0, 0);

        // Slider
        HBox sliderBox = afficherSlider();

        // On l'affiche au centre en haut
        this.add(sliderBox, 1, 0);

        // Carte
        HBox carteBox = afficherCarte(modeleJeu);
        this.add(carteBox, 0, 1);
        setColumnSpan(carteBox, 2);
    }

    public HBox afficherRetour() {
        // Bouton retour
        Image flecheRetour = new Image("file:img/flecheRetour.png");
        ImageView imageView = new ImageView(flecheRetour);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(20);

        ColorAdjust invertEffect = new ColorAdjust();
        invertEffect.setBrightness(1.0); // Inversion des couleurs
        imageView.setEffect(invertEffect);

        retour = new Button();
        retour.setId("retour");
        retour.setGraphic(imageView);
        retour.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");
        retour.setOnMouseEntered(e -> retour.setStyle("-fx-background-color: #800000; -fx-font-size: 24px; -fx-cursor: hand;"));
        retour.setOnMouseExited(e -> retour.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 24px;"));

        HBox retourBox = new HBox(retour);
        retourBox.setAlignment(Pos.CENTER_LEFT);
        retourBox.setPadding(new Insets(10, 0, 10, 0));

        return retourBox;
    }

    public HBox afficherSlider() {
        slider = new Slider();
        slider.setMin(1);
        slider.setMax(6);
        slider.setValue(1);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(0);
        slider.setBlockIncrement(1);
        slider.setSnapToTicks(true);
        slider.setPrefWidth(1000);
        slider.setStyle("-fx-background-color: transparent; -fx-text-fill: white;-fx-font-weight: bold;" );

        Label compteur = new Label("Temps : " + slider.getValue());
        compteur.setStyle("-fx-text-fill: #FFCC66");

        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            compteur.setText("Temps : " + String.format("%.2f", newValue.doubleValue()));
        });

        HBox sliderBox = new HBox(slider, compteur);
        sliderBox.setStyle("-fx-padding: 20; -fx-alignment: center;");
        sliderBox.setSpacing(10);

        return sliderBox;
    }

    public HBox afficherCarte(ModeleJeu modeleJeu) {
        // Chargement de l'image principale de la carte (le plateau)
        Image carte = new Image("file:img/plateau.png");

        // Images représentant le temps
        List<Image> tempsImages = List.of(
                Images.Temps.TEMPS1.creerImage(),
                Images.Temps.TEMPS2.creerImage(),
                Images.Temps.TEMPS3.creerImage(),
                Images.Temps.TEMPS4.creerImage(),
                Images.Temps.TEMPS5.creerImage(),
                Images.Temps.TEMPS6.creerImage()
        );

        // Création des conteneurs horizontaux pour les cartes du haut et du bas
        HBox hBox = new HBox(5);
        hBox.setSpacing(150);
        hBox.setAlignment(Pos.CENTER);

        // Récupération des zones interactives
        List<Polygon> zones = creerZone("Temps " + (slider.getValue()), modeleJeu);

        // Superposition de l'image et des zones interactives via un StackPane
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.CENTER);

        // Image principale de la carte (plateau)
        ImageView imageViewCarte = new ImageView(carte);
        imageViewCarte.setPreserveRatio(true);
        imageViewCarte.setFitHeight(160 * FACTEUR_ECHELLE);
        imageViewCarte.setFitWidth(Double.MAX_VALUE);

        stackPane.getChildren().addAll(imageViewCarte, creerCalque(zones));

        // Création de l'image du temps au-dessus de la carte
        ImageView imageViewTemps = new ImageView(tempsImages.get((int) slider.getValue() - 1));
        imageViewTemps.setPreserveRatio(true);
        imageViewTemps.setFitHeight(32 * FACTEUR_ECHELLE);

        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(imageViewTemps, stackPane);

        hBox.getChildren().add(vBox);
        hBox.setId("carte");

        // Retourner les deux conteneurs HBox contenant les cartes
        return hBox;
    }

    public HBox creerCalque(List<Polygon> zones) {
        // Création d'un calque pour superposer les zones interactives
        HBox calque = new HBox();
        calque.setAlignment(Pos.CENTER);

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(zones.get(4), zones.get(5));

        // Ajout des zones interactives dans le calque
        calque.getChildren().addAll(zones.get(0), zones.get(1), zones.get(2), zones.get(3), vBox);

        return calque;
    }

    public List<Polygon> creerZone(String temps, ModeleJeu modeleJeu) {
        // Zone 1 (Salle Orange à gauche)
        Polygon zone1 = creerLieu(new double[]{
                0, 0,  // Coin supérieur gauche
                30 * FACTEUR_ECHELLE, 0, // Coin supérieur droit
                30 * FACTEUR_ECHELLE, 110 * FACTEUR_ECHELLE, // Coin inférieur droit
                0, 110 * FACTEUR_ECHELLE   // Coin inférieur gauche
        }, temps + "-Grand foyer", modeleJeu);

        // Zone 2 (Salle Bleue au centre gauche)
        Polygon zone2 = creerLieu(new double[]{
                0, 0,  // Coin supérieur gauche
                50 * FACTEUR_ECHELLE, 0,  // Coin supérieur droit
                50 * FACTEUR_ECHELLE, 80 * FACTEUR_ECHELLE,  // Coin inférieur droit
                0, 80 * FACTEUR_ECHELLE   // Coin inférieur gauche
        }, temps + "-Grand escalier", modeleJeu);

        // Zone 3 (Grande Scène Rouge)
        Polygon zone3 = creerLieu(new double[]{
                0, 0,  // Coin supérieur gauche
                50 * FACTEUR_ECHELLE, 0,  // Coin supérieur droit
                50 * FACTEUR_ECHELLE, 150 * FACTEUR_ECHELLE,  // Coin inférieur droit
                0, 150 * FACTEUR_ECHELLE   // Coin inférieur gauche
        }, temps + "-Salle", modeleJeu);

        // Zone 4 (Couloir Sombre milieu droit)
        Polygon zone4 = creerLieu(new double[]{
                0, 0,  // Coin supérieur gauche
                40 * FACTEUR_ECHELLE, 0,  // Coin supérieur droit
                40 * FACTEUR_ECHELLE, 110 * FACTEUR_ECHELLE,  // Coin inférieur droit
                0, 110 * FACTEUR_ECHELLE   // Coin inférieur gauche
        }, temps + "-Scène", modeleJeu);

        // Zone 5 (Salle Bleue en haut à droite)
        Polygon zone5 = creerLieu(new double[]{
                0, 0,  // Coin supérieur gauche
                60 * FACTEUR_ECHELLE, 0,  // Coin supérieur droit
                60 * FACTEUR_ECHELLE, 40 * FACTEUR_ECHELLE, // Coin inférieur droit
                0, 40 * FACTEUR_ECHELLE  // Coin inférieur gauche
        }, temps + "-Foyer du chant", modeleJeu);

        // Zone 6 (Salle Dorée en bas à droite)
        Polygon zone6 = creerLieu(new double[]{
                0, 0,  // Coin supérieur gauche
                60 * FACTEUR_ECHELLE, 0,  // Coin supérieur droit
                60 * FACTEUR_ECHELLE, 70 * FACTEUR_ECHELLE,  // Coin inférieur droit
                0, 70 * FACTEUR_ECHELLE   // Coin inférieur gauche
        }, temps + "-Foyer de la danse", modeleJeu);

        zonesDePions = List.of(zone1, zone2, zone3, zone4, zone5, zone6);
        return List.of(zone1, zone2, zone3, zone4, zone5, zone6);
    }

    private Polygon creerLieu(double[] points, String zoneName, ModeleJeu modeleJeu) {
        Polygon polygon = new Polygon(points);
        polygon.setUserData(zoneName);
        polygon.setFill(Color.TRANSPARENT);

        // Ajout de la réception du drag
        polygon.setOnDragOver(event -> {
            if (event.getGestureSource() instanceof ImageView && event.getDragboard().hasImage()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        polygon.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasImage()) {

                // Création du pion déplacé
                Pion pionDeplace = new Pion(null, event.getGestureSource().toString().substring(8, event.getGestureSource().toString().indexOf(",")));
                if (event.getGestureSource().toString().substring(8, event.getGestureSource().toString().indexOf(",")).contains("Pion de Nombres")) {
                    pionDeplace.setFitHeight(47.5);
                    pionDeplace.setFitWidth(47.5);
                } else {
                    pionDeplace.setFitHeight(30);
                    pionDeplace.setFitWidth(30);
                }
                pionDeplace.setId(event.getGestureSource().toString().substring(8, event.getGestureSource().toString().indexOf(",")));
                pionDeplace.setPreserveRatio(true);
                pionDeplace.setStyle("-fx-cursor: hand;");

                // Activer le drag & drop pour le pion déplacé
                pionDeplace.setOnDragDetected(e -> {
                    Dragboard newDb = pionDeplace.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent content = new ClipboardContent();
                    content.putImage(pionDeplace.getImage());
                    newDb.setContent(content);
                    e.consume();
                });

                pionDeplace.setOnDragDone(e -> {
                    // Supprimer le pion si le dépôt n'est pas réussi
                    if (!e.isDropCompleted()) {
                        ((Pane) pionDeplace.getParent()).getChildren().remove(pionDeplace);
                        pions.add(new Pion(null, pionDeplace.getImage().getUrl()));
                    }

                    // Déléguer la logique métier au contrôleur
                    new ControleurChoixCarte(modeleJeu, polygon).handle(e);
                    e.consume();
                });

                // Positionner le pion là où le curseur a été lâché
                double sceneX = event.getSceneX();
                double sceneY = event.getSceneY();
                VueFilmRealite root = this;
                root.getChildren().add(pionDeplace);
                javafx.geometry.Point2D dropPoint = root.sceneToLocal(sceneX, sceneY);

                pionDeplace.setLayoutX(dropPoint.getX() - pionDeplace.getFitWidth() / 2);
                pionDeplace.setLayoutY(dropPoint.getY() - pionDeplace.getFitHeight() / 2);

                event.setDropCompleted(true);

                pionDeplace.setUserData(zoneName);

                // Ajouter le pion à la liste des pions
                pions.add(pionDeplace);
            } else {
                event.setDropCompleted(false);
            }
            event.consume();
        });

        return polygon;
    }

    @Override
    public void actualiser() {
    }
}
