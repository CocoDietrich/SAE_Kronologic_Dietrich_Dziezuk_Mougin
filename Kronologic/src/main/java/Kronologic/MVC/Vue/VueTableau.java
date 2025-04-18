package Kronologic.MVC.Vue;

import Kronologic.Jeu.Elements.*;
import Kronologic.Jeu.Enums.ImageLieux;
import Kronologic.Jeu.Enums.ImagePersonnages;
import Kronologic.Jeu.Enums.ImageTemps;
import Kronologic.MVC.Modele.ModeleJeu;
import Kronologic.MVC.TextCase;
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

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Kronologic.MVC.Controleur.ControleurChoixTableau.nomLieu;
import static Kronologic.MVC.Vue.VueAccueil.creerBouton;

// TODO : à vérifier
public class VueTableau extends BorderPane implements Observateur {

    private static final String CHANGER_AFFICHAGE = "Changer affichage";
    private static final String FAIRE_DEDUCTION = "Faire une déduction";
    private static final String POSER_QUESTION = "Poser une question";
    private static final String DEMANDER_INDICE = "Demander un indice";
    private static final String DEDUCTION_IA = "Déduction IA";

    private final static String FONT_FAMILY = "Arial";

    private Button retour;
    private Button regle;
    private Button faireDeduction;
    private Button poserQuestion;
    private Button demanderIndice;
    private Button changerAffichage;
    private Button deductionIA;
    private Button filmJoueur;
    private Button filmRealite;
    private TextArea historique;
    private final List<TextCase> listeCases = new ArrayList<>();
    private boolean temps1Instance = false;

    public VueTableau() {
        super();
        afficher();
    }

    public void afficher() {
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

    public VBox afficherBoutonsGauche() {
        VBox boutonsGauche = new VBox(10);
        boutonsGauche.setAlignment(Pos.CENTER);
        boutonsGauche.setSpacing(20);

        changerAffichage = creerBouton(CHANGER_AFFICHAGE);
        deductionIA = creerBouton(DEDUCTION_IA);
        demanderIndice = creerBouton(DEMANDER_INDICE);

        boutonsGauche.getChildren().addAll(changerAffichage, deductionIA, demanderIndice);

        return boutonsGauche;
    }

    public VBox afficherBoutonsDroite() {
        VBox boutonsDroite = new VBox(10);
        boutonsDroite.setAlignment(Pos.CENTER);
        boutonsDroite.setSpacing(20);

        faireDeduction = creerBouton(FAIRE_DEDUCTION);
        poserQuestion = creerBouton(POSER_QUESTION);

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

        retour.setOnMouseEntered(_ -> {
            retour.setStyle("-fx-background-color: #800000; " +
                    "-fx-cursor: hand;"); // Agrandir le bouton
        });

        retour.setOnMouseExited(_ -> retour.setStyle("-fx-background-color: #800000; " +
                "-fx-cursor: hand;"));

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
            Image image = ImageLieux.get(i);
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
            Image image = ImagePersonnages.get(i);
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
            Image image = ImageTemps.get(i);
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
            text.setFont(Font.font(FONT_FAMILY, 10));
            text.setFill(Color.LIGHTGRAY);
            text.setEtat("neutre");
            text.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
            text.setStyle("-fx-cursor: hand;");
            if (elements.contains("0")){
                text.setInfo("Nombre - " + temps + " - " + ImageLieux.getLieux().get(lieuOuPersonnage+1));
            }
            else {
                text.setInfo("Lieu - " + temps + " - " + ImagePersonnages.getPersonnages().get(lieuOuPersonnage));
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
        regle.setOnMouseEntered(_ -> regle.setStyle(
                "-fx-background-color: #E6B85C; " +  // Changement de couleur
                        "-fx-text-fill: #800000; " +  // Couleur du texte (si nécessaire)
                        "-fx-background-radius: 0 0 0 100px; " +  // Quart de cercle haut droit
                        "-fx-padding: 20;"  // Agrandir le bouton
        ));

        regle.setOnMouseExited(_ -> regle.setStyle(
                "-fx-background-color: #FFCC66; " +  // Couleur d'origine
                        "-fx-text-fill: #800000; " +  // Couleur du texte (si nécessaire)
                        "-fx-background-radius: 0 0 0 100px; " +  // Quart de cercle haut droit
                        "-fx-padding: 20;"  // Taille d'origine du bouton
        ));

        // Positionnement dans le coin supérieur droit
        StackPane stackPane = new StackPane(regle);
        stackPane.setAlignment(Pos.TOP_RIGHT);

        return stackPane;
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

    @Override
    public void actualiser() {
        if (!temps1Instance){
            creerNoteNombreTemps1();
        }

        // On actualise l'historique des indices en ajoutant le dernier indice découvert
        if (!ModeleJeu.Partie().getIndicesDecouverts().isEmpty()) {
            if (historique.getText().isEmpty()) {
                historique.setText("Tour 1 :\n" + ModeleJeu.Partie().getIndicesDecouverts().getLast() + "\n");
            } else if (ModeleJeu.Partie().getNbQuestion() != compterOccurrencesRegex(historique.getText(), "Tour")) {
                historique.setText("Tour " + ModeleJeu.Partie().getNbQuestion()
                        + " :\n" + ModeleJeu.Partie().getIndicesDecouverts().getLast()
                        + "\n" + historique.getText());
            }
        }

        for (TextCase t : listeCases){
            t.setFill(Color.LIGHTGRAY);
            t.setStyle("-fx-font-weight: normal; " +
                    "-fx-strikethrough: false;" +
                    "-fx-cursor: hand;");
            t.setEtat("neutre");
        }

        // On met à jour les cases du tableau
        for (TextCase text : listeCases) {
            List<String> elements = List.of("GF", "GE", "SA", "SC", "FD", "FC");
            String valeur;
            if (elements.contains(text.getText())) {
                valeur = nomLieu(text.getText()); // Pour le tableau de droite
            }
            else {
                valeur = text.getText(); // Pour le tableau de gauche
            }

            Temps temps = new Temps(Integer.parseInt(text.getInfo().split(" - ")[1])); // Pour les deux

            String lieu = ""; // Pour le tableau de gauche
            String personnage = ""; // Pour le tableau de droite

            if (ImageLieux.getLieux().containsValue(text.getInfo().split(" - ")[2])) {
                lieu = text.getInfo().split(" - ")[2];
            }
            else {
                personnage = text.getInfo().split(" - ")[2];
            }

            for (Note note : ModeleJeu.Partie().getGestionnaireNotes().getNotes()){
                if (note.estHypothese()){
                    continue;
                }
                if (personnage.isEmpty() && note.getPersonnage() == null){ // Pour le tableau de gauche
                    if (note.getLieu().getNom().equals(lieu)
                            && note.getTemps().getValeur() == temps.getValeur()
                            && note.getNbPersonnages() == Integer.parseInt(valeur)){
                        if (note.estAbsence()) {
                            text.setFill(Color.GRAY);
                            text.setStyle("-fx-font-weight: normal; " +
                                    "-fx-strikethrough: true;" +
                                    "-fx-cursor: hand;");
                            text.setEtat("absent");
                            break;
                        }
                        else {
                            text.setFill(Color.BLACK);
                            text.setStyle("-fx-font-weight: bold; " +
                                    "-fx-strikethrough: false;" +
                                    "-fx-cursor: hand;");
                            text.setEtat("présent");

                            // On met les autres cases en absent
                            for (TextCase t : listeCases) {
                                if (t.getInfo().contains("Nombre") &&
                                        t.getInfo().contains(lieu) &&
                                        t.getInfo().contains(String.valueOf(temps.getValeur())) &&
                                        !Objects.equals(t.getEtat(), "présent")) {
                                    t.setFill(Color.GRAY);
                                    t.setStyle("-fx-font-weight: normal; " +
                                            "-fx-strikethrough: true;" +
                                            "-fx-cursor: hand;");
                                    t.setEtat("absent");
                                }
                            }
                        }
                    }
                }
                else if (lieu.isEmpty() && note.getNbPersonnages() == 0 && note.getPersonnage() != null){ // Pour le tableau de droite
                    if (note.getPersonnage().getNom().equals(personnage)
                            && note.getTemps().getValeur() == temps.getValeur()
                            && note.getLieu().getNom().equals(valeur)){
                        if (note.estAbsence()) {
                            text.setFill(Color.GRAY);
                            text.setStyle("-fx-font-weight: normal; " +
                                    "-fx-strikethrough: true;" +
                                    "-fx-cursor: hand;");
                            text.setEtat("absent");
                            break;
                        }
                        else {
                            text.setFill(Color.BLACK);
                            text.setStyle("-fx-font-weight: bold; " +
                                    "-fx-strikethrough: false;" +
                                    "-fx-cursor: hand;");
                            text.setEtat("présent");

                            // On met les autres cases en absent
                            for (TextCase t : listeCases) {
                                if (t.getInfo().contains("Lieu") &&
                                        t.getInfo().contains(personnage) &&
                                        t.getInfo().contains(String.valueOf(temps.getValeur())) &&
                                        !Objects.equals(t.getEtat(), "présent")) {
                                    t.setFill(Color.GRAY);
                                    t.setStyle("-fx-font-weight: normal; " +
                                            "-fx-strikethrough: true;" +
                                            "-fx-cursor: hand;");
                                    t.setEtat("absent");
                                }
                            }
                        }
                    }
                }
            }
        }

        // On enlève la possibilité de cliquer sur les pions du temps 1

        for (TextCase t : listeCases){
            if (t.getInfo().split(" - ")[1].equals("1")) {
                t.setStyle(t.getStyle().split("-fx-cursor: hand;")[0] + "-fx-cursor: default;");
            }
        }
    }

    public static int compterOccurrencesRegex(String texte, String sequence) {
        Pattern pattern = Pattern.compile(Pattern.quote(sequence));
        Matcher matcher = pattern.matcher(texte);

        int count = 0;
        while (matcher.find()) {
            count++;
        }

        return count;
    }

    public void creerNoteNombreTemps1(){
        // On crée la liste de lieux
        List<String> lieux = new ArrayList<>();

        // On ajoute les lieux
        for (int i = 0; i < 6; i++){
            lieux.add(ImageLieux.getLieux().get(i + 1));
        }

        // On compte le nombre d'occurence dans chaque lieu et on met à jour la liste
        Map<String, Integer> occurences = new HashMap<>();

        // On l'initialise
        for (String s : lieux) {
            occurences.put(s, 0);
        }

        // On met à jour à chaque fois
        for(Note note : ModeleJeu.Partie().getGestionnaireNotes().getNotes()){
            if (note.getTemps().getValeur() == 1) {
                occurences.replace(note.getLieu().getNom(), occurences.get(note.getLieu().getNom()) + 1);
            }
        }

        // On crée les notes
        for (int i = 0; i < occurences.size(); i++){
            ModeleJeu.Partie().getGestionnaireNotes()
                    .ajouterNote(new Note(new Lieu(lieux.get(i), i+1, List.of()),
                            new Temps(1),
                            occurences.get(lieux.get(i))));
        }

        temps1Instance = true;
    }

    public Button getRetour() {
        return retour;
    }

    public Button getRegle() {
        return regle;
    }

    public Button getFaireDeduction() {
        return faireDeduction;
    }

    public Button getPoserQuestion() {
        return poserQuestion;
    }

    public Button getDemanderIndice() {
        return demanderIndice;
    }

    public Button getChangerAffichage() {
        return changerAffichage;
    }

    public Button getDeductionIA() {
        return deductionIA;
    }

    public Button getFilmJoueur() {
        return filmJoueur;
    }

    public Button getFilmRealite() {
        return filmRealite;
    }

    public TextArea getHistorique() {
        return historique;
    }

    public List<TextCase> getListeCases() {
        return listeCases;
    }
}
