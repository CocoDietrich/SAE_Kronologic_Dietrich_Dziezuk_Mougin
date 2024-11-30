package Kronologic.MVC.Vue;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
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
        // Création du GridPane
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
        TextArea historique = afficherHistorique();
        historique.setWrapText(true); // Texte ajusté

        HBox hBox = new HBox();
        hBox.getChildren().addAll(historique, optionsDroite);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(800);

        grille.setTop(cartesHaut); // Partie haute des cartes

        grille.setCenter(hBox); // Partie basse des cartes

        grille.setBottom(cartesBas);



        // Retourner la grille
        return grille;
    }


    public List<HBox> afficherCarte() {
        Image carte = new Image("file:src/main/resources/Icons/plateau.png");

        List<Image> Temps = List.of(
                new Image("file:src/main/resources/Icons/temps1.png"),
                new Image("file:src/main/resources/Icons/temps2.png"),
                new Image("file:src/main/resources/Icons/temps3.png"),
                new Image("file:src/main/resources/Icons/temps4.png"),
                new Image("file:src/main/resources/Icons/temps5.png"),
                new Image("file:src/main/resources/Icons/temps6.png")
        );

        HBox hBoxHaut = new HBox(5);
        hBoxHaut.setSpacing(120);
        HBox hBoxBas = new HBox(5);
        hBoxBas.setSpacing(120);

        // Ajout des six cartes 3 en lignes pour 2 en colonnes
        for (int i = 0; i < 6; i++) {
            VBox vBox = new VBox(5);
            // carte
            ImageView imageViewCarte = new ImageView(carte);
            imageViewCarte.setPreserveRatio(true);
            imageViewCarte.setFitHeight(220);

            // temps
            ImageView imageViewTemps = new ImageView(Temps.get(i));
            imageViewTemps.setPreserveRatio(true);
            imageViewTemps.setFitHeight(40);

            vBox.getChildren().addAll(imageViewTemps, imageViewCarte);
            vBox.setAlignment(Pos.CENTER);

            if (i < 3){
                hBoxHaut.getChildren().add(vBox);
            }
            else {
                hBoxBas.getChildren().add(vBox);
            }
        }

        return List.of(hBoxHaut, hBoxBas);
    }

    public void afficherPions() {
        // TODO : itération 2
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



    public TextArea afficherHistorique() {
        // Création d'une zone de texte non éditable
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefSize(400, 150);


        // Ajout de la zone de texte dans un ScrollPane
        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToWidth(true); // Le scrollpane s'adapte à la largeur de la zone de texte
        scrollPane.setFitToHeight(true); // S'adapte aussi à la hauteur

        // Personnalisation du ScrollPane
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Barre horizontale si nécessaire
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS); // Barre verticale toujours visible

        return textArea;
    }


    public StackPane afficherRegle() {
        // Création du bouton sans texte
        Button regle = new Button();
        regle.setId("boutonRegle");

        // Ajout de l'image (icône du livre)
        Image regleImage = new Image("file:src/main/resources/Icons/regle.png");
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
        Image flecheRetour = new Image("file:src/main/resources/Icons/flecheRetour.png");
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

        // Création de la zone pour le bouton retour (alignée à gauche)
        HBox retourBox = new HBox();
        retourBox.getChildren().add(retour);
        retourBox.setAlignment(Pos.CENTER_LEFT);
        retourBox.setPadding(new Insets(10));

        return retourBox;
    }

    public HBox afficherFilm() {
        // Boutons de film (joueur et partie)
        Button filmJoueur = creerButton("Film du joueur");
        Button filmPartie = creerButton("Film de la partie");

        Image film = new Image("file:src/main/resources/Icons/film.png");
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

    }
}
