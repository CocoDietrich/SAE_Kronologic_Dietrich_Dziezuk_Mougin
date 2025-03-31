package Kronologic.MVC.Vue.PopUps;

import Kronologic.MVC.Vue.Observateur;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class VuePopUpDeduction extends BorderPane implements Observateur {

    private final static int LARGEUR = 800;
    private final static int HAUTEUR = 500;
    private final static int INSETS = 20;

    private final static int LARGEUR_QUITTER = 120;
    private final static int LARGEUR_VOIR_FILM = 180;
    private final static int HAUTEUR_BOUTON = 50;

    private final static String STYLE_BOUTON = "-fx-background-color: #7b001e; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;";

    private final static String QUITTER = "Quitter";
    private final static String VOIR_FILM = "Voir le film";

    private final Stage stage;
    private final Stage nouveauStage;
    private Text message;
    private Button quitter;
    private Button voirFilm;

    public VuePopUpDeduction(Stage stage, Stage nouveauStage) {
        this.stage = stage;
        this.nouveauStage = nouveauStage;
        initialiserVue();
    }

    private void initialiserVue() {
        this.setStyle("-fx-background-color: #f5a623; -fx-padding: 20px;");

        message = new Text();
        message.setStyle("-fx-font-size: 30px; -fx-fill: #7b001e; -fx-font-weight: bold;");

        quitter = new Button(QUITTER);
        quitter.setStyle(STYLE_BOUTON);
        quitter.setPrefSize(LARGEUR_QUITTER, HAUTEUR_BOUTON);

        voirFilm = new Button(VOIR_FILM);
        voirFilm.setStyle(STYLE_BOUTON);
        voirFilm.setPrefSize(LARGEUR_VOIR_FILM, HAUTEUR_BOUTON);
    }

    public void afficherPopUp(boolean victoire, String loupe) {
        this.setCenter(null);
        this.setBottom(null);

        VBox contenuPrincipal = new VBox(INSETS);
        contenuPrincipal.setAlignment(Pos.CENTER);
        contenuPrincipal.setPadding(new Insets(INSETS));

        if (victoire) {
            message.setText("Bravo ! Vous avez résolu l'énigme !"
                    + "\nVous obtenez la " + loupe);
            contenuPrincipal.getChildren().addAll(message, quitter);
            this.setCenter(contenuPrincipal);
        } else {
            message.setText("Votre déduction n'est pas la bonne !\nDommage... Vous pouvez retrouver la solution\navec le film de l'énigme.");
            contenuPrincipal.getChildren().add(message);

            HBox quitterBox = new HBox(quitter);
            quitterBox.setAlignment(Pos.BOTTOM_LEFT);
            quitterBox.setPadding(new Insets(INSETS));

            BorderPane boutonsPane = new BorderPane();
            boutonsPane.setBottom(voirFilm);
            BorderPane.setMargin(voirFilm, new Insets(INSETS));
            BorderPane.setAlignment(voirFilm, Pos.BOTTOM_RIGHT);

            this.setCenter(contenuPrincipal);
            this.setBottom(new BorderPane(null, null, boutonsPane, null, quitterBox));
        }

        Scene scene = new Scene(this, LARGEUR, HAUTEUR);
        nouveauStage.setScene(scene);
        nouveauStage.show();
    }

    @Override
    public void actualiser() {}

    public Stage getStage() {
        return stage;
    }

    public Button getQuitter() {
        return quitter;
    }

    public Button getVoirFilm() {
        return voirFilm;
    }
}
