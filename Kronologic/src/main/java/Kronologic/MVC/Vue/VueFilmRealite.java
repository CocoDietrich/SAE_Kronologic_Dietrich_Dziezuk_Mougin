package Kronologic.MVC.Vue;

import Kronologic.Jeu.Elements.Note;
import Kronologic.Jeu.Elements.Pion;
import Kronologic.Jeu.Elements.Realite;
import Kronologic.Jeu.Images;
import Kronologic.MVC.Controleur.ControleurChoixCarte;
import Kronologic.MVC.Modele.ModeleJeu;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
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

import java.util.ArrayList;
import java.util.List;

// TODO : à vérifier
public class VueFilmRealite extends GridPane implements Observateur {

    public Button retour;
    public Slider slider;
    public List<Pion> pions;
    private static final double FACTEUR_ECHELLE = 3.0;
    public List<Polygon> zonesDeJeu = new ArrayList<>();
    public List<Polygon> zonesContenantPions = new ArrayList<>();

    public VueFilmRealite(ModeleJeu modeleJeu) {
        super();
        afficher(modeleJeu);
        actualiser();
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
        slider.setStyle("-fx-background-color: transparent; -fx-text-fill: white;-fx-font-weight: bold;");

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
        HBox calque = new HBox();
        calque.setAlignment(Pos.CENTER);
        calque.setSpacing(0);

        for (int i = 0; i < 6; i++) {
            StackPane lieuContainer = new StackPane();
            lieuContainer.setAlignment(Pos.CENTER);
            lieuContainer.setPadding(new Insets(-1));

            Polygon zonePrincipale = zones.get(i);
            lieuContainer.getChildren().add(zonePrincipale);

            if (i < 4) { // Pour le grand foyer, grand escalier, salle et scène
                GridPane sousZonesGrid = new GridPane();
                sousZonesGrid.setAlignment(Pos.CENTER);

                for (int j = 0; j < 6; j++) {
                    int col = j % 2; // 2 colonnes
                    int row = j / 2; // 3 lignes
                    Polygon sousZone = zones.get(6 + i * 6 + j); // Index des sous-zones
                    sousZonesGrid.add(sousZone, col, row);
                }
                lieuContainer.getChildren().add(sousZonesGrid);
            } else if (i == 4) {
                VBox foyerContainer = new VBox();
                foyerContainer.setAlignment(Pos.CENTER);
                foyerContainer.setSpacing(0);

                GridPane sousZonesFoyerChant = new GridPane();
                sousZonesFoyerChant.setAlignment(Pos.CENTER);

                GridPane sousZonesFoyerDanse = new GridPane();
                sousZonesFoyerDanse.setAlignment(Pos.CENTER);
                sousZonesFoyerDanse.setPadding(new Insets(0));

                for (int j = 0; j < 6; j++) {
                    int sousZoneIndex = 6 + i * 6 + j;
                    sousZonesFoyerChant.add(zones.get(sousZoneIndex), j % 3, j / 3);
                }

                for (int j = 0; j < 6; j++) {
                    int sousZoneIndex = 6 + (i + 1) * 6 + j;
                    sousZonesFoyerDanse.add(zones.get(sousZoneIndex), j % 3, j / 3);
                }

                foyerContainer.getChildren().addAll(sousZonesFoyerChant, sousZonesFoyerDanse);
                lieuContainer.getChildren().add(foyerContainer);
                i++;
            }

            calque.getChildren().add(lieuContainer);
        }

        return calque;
    }


    private List<Polygon> creerSousLieux(Polygon zoneParent, String nomZone, ModeleJeu modeleJeu) {
        ArrayList<Polygon> sousZones = new ArrayList<>();

        // Récupération des coordonnées du lieu parent
        double[] coords = zoneParent.getPoints().stream().mapToDouble(Double::doubleValue).toArray();

        // Ajustements des dimensions en fonction du lieu
        double ajustementX = 0;
        double ajustementY = 0;
        double largeurFactor = 1.0;
        double hauteurFactor = 1.0;

        if (nomZone.contains("Grand foyer") || nomZone.contains("Grand escalier") ||
                nomZone.contains("Salle") || nomZone.contains("Scène")) {
            largeurFactor = 0.98; // Ajustement léger de largeur
            hauteurFactor = 0.98; // Ajustement léger de hauteur
        } else if (nomZone.contains("Foyer du chant") || nomZone.contains("Foyer de la danse")) {
            largeurFactor = 0.95;
            hauteurFactor = 0.95;
        }

        // Création et positionnement des sous-zones
        for (int i = 0; i < 6; i++) {
            double largeur = (coords[2] - coords[0]) * largeurFactor;
            double hauteur = (coords[5] - coords[1]) * hauteurFactor;

            int col, row;

            if (nomZone.contains("Grand foyer") || nomZone.contains("Grand escalier") ||
                    nomZone.contains("Salle") || nomZone.contains("Scène")) {
                col = i % 2; // 2 colonnes
                row = i / 2; // 3 lignes
            } else {
                col = i % 3; // 3 colonnes
                row = i / 3; // 2 lignes
            }

            double largeurPart = largeur / (nomZone.contains("Grand foyer") || nomZone.contains("Grand escalier") ||
                    nomZone.contains("Salle") || nomZone.contains("Scène") ? 2 : 3);
            double hauteurPart = hauteur / (nomZone.contains("Grand foyer") || nomZone.contains("Grand escalier") ||
                    nomZone.contains("Salle") || nomZone.contains("Scène") ? 3 : 2);

            double x1 = coords[0] + ajustementX + col * largeurPart;
            double y1 = coords[1] + ajustementY + row * hauteurPart;
            double x2 = x1 + largeurPart;
            double y2 = y1 + hauteurPart;

            double[] sousLieuCoords = new double[]{x1, y1, x2, y1, x2, y2, x1, y2};

            // Création du sous-lieu
            Polygon sousLieu = creerLieu(sousLieuCoords, nomZone + "-SousZone" + (i + 1), modeleJeu);

            // Ajout de la bordure
            sousLieu.setStroke(Color.TRANSPARENT);
            sousLieu.setStrokeWidth(1);

            // Ajout de la sous-zone à la liste
            sousZones.add(sousLieu);
        }
        return sousZones;
    }

    public List<Polygon> creerZone(String temps, ModeleJeu modeleJeu) {
        zonesDeJeu.clear();
        List<Polygon> zones = new ArrayList<>();

        // Exemple de création de zones principales
        Polygon zone1 = creerLieu(new double[]{0, 0,
                30 * FACTEUR_ECHELLE, 0,
                30 * FACTEUR_ECHELLE, 110 * FACTEUR_ECHELLE,
                0, 110 * FACTEUR_ECHELLE
        }, temps + "-Grand foyer", modeleJeu);
        Polygon zone2 = creerLieu(new double[]{0, 0,
                50 * FACTEUR_ECHELLE, 0,
                50 * FACTEUR_ECHELLE, 80 * FACTEUR_ECHELLE,
                0, 80 * FACTEUR_ECHELLE
        }, temps + "-Grand escalier", modeleJeu);
        Polygon zone3 = creerLieu(new double[]{0, 0,
                50 * FACTEUR_ECHELLE, 0,
                50 * FACTEUR_ECHELLE, 150 * FACTEUR_ECHELLE,
                0, 150 * FACTEUR_ECHELLE
        }, temps + "-Salle", modeleJeu);
        Polygon zone4 = creerLieu(new double[]{0, 0,
                40 * FACTEUR_ECHELLE, 0,
                40 * FACTEUR_ECHELLE, 110 * FACTEUR_ECHELLE,
                0, 110 * FACTEUR_ECHELLE
        }, temps + "-Scène", modeleJeu);
        Polygon zone5 = creerLieu(new double[]{0, 0,
                60 * FACTEUR_ECHELLE, 0,
                60 * FACTEUR_ECHELLE, 40 * FACTEUR_ECHELLE,
                0, 40 * FACTEUR_ECHELLE
        }, temps + "-Foyer du chant", modeleJeu);
        Polygon zone6 = creerLieu(new double[]{0, 0,
                60 * FACTEUR_ECHELLE, 0,
                60 * FACTEUR_ECHELLE, 70 * FACTEUR_ECHELLE,
                0, 70 * FACTEUR_ECHELLE
        }, temps + "-Foyer de la danse", modeleJeu);

        // Ajout des zones à la liste
        zones.add(zone1);
        zonesDeJeu.add(zone1);
        zones.add(zone2);
        zonesDeJeu.add(zone2);
        zones.add(zone3);
        zonesDeJeu.add(zone3);
        zones.add(zone4);
        zonesDeJeu.add(zone4);
        zones.add(zone5);
        zonesDeJeu.add(zone5);
        zones.add(zone6);
        zonesDeJeu.add(zone6);

        List<Polygon> sousZones1 = creerSousLieux(zone1, temps + "-Grand foyer", modeleJeu);
        zones.addAll(sousZones1);
        zonesDeJeu.addAll(sousZones1);
        List<Polygon> sousZones2 = creerSousLieux(zone2, temps + "-Grand escalier", modeleJeu);
        zones.addAll(sousZones2);
        zonesDeJeu.addAll(sousZones2);
        List<Polygon> sousZones3 = creerSousLieux(zone3, temps + "-Salle", modeleJeu);
        zones.addAll(sousZones3);
        zonesDeJeu.addAll(sousZones3);
        List<Polygon> sousZones4 = creerSousLieux(zone4, temps + "-Scène", modeleJeu);
        zones.addAll(sousZones4);
        zonesDeJeu.addAll(sousZones4);
        List<Polygon> sousZones5 = creerSousLieux(zone5, temps + "-Foyer du chant", modeleJeu);
        zones.addAll(sousZones5);
        zonesDeJeu.addAll(sousZones5);
        List<Polygon> sousZones6 = creerSousLieux(zone6, temps + "-Foyer de la danse", modeleJeu);
        zones.addAll(sousZones6);
        zonesDeJeu.addAll(sousZones6);

        return zones;
    }

    private Polygon creerLieu(double[] points, String zoneName, ModeleJeu modeleJeu) {
        Polygon polygon = new Polygon(points);
        polygon.setUserData(zoneName);
        polygon.setFill(Color.TRANSPARENT);

        // Drag and drop pour les lieux (sauf le temps 1)
        if (!polygon.getUserData().toString().contains("Temps 1")) {
            // Ajout de la réception du drag
            polygon.setOnDragOver(event -> {
                if (event.getGestureSource() instanceof ImageView && event.getDragboard().hasImage()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            });

            polygon.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();

                // Ajout de la zone où on a déposé le pion à la liste des zones contenant des pions
                zonesContenantPions.add(polygon);

                if (db.hasImage()) {

                    // Création du pion déplacé
                    Pion pionDeplace = new Pion(null, event.getGestureSource().toString().substring(8, event.getGestureSource().toString().indexOf(",")));
                    if (event.getGestureSource().toString().substring(8, event.getGestureSource().toString().indexOf(",")).contains("Pion de Nombres")) {
                        pionDeplace.setFitHeight(30);
                        pionDeplace.setFitWidth(30);
                    } else {
                        pionDeplace.setFitHeight(30);
                        pionDeplace.setFitWidth(30);
                    }
                    pionDeplace.setId(event.getGestureSource().toString().substring(8, event.getGestureSource().toString().indexOf(",")));
                    pionDeplace.setPreserveRatio(true);
                    pionDeplace.setStyle("-fx-cursor: hand;");
                    pionDeplace.setUserData(zoneName);

                    // Ajouter le pion à la liste des pions
                    pions.add(pionDeplace);

                    // Vérifier si la sous-zone est occupée (sous-zone occupée si doublon dans la liste de pions)
                    boolean sousZoneOccupee = pions.stream()
                            .filter(p -> p.getUserData() != null && p.getUserData().equals(polygon.getUserData()))
                            .count() >= 2;

                    if (sousZoneOccupee) {
                        String nomLieu = polygon.getUserData().toString().split("-")[1];
                        String nomTemps = polygon.getUserData().toString().split("-")[0];
                        if (zonesContenantPions.getLast().getUserData() != zonesContenantPions.get(zonesContenantPions.size() - 2).getUserData()) {
                            zonesContenantPions.removeLast();
                        }
                        Polygon sousZoneLibre = trouverSousZoneLibre(nomLieu, nomTemps);
                        if (sousZoneLibre != null) {

                            // Mise à jour du UserData pour refléter la sous-zone libre
                            pionDeplace.setUserData(sousZoneLibre.getUserData());
                            pions.removeLast();
                            pions.add(pionDeplace);
                            zonesContenantPions.add(sousZoneLibre);

                            // Positionner le pion au centre de la sous-zone libre
                            Point2D pointLibre = sousZoneLibre.localToScene(sousZoneLibre.getBoundsInLocal().getCenterX(), sousZoneLibre.getBoundsInLocal().getCenterY());
                            pionDeplace.setLayoutX(pointLibre.getX() - pionDeplace.getFitWidth() / 2);
                            pionDeplace.setLayoutY(pointLibre.getY() - pionDeplace.getFitHeight() / 2);
                        } else {
                            // On ne fait rien
                            return;
                        }
                    } else {
                        // Positionner le pion au centre de la zone où il a été déposé
                        Point2D point = polygon.localToScene(polygon.getBoundsInLocal().getCenterX(), polygon.getBoundsInLocal().getCenterY());
                        pionDeplace.setLayoutX(point.getX() - pionDeplace.getFitWidth() / 2);
                        pionDeplace.setLayoutY(point.getY() - pionDeplace.getFitHeight() / 2);
                    }

                    VueFilmRealite root = this;
                    root.getChildren().add(pionDeplace);

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
                        new ControleurChoixCarte(modeleJeu.getModeleNotes()).handle(e);
                        e.consume();
                    });

                    event.setDropCompleted(true);
                } else {
                    event.setDropCompleted(false);
                }
                event.consume();
            });
        }

        return polygon;
    }

    private Polygon trouverSousZoneLibre(String nomLieu, String nomTemps) {
        // On récupère les sous-zones du lieu et du temps ciblé
        List<Polygon> zonesMemeLieu = zonesDeJeu.stream()
                .filter(p -> p.getUserData().toString().contains(nomLieu))
                .filter(p -> p.getUserData().toString().contains(nomTemps))
                .filter(p -> p.getUserData().toString().contains("SousZone"))
                .toList();

        // On renvoie la première qui n'est pas dans la liste des zones contenant des pions
        // Si toutes les sous-zones du lieu au temps donné sont déjà occupées, on ne fait rien
        return zonesMemeLieu.stream()
                .filter(p -> !zonesContenantPions.contains(p))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void actualiser() {
        if (slider.getValue() % 1 != 0) {
            return;
        }

        // On remet à zéro les zones prises
        zonesContenantPions.clear();

        // Liste des chemins d'images des personnages
        List<String> cheminsImgPerso = List.of(
                Images.Personnages.PERSONNAGE1.getUrl(),
                Images.Personnages.PERSONNAGE2.getUrl(),
                Images.Personnages.PERSONNAGE3.getUrl(),
                Images.Personnages.PERSONNAGE4.getUrl(),
                Images.Personnages.PERSONNAGE5.getUrl(),
                Images.Personnages.PERSONNAGE6.getUrl()
        );

        // On récupère les pions correspondant à la valeur actuelle du slider
        List<Realite> positions = ModeleJeu.getPartie().getDeroulement().getListePositions();

        // On affiche le tour actuel dans le terminal
        // On en profite pour créer les pions du tour actuel
        List<Pion> pionsTourActuel = new ArrayList<>();
        for (Realite r : positions) {
            if (r.getTemps().getValeur() == Math.round(slider.getValue())) {
                String img = "";
                // On récupère l'image correspondante au personnage
                for (String s : cheminsImgPerso) {
                    if (s.contains(r.getPersonnage().getNom())) {
                        img = s;
                        break;
                    }
                }
                pionsTourActuel.add(new Pion(new Note(r.getLieu(), r.getTemps(), r.getPersonnage()), img));
            }
        }

        // On place les pions
        for (Pion pion : pionsTourActuel) {
            for (Polygon zone : zonesDeJeu) {
                if (((String) zone.getUserData()).split("-SousZone").length == 1) {
                    continue;
                }
                if (((String) zone.getUserData()).split("-SousZone")[0].equals("Temps "
                        + pion.getNote().getTemps().getValeur()
                        + ".0-" + pion.getNote().getLieu().getNom())
                        && !zonesContenantPions.contains(zone)) {
                    // On calcule la position du pion dans la grille par rapport à la sous zone
                    int positionX = 0;
                    int positionY = 0;
                    int index = Integer.parseInt(((String) zone.getUserData()).split("-SousZone")[1]);
                    // Cas des 4 premières zones
                    // [1, 2]
                    // [3, 4]
                    // [5, 6]
                    if (Math.round(slider.getValue()) < 5) {
                        switch (index) {
                            case 2 -> positionX = 1;
                            case 3 -> positionY = 1;
                            case 4 -> {
                                positionX = 1;
                                positionY = 1;
                            }
                            case 5 -> positionY = 2;
                            case 6 -> {
                                positionX = 1;
                                positionY = 2;
                            }
                        }
                    }
                    // Cas des 2 dernières zones
                    // [1, 2, 3]
                    // [4, 5, 6]
                    else {
                        switch (index) {
                            case 2 -> positionX = 1;
                            case 3 -> positionX = 2;
                            case 4 -> positionY = 1;
                            case 5 -> {
                                positionX = 1;
                                positionY = 1;
                            }
                            case 6 -> {
                                positionX = 2;
                                positionY = 1;
                            }
                        }
                    }

                    pion.setFitHeight(50);
                    pion.setFitWidth(50);

                    zonesContenantPions.add(zone);

                    GridPane.setColumnIndex(pion, positionX);
                    GridPane.setRowIndex(pion, positionY);


                    ((Pane) zone.getParent()).getChildren().add(pion);

                    break;
                }
            }
        }
    }
}