package Kronologic.MVC.Vue;

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
    private final Stage stage;
    private final Stage nouveauStage;
    private Text message;
    public Button quitter;
    public Button voirFilm;

    public VuePopUpDeduction(Stage stage, Stage nouveauStage) {
        this.stage = stage;
        this.nouveauStage = nouveauStage;
        initialiserVue();
    }

    private void initialiserVue() {
        this.setStyle("-fx-background-color: #f5a623; -fx-padding: 20px;");

        message = new Text();
        message.setStyle("-fx-font-size: 30px; -fx-fill: #7b001e; -fx-font-weight: bold;");

        quitter = new Button("Quitter");
        quitter.setId("Quitter");
        quitter.setStyle("-fx-background-color: #7b001e; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");
        quitter.setPrefSize(120, 50);

        voirFilm = new Button("Voir le film");
        voirFilm.setStyle("-fx-background-color: #7b001e; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");
        voirFilm.setPrefSize(180, 50);
    }


    public void afficherPopUp(boolean victoire, String loupe) {
        this.setCenter(null);
        this.setBottom(null);

        VBox contenuPrincipal = new VBox(20);
        contenuPrincipal.setAlignment(Pos.CENTER);
        contenuPrincipal.setPadding(new Insets(20));

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
            quitterBox.setPadding(new Insets(20));

            BorderPane boutonsPane = new BorderPane();
            boutonsPane.setBottom(voirFilm);
            BorderPane.setMargin(voirFilm, new Insets(20));
            BorderPane.setAlignment(voirFilm, Pos.BOTTOM_RIGHT);

            this.setCenter(contenuPrincipal);
            this.setBottom(new BorderPane(null, null, boutonsPane, null, quitterBox));
        }

        Scene scene = new Scene(this, 800, 500);
        nouveauStage.setScene(scene);
        nouveauStage.show();
    }


    @Override
    public void actualiser() {

    }

    public Stage getStage() {
        return stage;
    }
}
