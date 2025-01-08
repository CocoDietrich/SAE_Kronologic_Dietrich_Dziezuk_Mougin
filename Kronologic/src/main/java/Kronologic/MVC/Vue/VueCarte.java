package Kronologic.MVC.Vue;

import Kronologic.Jeu.Elements.Pion;
import Kronologic.Jeu.Images;
import Kronologic.MVC.Controleur.ControleurChoixCarte;
import Kronologic.MVC.Modele.ModeleJeu;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.*;
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
import java.util.Arrays;
import java.util.List;

import static Kronologic.MVC.Vue.VueAccueil.creerBouton;
import static Kronologic.MVC.Vue.VueTableau.compterOccurrencesRegex;

public class VueCarte extends BorderPane implements Observateur {

    public Button retour;
    public Button regle;
    public Button faireDeduction;
    public Button poserQuestion;
    public Button demanderIndice;
    public Button changerAffichage;
    public Button deductionIA;
    public Button filmJoueur;
    public Button filmRealite;
    public TextArea historique;
    public CheckBox hypothese;
    public CheckBox absence;
    public List<Polygon> zonesDeJeu = new ArrayList<>();
    public List<Polygon> zonesContenantPions = new ArrayList<>();
    public List<Pion> pions = new ArrayList<>();

    public VueCarte(ModeleJeu modeleJeu) {
        super();
        afficher(modeleJeu);
    }

    public void afficher(ModeleJeu modeleJeu) {
        this.setStyle("-fx-background-color: #800000;");

        HBox retourBox = afficherRetour();
        List<CheckBox> presenceAbsence = afficherPresenceAbsence();

        HBox films = afficherFilm();
        films.setAlignment(Pos.CENTER);

        // Zone centrale
        HBox centreBox = new HBox(30);
        centreBox.getChildren().addAll(presenceAbsence.get(0), films, presenceAbsence.get(1));
        centreBox.setAlignment(Pos.CENTER);

        StackPane regle = afficherRegle();
        HBox regleBox = new HBox();
        regleBox.getChildren().add(regle);
        regleBox.setAlignment(Pos.TOP_RIGHT);

        BorderPane topPane = new BorderPane();
        topPane.setLeft(retourBox);
        topPane.setCenter(centreBox);
        topPane.setRight(regleBox);

        this.setTop(topPane);
        this.setCenter(afficherMilieu(modeleJeu));
        this.setBottom(afficherBoutonsBas());

    }

    public BorderPane afficherMilieu(ModeleJeu modeleJeu) {
        // Création du BorderPane
        BorderPane grille = new BorderPane();
        grille.setPadding(new Insets(10)); // Marges autour du BorderPane

        // Récupération des cartes (partie haute et partie basse)
        List<HBox> cartes = afficherCarte(modeleJeu); // La liste contient deux HBox (haut et bas)
        HBox cartesHaut = cartes.get(0);
        cartesHaut.setAlignment(Pos.CENTER);
        HBox cartesBas = cartes.get(1);
        cartesBas.setAlignment(Pos.CENTER);

        // Récupération des boutons (optionsDroite)
        VBox optionsDroite = afficherBoutonsDroite();
        optionsDroite.setAlignment(Pos.CENTER_RIGHT);

        // Récupération de l'historique
        historique = afficherHistorique();
        historique.setWrapText(true);

        // Création du HBox central
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(50);

        // Ajout de l'historique
        hBox.getChildren().add(historique);

        // Création des pions et des options supplémentaires
        VBox pions = new VBox(10);
        HBox pionsPersonnages = afficherPionsPersonnages();
        HBox hboxBas = new HBox(10);
        ImageView pionNombre = afficherPionNombre();
        hypothese = afficherHypothese();
        absence = afficherAbsence();
        CheckBox masquerHypothese = afficherMasquerHypothese();
        masquerHypothese.setAlignment(Pos.CENTER);
        hboxBas.getChildren().addAll(hypothese, pionNombre, absence);
        hboxBas.setAlignment(Pos.CENTER);

        pions.getChildren().addAll(pionsPersonnages, hboxBas, masquerHypothese);
        pions.setAlignment(Pos.CENTER);
        HBox.setHgrow(pions, Priority.ALWAYS);
        hBox.getChildren().add(pions);

        // Ajout des boutons à droite
        hBox.getChildren().add(optionsDroite);

        // Forcer hBox à s'étendre sur toute la largeur
        hBox.setMaxWidth(Double.MAX_VALUE);

        // Définir le Top, Center et Bottom du BorderPane
        grille.setTop(cartesHaut);
        grille.setCenter(hBox);
        grille.setBottom(cartesBas);

        return grille;
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

        HBox hBoxBas = new HBox(5);
        hBoxBas.setSpacing(150);
        hBoxBas.setAlignment(Pos.CENTER);

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

            // Style pour les voir
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
                    pionDeplace.setFitHeight(47.5);
                    pionDeplace.setFitWidth(47.5);
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
                System.out.println("Sous-zone occupée : " + sousZoneOccupee);

                if (sousZoneOccupee) {
                    String nomLieu = polygon.getUserData().toString().split("-")[1];
                    String nomTemps = polygon.getUserData().toString().split("-")[0];
                    System.out.println("Nom lieu : " + nomLieu + " - Nom temps : " + nomTemps);
                    if (zonesContenantPions.getLast().getUserData() != zonesContenantPions.get(zonesContenantPions.size() - 2).getUserData()) {
                        zonesContenantPions.removeLast();
                    }
                    Polygon sousZoneLibre = trouverSousZoneLibre(nomLieu, nomTemps);
                    if (sousZoneLibre != null) {
                        System.out.println("Data sous-zone libre : " + sousZoneLibre.getUserData());

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
                        System.out.println("Aucune sous-zone libre trouvée");
                        return;
                    }
                } else {
                    // Positionner le pion au centre de la zone où il a été déposé
                    Point2D point = polygon.localToScene(polygon.getBoundsInLocal().getCenterX(), polygon.getBoundsInLocal().getCenterY());
                    pionDeplace.setLayoutX(point.getX() - pionDeplace.getFitWidth() / 2);
                    pionDeplace.setLayoutY(point.getY() - pionDeplace.getFitHeight() / 2);
                }

                VueCarte root = this;
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
                    new ControleurChoixCarte(modeleJeu).handle(e);
                    e.consume();
                });

                event.setDropCompleted(true);
            } else {
                event.setDropCompleted(false);
            }
            event.consume();
        });

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


    private HBox afficherPionsPersonnages() {
        HBox pionsPersonnages = new HBox(15);
        pionsPersonnages.setAlignment(Pos.CENTER);
        String[] cheminsPions = {
                Images.Personnages.PERSONNAGE1.getUrl(),
                Images.Personnages.PERSONNAGE2.getUrl(),
                Images.Personnages.PERSONNAGE3.getUrl(),
                Images.Personnages.PERSONNAGE4.getUrl(),
                Images.Personnages.PERSONNAGE5.getUrl(),
                Images.Personnages.PERSONNAGE6.getUrl()
        };

        for (String chemin : cheminsPions) {
            Pion pion = new Pion(null, chemin);
            pion.setFitHeight(60);
            pion.setFitWidth(60);
            pion.setPreserveRatio(true);
            pion.setStyle("-fx-cursor: hand;");
            pion.setId(chemin);

            // Activation du Drag & Drop
            pion.setOnDragDetected(event -> {
                Dragboard db = pion.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putImage(pion.getImage());
                db.setContent(content);
                event.consume();
            });

            pion.setOnDragDone(event -> event.consume());

            pionsPersonnages.getChildren().add(pion);
            pions.add(pion);
        }
        return pionsPersonnages;
    }

    private ImageView afficherPionNombre() {
        // Dossier contenant les images des nombres
        String[] imagesPionNombre = {
                Images.Nombre.NOMBREX.getUrl(),
                Images.Nombre.NOMBRE0.getUrl(),
                Images.Nombre.NOMBRE1.getUrl(),
                Images.Nombre.NOMBRE2.getUrl(),
                Images.Nombre.NOMBRE3.getUrl(),
                Images.Nombre.NOMBRE4.getUrl(),
                Images.Nombre.NOMBRE5.getUrl()
        };

        // Création de l'image par défaut (non modifiée)
        Pion pionNombre = new Pion(null, imagesPionNombre[0]);
        pionNombre.setFitWidth(90);
        pionNombre.setFitHeight(90);
        pionNombre.setPreserveRatio(true);
        pionNombre.setStyle("-fx-cursor: hand;");
        pionNombre.setId(imagesPionNombre[0]);

        // Ajout du double-clic pour changer le nombre
        pionNombre.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double-clic détecté
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Modifier le pion de nombre");
                dialog.setHeaderText("Entrer un chiffre entre 0 et 5");
                dialog.setContentText("Nouveau chiffre :");

                dialog.showAndWait().ifPresent(input -> {
                    try {
                        int value = Integer.parseInt(input); // Vérifie que c'est un entier
                        if (value >= 0 && value <= 5) {
                            // Mise à jour de l'image du pion
                            pionNombre.setImage(new Image(imagesPionNombre[value + 1]));
                            pionNombre.setUserData(value);
                            pionNombre.setId(imagesPionNombre[value + 1]);
                        } else {
                            showAlert("Le nombre doit être entre 0 et 5 !");
                        }
                    } catch (NumberFormatException ex) {
                        showAlert("Veuillez entrer un chiffre valide !");
                    }
                });
            }
        });

        // Activation du Drag & Drop uniquement si la valeur est valide
        pionNombre.setOnDragDetected(event -> {
            // Vérifie si le nombre est défini (non null)
            if (pionNombre.getUserData() instanceof Integer value && value >= 0 && value <= 6) {
                Dragboard db = pionNombre.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putImage(pionNombre.getImage()); // Transfère l'image actuelle
                db.setContent(content);
            } else {
                showAlert("Veuillez d'abord définir une valeur entre 0 et 6 !");
            }
            event.consume();
        });

        pionNombre.setOnDragDone(Event::consume);
        pions.add(pionNombre);
        return pionNombre;
    }

    // Méthode pour afficher une alerte en cas d'erreur
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public CheckBox afficherHypothese() {
        CheckBox hypothese = new CheckBox("Hypothèse");
        hypothese.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 12px;");
        return hypothese;
    }

    public CheckBox afficherAbsence() {
        CheckBox absence = new CheckBox("Absence");
        absence.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 12px;");
        return absence;
    }

    public CheckBox afficherMasquerHypothese(){
        CheckBox masquerHypothese = new CheckBox("Masquer les hypothèses");
        masquerHypothese.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 12px;");
        return masquerHypothese;
    }

    public HBox afficherBoutonsBas() {
        // ======== Bas : Boutons d'action ========
        HBox boutonsBas = new HBox();
        boutonsBas.setAlignment(Pos.CENTER);
        boutonsBas.setSpacing(300);
        boutonsBas.setPadding(new Insets(0, 20, 50, 20));

        faireDeduction = creerBouton("Faire une déduction");
        poserQuestion = creerBouton("Poser une question");
        demanderIndice = creerBouton("Demander un indice");

        boutonsBas.getChildren().addAll(faireDeduction, poserQuestion, demanderIndice);

        return boutonsBas;
    }

    public VBox afficherBoutonsDroite() {
        VBox optionsDroite = new VBox(20);
        optionsDroite.setAlignment(Pos.CENTER);
        optionsDroite.setPadding(new Insets(20));

        changerAffichage = creerBouton("Changer affichage");
        deductionIA = creerBouton("Déduction de l'IA");

        optionsDroite.getChildren().addAll(changerAffichage, deductionIA);

        return optionsDroite;
    }

    public static TextArea afficherHistorique() {
        // Création d'une zone de texte non éditable
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);

        // Définir une largeur et une hauteur préférées, avec une taille minimale
        textArea.setMaxWidth(400); // Largeur maximale
        textArea.setMaxHeight(200); // Hauteur maximale
        textArea.setMinWidth(200);  // Largeur minimale (permet de réduire la taille)
        textArea.setMinHeight(80);  // Hauteur minimale (permet de réduire la taille)

        // Ajout de la zone de texte dans un ScrollPane
        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToWidth(true); // Le ScrollPane s'adapte à la largeur de la zone de texte
        scrollPane.setFitToHeight(true); // S'adapte aussi à la hauteur

        // Personnalisation du ScrollPane
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Barre horizontale si nécessaire
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS); // Barre verticale toujours visible

        return textArea;
    }

    public StackPane afficherRegle() {
        // Création du bouton sans texte
        regle = new Button();
        regle.setId("boutonRegle");

        // Ajout de l'image (icône du livre)
        Image regleImage = new Image("file:img/regle.png");
        ImageView imageViewRegle = new ImageView(regleImage);
        imageViewRegle.setPreserveRatio(true);
        imageViewRegle.setFitHeight(30); // Ajustez la taille si nécessaire
        regle.setGraphic(imageViewRegle);

        // Application du style CSS pour un quart de cercle avec la bonne orientation et couleur
        regle.setStyle("""
            -fx-background-color: #FFCC66; /* Même couleur que les boutons */
            -fx-background-radius: 0 0 0 100px; /* Quart de cercle (haut droit) */
            -fx-padding: 20; /* Taille du bouton (ajustez selon vos besoins) */
            -fx-border-radius: 0 0 100px 0; /* Coin arrondi */
        """);

        // Effets au survol de la souris
        regle.setOnMouseEntered(e -> {
            regle.setStyle(
                    "-fx-background-color: #E6B85C; " +  // Changement de couleur
                            "-fx-text-fill: #800000; " +  // Couleur du texte (si nécessaire)
                            "-fx-background-radius: 0 0 0 100px; " +  // Quart de cercle haut droit
                            "-fx-padding: 20;"  // Agrandir le bouton
            );
        });

        regle.setOnMouseExited(e -> {
            regle.setStyle(
                    "-fx-background-color: #FFCC66; " +  // Couleur d'origine
                            "-fx-text-fill: #800000; " +  // Couleur du texte (si nécessaire)
                            "-fx-background-radius: 0 0 0 100px; " +  // Quart de cercle haut droit
                            "-fx-padding: 20;"  // Taille d'origine du bouton
            );
        });

        // Positionnement dans le coin supérieur droit
        StackPane stackPane = new StackPane(regle);
        stackPane.setAlignment(Pos.TOP_RIGHT);

        return stackPane;
    }

    public HBox afficherRetour(){
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
        retour.setStyle("-fx-background-color: transparent;");

        retour.setOnMouseEntered(e -> {
            retour.setStyle("-fx-background-color: #800000; " +
                    "-fx-cursor: hand;"); // Agrandir le bouton
        });

        retour.setOnMouseExited(e -> {
            retour.setStyle("-fx-background-color: #800000; " +
                    "-fx-cursor: hand;");
        });

        // Création de la zone pour le bouton retour (alignée à gauche)
        HBox retourBox = new HBox();
        retourBox.getChildren().add(retour);
        retourBox.setAlignment(Pos.CENTER_LEFT);
        retourBox.setPadding(new Insets(2, 0, 2, 0));

        return retourBox;
    }

    public HBox afficherFilm() {
        // Boutons de film (joueur et partie)
        filmJoueur = creerBouton("Film du joueur");
        filmRealite = creerBouton("Film de la partie");

        Image film = new Image("file:img/film.png");
        ImageView imageViewFilm = new ImageView(film);
        imageViewFilm.setPreserveRatio(true);
        imageViewFilm.setFitHeight(20);

        ImageView imageViewFilm2 = new ImageView(film);
        imageViewFilm2.setPreserveRatio(true);
        imageViewFilm2.setFitHeight(20);

        filmJoueur.setGraphic(imageViewFilm);
        filmRealite.setGraphic(imageViewFilm2);

        // Groupe des films
        HBox films = new HBox(10);
        films.setSpacing(50);
        films.getChildren().addAll(filmJoueur, filmRealite);

        return films;
    }

    public List<CheckBox> afficherPresenceAbsence() {
        CheckBox afficherPresences = new CheckBox("Afficher les présences");
        afficherPresences.setStyle("-fx-text-fill: #FFCC66; -fx-font-size: 12px;");

        CheckBox afficherAbsences = new CheckBox("Afficher les absences");
        afficherAbsences.setStyle("-fx-text-fill: #FFCC66; -fx-font-size: 12px;");

        return List.of(afficherPresences, afficherAbsences);
    }


    @Override
    public void actualiser() {
        // On actualise l'historique des indices en ajoutant le dernier indice découvert
        if (!ModeleJeu.getPartie().getIndicesDecouverts().isEmpty()) {
            if (historique.getText().isEmpty()) {
                historique.setText("Tour 1 :\n" + ModeleJeu.getPartie().getIndicesDecouverts().getLast() + "\n");
            } else if (ModeleJeu.getPartie().getNbQuestion() != compterOccurrencesRegex(historique.getText(), "Tour")) {
                historique.setText("Tour " + ModeleJeu.getPartie().getNbQuestion()
                        + " :\n" + ModeleJeu.getPartie().getIndicesDecouverts().getLast()
                        + "\n" + historique.getText());
            }
        }

        // On actualise les pions


    }
}
