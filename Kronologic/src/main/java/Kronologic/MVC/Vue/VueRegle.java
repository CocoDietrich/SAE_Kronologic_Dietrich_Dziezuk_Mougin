package Kronologic.MVC.Vue;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class VueRegle extends GridPane implements Observateur {

    public Button retour;
    public Button demanderIndice;
    public Button poserQuestion;
    public Button faireDeduction;
    public Button deductionIA;
    public Button changerAffichage;
    public CheckBox afficherPresences;
    public CheckBox afficherAbsences;

    public VueRegle() {
        super();
        afficher();
    }

    public void afficher() {
        this.setStyle("-fx-background-color: #800000; -fx-padding: 30;");
        this.setHgap(50);
        this.setVgap(50);

        // Flèche de retour
        HBox retourBox = afficherRetour();
        this.add(retourBox, 0, 0);

        // Section gauche : Boutons principaux
        VBox boutonsBox = creerBoutonsPrincipaux();
        GridPane.setHgrow(boutonsBox, Priority.ALWAYS);
        this.add(boutonsBox, 0, 1);

        // Section droite : Règles, checkboxes et actions secondaires
        VBox reglesEtActionsBox = creerReglesEtActions();
        GridPane.setHgrow(reglesEtActionsBox, Priority.ALWAYS);
        this.add(reglesEtActionsBox, 1, 1);
    }

    public VBox creerBoutonsPrincipaux() {
        VBox boutonsBox = new VBox(30);
        boutonsBox.setAlignment(Pos.TOP_CENTER);

        demanderIndice = new Button("Demander un indice");
        demanderIndice.setStyle("-fx-background-color: #f5a623; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");
        Text descriptionIndice = new Text("Permet de demander un indice. Cela vous posera automatiquement\nla meilleure question à poser.");
        descriptionIndice.setStyle("-fx-font-size: 16px; -fx-fill: white;");
        boutonsBox.getChildren().addAll(demanderIndice, descriptionIndice);

        poserQuestion = new Button("Poser une question");
        poserQuestion.setStyle("-fx-background-color: #f5a623; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");
        Text descriptionQuestion = new Text("En posant une question, vous allez avoir accès à un indice qui\nva vous permettre d’avancer dans votre raisonnement.");
        descriptionQuestion.setStyle("-fx-font-size: 16px; -fx-fill: white;");
        boutonsBox.getChildren().addAll(poserQuestion, descriptionQuestion);

        faireDeduction = new Button("Faire une déduction");
        faireDeduction.setStyle("-fx-background-color: #f5a623; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");
        Text descriptionDeduction = new Text("Vous permet d’essayer de trouver la solution. Attention, vous\navez le droit à une seule déduction !");
        descriptionDeduction.setStyle("-fx-font-size: 16px; -fx-fill: white;");
        boutonsBox.getChildren().addAll(faireDeduction, descriptionDeduction);

        return boutonsBox;
    }

    public VBox creerReglesEtActions() {
        VBox reglesEtActionsBox = new VBox(40);
        reglesEtActionsBox.setAlignment(Pos.TOP_CENTER);

        // Titre des règles
        HBox reglesHeader = new HBox(10);
        Text titreRegles = new Text("Règles particulières :");
        titreRegles.setStyle("-fx-fill: #f5a623; -fx-font-size: 22px; -fx-font-weight: bold;");
        Text iconeLivre = new Text("\uD83D\uDCDA"); // Icône de livre
        iconeLivre.setStyle("-fx-fill: #f5a623; -fx-font-size: 24px;");
        reglesHeader.getChildren().addAll(iconeLivre, titreRegles);
        reglesHeader.setAlignment(Pos.CENTER_LEFT);

        // Contenu des règles
        Text contenuRegles = new Text(
                "• Les Personnages se déplacent obligatoirement et d’un lieu maximum.\n" +
                        "• Les lieux sont reliés par des passages (en blanc)."
        );
        contenuRegles.setStyle("-fx-fill: white; -fx-font-size: 16px;");

        // Checkboxes centrées et alignées
        afficherPresences = new CheckBox("Afficher les présences");
        afficherPresences.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        afficherPresences.setSelected(true);

        afficherAbsences = new CheckBox("Afficher les absences");
        afficherAbsences.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        afficherAbsences.setSelected(true);

        HBox checkBoxesBox = new HBox(20, afficherPresences, afficherAbsences);
        checkBoxesBox.setAlignment(Pos.CENTER);

        // Actions secondaires
        VBox actionsBox = creerActionsSecondaires();

        reglesEtActionsBox.getChildren().addAll(reglesHeader, contenuRegles, checkBoxesBox, actionsBox);
        return reglesEtActionsBox;
    }


    public VBox creerActionsSecondaires() {
        VBox actionsBox = new VBox(30);
        actionsBox.setAlignment(Pos.TOP_CENTER);

        deductionIA = new Button("Déduction de l’IA");
        deductionIA.setStyle("-fx-background-color: #f5a623; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");
        Text descriptionIA = new Text("Vous permet d’afficher et de comparer vos déductions avec celle de l’IA.");
        descriptionIA.setStyle("-fx-font-size: 16px; -fx-fill: white;");
        actionsBox.getChildren().addAll(deductionIA, descriptionIA);

        changerAffichage = new Button("Changer affichage");
        changerAffichage.setStyle("-fx-background-color: #f5a623; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");
        Text descriptionAffichage = new Text("Vous permet de passer de l’affichage sous forme de cartes au tableau.");
        descriptionAffichage.setStyle("-fx-font-size: 16px; -fx-fill: white;");
        actionsBox.getChildren().addAll(changerAffichage, descriptionAffichage);

        return actionsBox;
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



    @Override
    public void actualiser() {
        // Méthode pour mettre à jour la vue si nécessaire
    }
}
