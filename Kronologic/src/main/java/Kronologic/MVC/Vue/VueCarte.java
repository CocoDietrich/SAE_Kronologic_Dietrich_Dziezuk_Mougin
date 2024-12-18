package Kronologic.MVC.Vue;

import Kronologic.MVC.Modele.ModeleJeu;
import javafx.event.Event;
import javafx.geometry.Insets;
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
import java.util.List;

import static Kronologic.MVC.Vue.VueAccueil.creerButton;

public class VueCarte extends BorderPane implements Observateur {

    public Button retour;
    public Button regle;
    public Button faireDeduction;
    public Button poserQuestion;
    public Button demanderIndice;
    public Button changerAffichage;
    public Button deductionIA;
    public TextArea historique;
    public CheckBox hypothese;
    public CheckBox absence;
    public List<Polygon> zonesDeJeu;
    public List<Pion> pions = new ArrayList<>();

    public VueCarte() {
        super();
        afficher();
    }

    public void afficher() {
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
        this.setCenter(afficherMilieu());
        this.setBottom(afficherBoutonsBas());

    }

    public BorderPane afficherMilieu() {
        // Création du BorderPane
        BorderPane grille = new BorderPane();
        grille.setPadding(new Insets(10)); // Marges autour du BorderPane

        // Récupération des cartes (partie haute et partie basse)
        List<HBox> cartes = afficherCarte(); // La liste contient deux HBox (haut et bas)
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

    public List<HBox> afficherCarte() {
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
            List<Polygon> zones = creerZone("Temps " + (i+1));

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

    public List<Polygon> creerZone(String temps) {
        // Zone 1 (Salle Orange à gauche)
        Polygon zone1 = creerLieu(new double[]{
                0, 0,  // Coin supérieur gauche
                30, 0, // Coin supérieur droit
                30, 110, // Coin inférieur droit
                0, 110   // Coin inférieur gauche
        }, temps + " - Grand foyer");

        // Zone 2 (Salle Bleue au centre gauche)
        Polygon zone2 = creerLieu(new double[]{
                0, 0,  // Coin supérieur gauche
                50, 0,  // Coin supérieur droit
                50, 80,  // Coin inférieur droit
                0, 80   // Coin inférieur gauche
        }, temps + " - Grand escalier");

        // Zone 3 (Grande Scène Rouge)
        Polygon zone3 = creerLieu(new double[]{
                0, 0,  // Coin supérieur gauche
                50, 0,  // Coin supérieur droit
                50, 150,  // Coin inférieur droit
                0, 150   // Coin inférieur gauche
        }, temps + " - Salle");

        // Zone 4 (Couloir Sombre milieu droit)
        Polygon zone4 = creerLieu(new double[]{
                0, 0,  // Coin supérieur gauche
                40, 0,  // Coin supérieur droit
                40, 110,  // Coin inférieur droit
                0, 110   // Coin inférieur gauche
        }, temps + " - Scène");

        // Zone 5 (Salle Bleue en haut à droite)
        Polygon zone5 = creerLieu(new double[]{
                0, 0,  // Coin supérieur gauche
                60, 0,  // Coin supérieur droit
                60, 40, // Coin inférieur droit
                0, 40  // Coin inférieur gauche
        }, temps + " - Foyer du chant");

        // Zone 6 (Salle Dorée en bas à droite)
        Polygon zone6 = creerLieu(new double[]{
                0, 0,  // Coin supérieur gauche
                60, 0,  // Coin supérieur droit
                60, 70,  // Coin inférieur droit
                0, 70   // Coin inférieur gauche
        }, temps + " - Foyer de la danse");

        zonesDeJeu = List.of(zone1, zone2, zone3, zone4, zone5, zone6);
        return List.of(zone1, zone2, zone3, zone4, zone5, zone6);
    }

    private Polygon creerLieu(double[] points, String zoneName) {
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
                // TODO: Vérifier le type de pion (personnage ou nombre)
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
                    }
                    e.consume();
                });

                // Ajouter le pion dans le root principal (VueCarte)
                VueCarte root = this;
                root.getChildren().add(pionDeplace);

                // Positionner le pion là où le curseur a été lâché
                double sceneX = event.getSceneX();
                double sceneY = event.getSceneY();
                javafx.geometry.Point2D dropPoint = root.sceneToLocal(sceneX, sceneY);

                pionDeplace.setLayoutX(dropPoint.getX() - pionDeplace.getFitWidth() / 2);
                pionDeplace.setLayoutY(dropPoint.getY() - pionDeplace.getFitHeight() / 2);

                event.setDropCompleted(true);

                // Ajouter le pion à la liste des pions
                pions.add(pionDeplace);
            } else {
                event.setDropCompleted(false);
            }
            event.consume();
        });

        return polygon;
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

        faireDeduction = creerButton("Faire une déduction");
        poserQuestion = creerButton("Poser une question");
        demanderIndice = creerButton("Demander un indice");

        boutonsBas.getChildren().addAll(faireDeduction, poserQuestion, demanderIndice);

        return boutonsBas;
    }

    public VBox afficherBoutonsDroite() {
        VBox optionsDroite = new VBox(20);
        optionsDroite.setAlignment(Pos.CENTER);
        optionsDroite.setPadding(new Insets(20));

        changerAffichage = creerButton("Changer affichage");
        deductionIA = creerButton("Déduction de l'IA");

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

    public static HBox afficherFilm() {
        // Boutons de film (joueur et partie)
        Button filmJoueur = creerButton("Film du joueur");
        Button filmPartie = creerButton("Film de la partie");

        Image film = new Image("file:img/film.png");
        ImageView imageViewFilm = new ImageView(film);
        imageViewFilm.setPreserveRatio(true);
        imageViewFilm.setFitHeight(20);

        ImageView imageViewFilm2 = new ImageView(film);
        imageViewFilm2.setPreserveRatio(true);
        imageViewFilm2.setFitHeight(20);

        filmJoueur.setGraphic(imageViewFilm);
        filmPartie.setGraphic(imageViewFilm2);

        // Groupe des films
        HBox films = new HBox(10);
        films.setSpacing(50);
        films.getChildren().addAll(filmJoueur, filmPartie);

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
        if (historique.getText().isEmpty()) {
            historique.setText("Tour 1 :\n" + ModeleJeu.getPartie().getIndicesDecouverts().getLast() + "\n");
        } else {
            historique.setText("Tour " + ModeleJeu.getPartie().getNbQuestion() + " :\n" + ModeleJeu.getPartie().getIndicesDecouverts().getLast() + "\n" + historique.getText());
        }
    }
}
