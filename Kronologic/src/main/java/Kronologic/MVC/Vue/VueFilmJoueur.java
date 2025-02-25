package Kronologic.MVC.Vue;

import Kronologic.Jeu.Elements.Note;
import Kronologic.Jeu.Elements.Pion;
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

// TODO : A verifier
public class VueFilmJoueur extends GridPane implements Observateur {

    public Button retour;
    public Slider slider;
    public List<Pion> pions;
    public List<Polygon> zonesDeJeu = new ArrayList<>();
    public List<Polygon> zonesContenantPions = new ArrayList<>();
    private static final double FACTEUR_ECHELLE = 1.25;

    public VueFilmJoueur(ModeleJeu modeleJeu) {
        super();
        afficher(modeleJeu);
    }

    public void afficher(ModeleJeu modeleJeu) {
        this.setStyle("-fx-background-color: #800000; -fx-padding: 30;");
        this.setHgap(175);
        this.setVgap(50);

        // Flèche de retour
        this.add(afficherRetour(), 0, 0);

        // Slider
        HBox sliderBox = afficherSlider();
        this.add(sliderBox, 1, 0);

        // Cartes
        List<HBox> cartes = afficherCarte(modeleJeu);
        this.add(cartes.getFirst(), 0, 1);
        this.add(cartes.getLast(), 0, 2);
        setColumnSpan(cartes.getFirst(), 2);
        setColumnSpan(cartes.getLast(), 2);
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
        slider.setMin(0);
        slider.setMax(0);
        slider.setValue(0); // Valeur initiale
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(0);
        slider.setBlockIncrement(1);
        slider.setSnapToTicks(true);
        slider.setPrefWidth(1000);
        slider.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");

        Label compteur = new Label("Tour : " + slider.getValue());
        compteur.setStyle("-fx-text-fill: #FFCC66");

        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            compteur.setText("Tour : " + String.format("%.2f", newValue.doubleValue()));
        });

        HBox sliderBox = new HBox(slider, compteur);
        sliderBox.setStyle("-fx-padding: 20; -fx-alignment: center;");
        sliderBox.setSpacing(10);
        sliderBox.setAlignment(Pos.CENTER);

        return sliderBox;
    }

    public List<HBox> afficherCarte(ModeleJeu modeleJeu) {
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
        HBox hBoxHaut = new HBox(5);
        hBoxHaut.setSpacing(150);
        hBoxHaut.setAlignment(Pos.CENTER);
        hBoxHaut.setId("carte");

        HBox hBoxBas = new HBox(5);
        hBoxBas.setSpacing(150);
        hBoxBas.setAlignment(Pos.CENTER);
        hBoxBas.setId("carte");

        zonesDeJeu.clear();

        // Création de la grille pour afficher 6 cartes (3 en haut, 3 en bas)
        for (int i = 0; i < 6; i++) {
            // Récupération des zones interactives
            List<Polygon> zones = creerZone("Temps " + (i+1), modeleJeu);

            // Superposition de l'image et des zones interactives via un StackPane
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);

            // Image principale de la carte (plateau)
            ImageView imageViewCarte = new ImageView(carte);
            imageViewCarte.setPreserveRatio(true);
            imageViewCarte.setFitHeight(160);
            imageViewCarte.setFitWidth(Double.MAX_VALUE);

            stackPane.getChildren().addAll(imageViewCarte, creerCalque(zones));

            // Création de l'image du temps au-dessus de la carte
            ImageView imageViewTemps = new ImageView(tempsImages.get(i));
            imageViewTemps.setPreserveRatio(true);
            imageViewTemps.setFitHeight(32);

            VBox vBox = new VBox(10);
            vBox.setAlignment(Pos.CENTER);
            vBox.getChildren().addAll(imageViewTemps, stackPane);

            // Ajouter au bon conteneur (haut ou bas)
            if (i < 3) {
                hBoxHaut.getChildren().add(vBox);
            } else {
                hBoxBas.getChildren().add(vBox);
            }
        }

        // Retourner les deux conteneurs HBox contenant les cartes
        return List.of(hBoxHaut, hBoxBas);
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
            }
            else if (i == 4) {
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
        List<Polygon> zones = new ArrayList<>();

        // Exemple de création de zones principales
        Polygon zone1 = creerLieu(new double[]{0, 0, 30, 0, 30, 110, 0, 110}, temps + "-Grand foyer", modeleJeu);
        Polygon zone2 = creerLieu(new double[]{0, 0, 50, 0, 50, 80, 0, 80}, temps + "-Grand escalier", modeleJeu);
        Polygon zone3 = creerLieu(new double[]{0, 0, 50, 0, 50, 150, 0, 150}, temps + "-Salle", modeleJeu);
        Polygon zone4 = creerLieu(new double[]{0, 0, 40, 0, 40, 110, 0, 110}, temps + "-Scène", modeleJeu);
        Polygon zone5 = creerLieu(new double[]{0, 0, 60, 0, 60, 40, 0, 40}, temps + "-Foyer du chant", modeleJeu);
        Polygon zone6 = creerLieu(new double[]{0, 0, 60, 0, 60, 70, 0, 70}, temps + "-Foyer de la danse", modeleJeu);

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

                System.out.println(event.getGestureSource());

                if (db.hasImage()) {

                    // Création du pion déplacé
                    Pion pionDeplace = new Pion(null, event.getGestureSource().toString().substring(8, event.getGestureSource().toString().indexOf(",")));
                    if (event.getGestureSource().toString().substring(8, event.getGestureSource().toString().indexOf(",")).contains("Pion de Nombres")) {
                        pionDeplace.setFitHeight(30);
                        pionDeplace.setFitWidth(30);
                    } else {
                        pionDeplace.setFitHeight(30);
                        pionDeplace.setFitWidth(30);
                        if (event.getGestureSource().toString().substring(8, event.getGestureSource().toString().indexOf(",")).contains("hypotheses")) {
                            pionDeplace.setScaleX(1.3);
                            pionDeplace.setScaleY(1.3);
                        }
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

                    VueFilmJoueur root = this;
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

    private String cheminAbsenceHypothese(String chemin, boolean absence, boolean hypothese, boolean nombre){
        String a = "pions_absences/";
        String h = "pions_hypotheses/";
        String ah = "pions_hypotheses_absences/";
        String insertion = "";

        if (absence && hypothese){
            insertion = ah;
        }
        else if (absence){
            insertion = a;
        }
        else if (hypothese){
            insertion = h;
        }

        int position = chemin.indexOf("pions_personnages/") + "pions_personnages/".length();
        if (nombre){
            position = chemin.indexOf("pions_nombres/") + "pions_nombres/".length();
        }


        StringBuilder sb = new StringBuilder(chemin);
        sb.insert(position, insertion);
        return sb.toString();
    }

    @Override
    public void actualiser() {
        // On met à jour le slider
        slider.setMax(ModeleJeu.getPartie().getNbQuestion());

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

        // Liste des chemins d'images des pions de nombres
        List<String> cheminsImgPionNombres = List.of(
                Images.Nombre.NOMBRE0.getUrl(),
                Images.Nombre.NOMBRE1.getUrl(),
                Images.Nombre.NOMBRE2.getUrl(),
                Images.Nombre.NOMBRE3.getUrl(),
                Images.Nombre.NOMBRE4.getUrl(),
                Images.Nombre.NOMBRE5.getUrl()
        );

        // On récupère les pions correspondant à la valeur actuelle du slider (tour actuel)
        List<Note> notesTour = ModeleJeu.getPartie().getHistorique().get((int) Math.round(slider.getValue()));

        // On place les notes
        for (Note note : notesTour){
            if (note.getTemps().getValeur() == 1
                    && note.getPersonnage() == null){
                continue;
            }
            for (Polygon zone : zonesDeJeu){
                if (((String) zone.getUserData()).split("-SousZone").length == 1
                        || zonesContenantPions.contains(zone)
                        || zone.getParent() == null) {
                    continue;
                }
                if (((String) zone.getUserData()).split("-SousZone")[0].equals("Temps "
                        + note.getTemps().getValeur()
                        + "-" + note.getLieu().getNom())) {
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

                    // On cherche le chemin de l'image du pion
                    String cheminImgPion = "";
                    if (note.getPersonnage() == null) {
                        // Cas du pion de nombres présent (défaut)
                        for (String cheminImg : cheminsImgPionNombres) {
                            if (cheminImg.contains("Pion de Nombres_" + note.getNbPersonnages() + ".png")) {
                                cheminImgPion = cheminImg;
                                break;
                            }
                        }
                        // Cas du pion nombres absent
                        if (note.estAbsence() && !note.estHypothese()){
                            cheminImgPion = cheminAbsenceHypothese(cheminImgPion, true, false, true);
                        }
                        // Cas du pion nombres hypothèse
                        else if (!note.estAbsence() && note.estHypothese()){
                            cheminImgPion = cheminAbsenceHypothese(cheminImgPion, false, true, true);
                        }
                        // Cas du pion nombres absence hypothèse
                        else if (note.estAbsence() && note.estHypothese()){
                            cheminImgPion = cheminAbsenceHypothese(cheminImgPion, true, true, true);
                        }
                    }
                    else {
                        // Cas du pion de personnage (défaut)
                        for (String cheminImg : cheminsImgPerso) {
                            if (cheminImg.contains(note.getPersonnage().getNom())) {
                                cheminImgPion = cheminImg;
                                break;
                            }
                        }
                        // Cas du pion de personnage absent
                        if (note.estAbsence() && !note.estHypothese()){
                            cheminImgPion = cheminAbsenceHypothese(cheminImgPion, true, false, false);
                        }
                        // Cas du pion de personnage hypothèse
                        else if (!note.estAbsence() && note.estHypothese()){
                            cheminImgPion = cheminAbsenceHypothese(cheminImgPion, false, true, false);
                        }
                        // Cas du pion de personnage absence hypothèse
                        else if (note.estAbsence() && note.estHypothese()){
                            cheminImgPion = cheminAbsenceHypothese(cheminImgPion, true, true, false);
                        }
                    }

                    Pion pion = new Pion(note, cheminImgPion);
                    pion.setUserData(zone.getUserData());
                    pion.setImageView(cheminImgPion);
                    pion.setId(cheminImgPion);
                    pion.setPreserveRatio(true);

                    // On cherche à modifier l'image du pion
                    if (pion.getNote().getPersonnage() == null) {
                        pion.setFitHeight(30);
                        pion.setFitWidth(30);
                        if (pion.getNote().estAbsence() && !pion.getNote().estHypothese()) {
                            pion.setScaleX(0.8);
                            pion.setScaleY(0.8);
                        }
                        else if (pion.getNote().estAbsence() || pion.getNote().estHypothese()) {
                            pion.setScaleX(0.9);
                            pion.setScaleY(0.9);
                        }
                    } else {
                        pion.setFitHeight(30);
                        pion.setFitWidth(30);
                        if (pion.getNote().estAbsence() && !pion.getNote().estHypothese()) {
                            pion.setScaleX(1.2);
                            pion.setScaleY(1.2);
                        }
                        else if (pion.getNote().estAbsence() || pion.getNote().estHypothese()) {
                            pion.setScaleX(1.3);
                            pion.setScaleY(1.3);
                        }
                    }

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
