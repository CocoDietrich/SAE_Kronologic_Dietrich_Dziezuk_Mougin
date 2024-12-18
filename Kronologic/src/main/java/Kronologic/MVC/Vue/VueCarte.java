package Kronologic.MVC.Vue;

import Kronologic.MVC.Modele.ModeleJeu;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.util.List;

import static Kronologic.MVC.Vue.VueAccueil.creerButton;

public class VueCarte extends BorderPane implements Observateur {

    public Button retour;
    public Button faireDeduction;
    public Button poserQuestion;
    public Button demanderIndice;
    public Button changerAffichage;
    public Button deductionIA;
    public TextArea historique;

    public VueCarte() {
        super();

        this.setStyle("-fx-background-color: #800000;");

        HBox retourBox = afficherRetour();

        List<CheckBox> presenceAbsence = afficherPresenceAbsence();

        HBox films = afficherFilm();
        films.setAlignment(Pos.CENTER);

        // Création de la zone centrale (checkboxes et boutons alignés au centre)
        HBox centreBox = new HBox(30);
        centreBox.getChildren().addAll(presenceAbsence.get(0), films, presenceAbsence.get(1));
        centreBox.setAlignment(Pos.CENTER);


        StackPane regle = afficherRegle();
        HBox regleBox = new HBox();
        regleBox.getChildren().add(regle);
        regleBox.setAlignment(Pos.TOP_RIGHT);

        // Combinaison des deux zones dans un conteneur principal
        BorderPane topPane = new BorderPane();
        topPane.setLeft(retourBox);
        topPane.setCenter(centreBox);
        topPane.setRight(regleBox);

        // Positionner la vue supérieure dans la scène
        this.setTop(topPane);

        // Positionner la vue centrale
        this.setCenter(afficherMilieu());

        // Affichage des boutons d'action
        this.setBottom(afficherBoutonsBas());
    }

    public void afficher(Stage stage) {
        // TODO
    }

    public BorderPane afficherMilieu() {
        // Création du BorderPane
        BorderPane grille = new BorderPane();
        grille.setPadding(new Insets(10)); // Marges autour du GridPane

        // Récupération des cartes (partie haute et partie basse)
        List<HBox> cartes = afficherCarte(); // La liste contient deux HBox (haut et bas)
        HBox cartesHaut = cartes.get(0); // Partie haute des cartes
        cartesHaut.setAlignment(Pos.CENTER);
        HBox cartesBas = cartes.get(1); // Partie basse des cartes
        cartesBas.setAlignment(Pos.CENTER);

        // Récupération des boutons (optionsDroite)
        VBox optionsDroite = afficherBoutonsDroite();
        optionsDroite.setAlignment(Pos.CENTER_RIGHT);

        // Récupération de l'historique
        historique = afficherHistorique();
        historique.setWrapText(true); // Texte ajusté

        // Création du HBox central pour contenir l'historique à gauche et les optionsDroite à droite
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER); // Alignement général au centre

        // Ajout de l'historique à gauche
        hBox.getChildren().add(historique); // Ajout de l'historique

        VBox pions = afficherPions();
        //HBox.setHgrow(pions, Priority.ALWAYS); // L'espace vide prend toute la place restante
        hBox.getChildren().add(pions); // Ajout de la zone vide

        // Ajout des optionsDroite à droite
        hBox.getChildren().add(optionsDroite); // Ajout des boutons à droite

        // Assurez-vous que le HBox occupe toute la largeur disponible
        hBox.setMaxWidth(Double.MAX_VALUE); // Prendre toute la largeur

        // Définir le Top, Center et Bottom du BorderPane
        grille.setTop(cartesHaut); // Partie haute des cartes
        grille.setCenter(hBox);   // Partie centrale avec l'historique à gauche et les optionsDroite à droite
        grille.setBottom(cartesBas); // Partie basse des cartes

        // Retourner la grille
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

    private VBox afficherPions() {
        VBox pionsVBox = new VBox(10); // Alignement vertical avec espacement
        pionsVBox.setAlignment(Pos.CENTER);

        // Pions des personnages
        HBox pionsPersonnages = new HBox(15);
        pionsPersonnages.setAlignment(Pos.CENTER);
        String[] cheminsPions = {
                "file:img/Aventurière.png",
                "file:img/Baronne.png",
                "file:img/Chauffeur.png",
                "file:img/Détective.png",
                "file:img/Journaliste.png",
                "file:img/Servante.png"
        };

        for (String chemin : cheminsPions) {
            ImageView pion = new ImageView(new Image(chemin));
            pion.setFitHeight(50); // Taille uniforme plus grande
            pion.setFitWidth(50);  // Taille uniforme plus grande
            pion.setPreserveRatio(true);

            // Changer le curseur en main au survol
            pion.setOnMouseEntered(e -> pion.setCursor(javafx.scene.Cursor.HAND));
            pion.setOnMouseExited(e -> pion.setCursor(javafx.scene.Cursor.DEFAULT));

            pionsPersonnages.getChildren().add(pion);
        }

        // Création du pion de nombre (affiché en dessous des pions de personnages)
        Button pionNombre = new Button("X");
        pionNombre.setId("pionNombre");
        pionNombre.setStyle("-fx-background-color: #464545; -fx-text-fill: #ffffff; -fx-font-size: 30px;");
        pionNombre.setPrefWidth(45);
        pionNombre.setPrefHeight(45);
        pionNombre.setStyle("-fx-background-radius: 50%;");

        // Changer le curseur pour le pion de nombre
        pionNombre.setOnMouseEntered(e -> pionNombre.setCursor(javafx.scene.Cursor.HAND));
        pionNombre.setOnMouseExited(e -> pionNombre.setCursor(javafx.scene.Cursor.DEFAULT));

        // Ajout des pions dans le VBox principal
        pionsVBox.getChildren().addAll(pionsPersonnages, pionNombre);

        return pionsVBox;
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
        }, temps + " - Grand Foyer");

        // Zone 2 (Salle Bleue au centre gauche)
        Polygon zone2 = creerLieu(new double[]{
                0, 0,  // Coin supérieur gauche
                50, 0,  // Coin supérieur droit
                50, 80,  // Coin inférieur droit
                0, 80   // Coin inférieur gauche
        }, temps + " - Grand Escalier");

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
        }, temps + " - Foyer du Chant");

        // Zone 6 (Salle Dorée en bas à droite)
        Polygon zone6 = creerLieu(new double[]{
                0, 0,  // Coin supérieur gauche
                60, 0,  // Coin supérieur droit
                60, 70,  // Coin inférieur droit
                0, 70   // Coin inférieur gauche
        }, temps + " - Foyer de la Danse");

        return List.of(zone1, zone2, zone3, zone4, zone5, zone6);
    }

    private Polygon creerLieu(double[] points, String zoneName) {

        Polygon polygon = new Polygon(points);
        polygon.setUserData(zoneName); // Nom de la zone
        polygon.setFill(Color.TRANSPARENT); // Couleur transparente par défaut

        polygon.setOnMouseEntered(event -> {
            polygon.setFill(Color.LIGHTGRAY);
            System.out.println("Vous survolez la zone : " + polygon.getUserData());
        }); // Survol
        polygon.setOnMouseExited(event -> polygon.setFill(Color.TRANSPARENT)); // Quitter

        return polygon;
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
        textArea.setPrefWidth(450); // Largeur préférée
        textArea.setPrefHeight(100); // Hauteur préférée
        textArea.setMinWidth(200);  // Largeur minimale (permet de réduire la taille)
        textArea.setMinHeight(50);  // Hauteur minimale (permet de réduire la taille)

        // Ajout de la zone de texte dans un ScrollPane
        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToWidth(true); // Le ScrollPane s'adapte à la largeur de la zone de texte
        scrollPane.setFitToHeight(true); // S'adapte aussi à la hauteur

        // Personnalisation du ScrollPane
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Barre horizontale si nécessaire
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS); // Barre verticale toujours visible

        return textArea;
    }



    public static StackPane afficherRegle() {
        // Création du bouton sans texte
        Button regle = new Button();
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
        afficherPresences.setStyle("-fx-text-fill: #FFCC66; -fx-font-size: 16px;");

        CheckBox afficherAbsences = new CheckBox("Afficher les absences");
        afficherAbsences.setStyle("-fx-text-fill: #FFCC66; -fx-font-size: 16px;");

        return List.of(afficherPresences, afficherAbsences);
    }

    public void afficherHypothese() {
        // TODO : itération 2
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
