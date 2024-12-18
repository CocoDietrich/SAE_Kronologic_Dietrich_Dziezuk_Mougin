package Kronologic.MVC.Vue;

import Kronologic.MVC.Modele.ModeleJeu;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

import static Kronologic.MVC.Vue.VueAccueil.creerButton;
import static Kronologic.MVC.Vue.VueCarte.*;

public class VueTableau extends BorderPane implements Observateur {

    public Button retour;
    public Button faireDeduction;
    public Button poserQuestion;
    public Button demanderIndice;
    public Button changerAffichage;
    public Button deductionIA;
    public TextArea historique;
    public List<TextCase> listeCases = new ArrayList<>();

    public VueTableau() {
        super();
        this.setStyle("-fx-background-color: #800000;");

        HBox retourBox = afficherRetour();

        HBox films = afficherFilm();
        films.setAlignment(Pos.CENTER);

        // Création de la zone centrale (checkboxes et boutons alignés au centre)
        HBox centreBox = new HBox(30);
        centreBox.getChildren().add(films);
        centreBox.setAlignment(Pos.CENTER);

        StackPane regle = afficherRegle();
        HBox regleBox = new HBox();
        regleBox.getChildren().add(regle);
        regleBox.setAlignment(Pos.TOP_RIGHT);

        // Boutons
        VBox boutonsGauche = afficherBoutonsGauche();
        VBox boutonsDroite = afficherBoutonsDroite();

        boutonsGauche.setAlignment(Pos.CENTER_LEFT);
        boutonsDroite.setAlignment(Pos.CENTER_RIGHT);

        historique = new TextArea();
        historique.setWrapText(true); // Texte ajusté

        // Création du HBox central pour contenir l'historique à gauche et les optionsDroite à droite
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER); // Alignement général au centre

        // Ajout de l'historique à gauche
        hBox.getChildren().add(historique); // Ajout de l'historique

        HBox bottom = new HBox(30);
        bottom.getChildren().addAll(boutonsGauche, hBox, boutonsDroite);
        bottom.setAlignment(Pos.CENTER);

        // Combinaison des deux zones dans un conteneur principal
        BorderPane topPane = new BorderPane();
        topPane.setLeft(retourBox);
        topPane.setCenter(centreBox);
        topPane.setRight(regleBox);
        topPane.setBottom(bottom);

        this.setTop(topPane);

        // Affichage des tableaux
        List<GridPane> tableaux = afficherTableaux();

        HBox tableauxBox = new HBox(30);
        tableauxBox.setSpacing(200);
        tableauxBox.setAlignment(Pos.TOP_CENTER);
        for (GridPane tableau : tableaux) {
            tableauxBox.getChildren().add(tableau);
        }

        this.setCenter(tableauxBox);

    }

    public void afficher() {
        // TODO
    }

    public VBox afficherBoutonsGauche() {
        VBox boutonsGauche = new VBox(10);
        boutonsGauche.setAlignment(Pos.CENTER);
        boutonsGauche.setSpacing(20);

        changerAffichage = creerButton("Changer affichage");
        deductionIA = creerButton("Déduction de l'IA");
        demanderIndice = creerButton("Demander un indice");

        boutonsGauche.getChildren().addAll(changerAffichage, deductionIA, demanderIndice);

        return boutonsGauche;
    }

    public VBox afficherBoutonsDroite() {
        VBox boutonsDroite = new VBox(10);
        boutonsDroite.setAlignment(Pos.CENTER);
        boutonsDroite.setSpacing(20);

        faireDeduction = creerButton("Faire une déduction");
        poserQuestion = creerButton("Poser une question");

        boutonsDroite.getChildren().addAll(faireDeduction, poserQuestion);

        return boutonsDroite;
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


    public List<GridPane> afficherTableaux() {
        // Tableau Temps-Lieux
        GridPane tempsLieux = afficherTableauTempsLieu();

        // Tableau Temps-Personnage
        GridPane tempsPersonnages = afficherTableauTempsPersonnage();

        return List.of(tempsLieux, tempsPersonnages);
    }

    public GridPane afficherTableauTempsLieu(){
        GridPane tableau = creerTableauTemps();
        tableau.setAlignment(Pos.TOP_LEFT);
        tableau.setVgap(15);

        // On affiche les lieux en colonne
        for (int i = 0; i < 6; i++) {
            Image image = Images.Lieux.get(i);
            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(30);
            tableau.add(imageView, 0, i + 1);
        }

        List<String> elements = List.of("0", "1", "2", "3", "4", "5");
        // On ajoute les cases
        return creerCaseTableau(tableau, elements);
    }

    public GridPane afficherTableauTempsPersonnage(){
        GridPane tableau = creerTableauTemps();
        tableau.setAlignment(Pos.TOP_RIGHT);
        tableau.setVgap(12);


        for (int i = 0; i < 6; i++) {
            Image image = Images.Personnages.get(i);
            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(30);
            tableau.add(imageView, 0, i + 1);
        }

        List<String> elements = List.of("GF", "GE", "SA", "SC", "FD", "FC");
        // On ajoute les cases
        return creerCaseTableau(tableau, elements);
    }

    public GridPane creerTableauTemps(){
        GridPane tableau = new GridPane();
        tableau.setPadding(new Insets(10, 10, 10, 10));
        tableau.setHgap(40);

        // Partie Horizontal
        for (int i = 0; i < 6; i++) {
            Image image = Images.Temps.get(i);
            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(30);
            tableau.add(imageView, i + 1, 0);
        }

        return tableau;
    }

    private GridPane creerCaseTableau(GridPane tableau, List<String> elements) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                GridPane caseLettre = creerCase(elements, i + 1, j);
                tableau.add(caseLettre, i + 1, j + 1);
            }
        }

        return tableau;
    }

    private GridPane creerCase(List<String> elements, int temps, int lieuOuPersonnage){
        GridPane caseNumero = new GridPane();
        caseNumero.setHgap(5);
        caseNumero.setVgap(5);

        for (int i = 0; i < 6; i++) {
            TextCase text = new TextCase(elements.get(i));
            text.setFont(Font.font("Arial", 10));
            text.setFill(Color.LIGHTGRAY);
            text.setEtat("neutre");
            text.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
            text.setStyle("-fx-cursor: hand;");
            if (elements.contains("0")){
                text.setInfo("Nombre - " + temps + " - " + Images.Lieux.toString(lieuOuPersonnage));
            }
            else {
                text.setInfo("Lieu - " + temps + " - " + Images.Personnages.toString(lieuOuPersonnage));
            }
            if (i % 3 == 0) {
                caseNumero.add(text, 0, (int) Math.floor((float) i / 3));
            } else if (i % 3 == 1) {
                caseNumero.add(text, 1, (int) Math.floor((float) i / 3));
            } else {
                caseNumero.add(text, 2, (int) Math.floor((float) i / 3));
            }
            listeCases.add(text);
        }

        return caseNumero;
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
