package Kronologic.MVC.Vue;

import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;

public class VueDeductionIA extends BorderPane implements Observateur {

    private final TextArea affichage;
    private final Button boutonChocoSolver;
    private final Button boutonHeuristique;

    public VueDeductionIA() {
        super();

        this.setBackground(new Background(
                new BackgroundFill(
                        new javafx.scene.paint.LinearGradient(0, 0, 1, 0, true,
                                javafx.scene.paint.CycleMethod.NO_CYCLE,
                                new javafx.scene.paint.Stop(0, Color.YELLOW),
                                new javafx.scene.paint.Stop(1, Color.RED)),
                        CornerRadii.EMPTY,
                        null
                )
        ));

        // Titre avec ombre
        Text titre = new Text("Déductions de l'IA");
        titre.setFont(Font.font("Arial", 36));
        titre.setFill(Color.DARKRED);
        titre.setTextAlignment(TextAlignment.CENTER);
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.BLACK);
        shadow.setOffsetX(3);
        shadow.setOffsetY(3);
        titre.setEffect(shadow);

        // Zone d'affichage
        affichage = new TextArea();
        affichage.setEditable(false);  // Lecture seule
        affichage.setWrapText(true);   // Ajuster le texte
        affichage.setStyle("-fx-control-inner-background: #FFFDD0; -fx-font-family: 'Courier New'; -fx-font-size: 14px; -fx-border-color: #A52A2A; -fx-border-width: 2px;");
        affichage.setPrefHeight(300);

        // Boutons
        boutonChocoSolver = new Button("IA ChocoSolver");
        boutonChocoSolver.setStyle("-fx-background-color: #FFD700; -fx-text-fill: black; -fx-font-size: 14px; -fx-border-color: #B22222; -fx-border-width: 2px; -fx-background-radius: 10px; -fx-border-radius: 10px;");
        boutonChocoSolver.setOnMouseEntered(e -> boutonChocoSolver.setStyle("-fx-background-color: #FFA500; -fx-text-fill: white; -fx-font-size: 14px; -fx-border-color: #8B0000; -fx-border-width: 2px; -fx-background-radius: 10px; -fx-border-radius: 10px;"));
        boutonChocoSolver.setOnMouseExited(e -> boutonChocoSolver.setStyle("-fx-background-color: #FFD700; -fx-text-fill: black; -fx-font-size: 14px; -fx-border-color: #B22222; -fx-border-width: 2px; -fx-background-radius: 10px; -fx-border-radius: 10px;"));

        boutonHeuristique = new Button("IA Heuristique");
        boutonHeuristique.setStyle("-fx-background-color: #FFD700; -fx-text-fill: black; -fx-font-size: 14px; -fx-border-color: #B22222; -fx-border-width: 2px; -fx-background-radius: 10px; -fx-border-radius: 10px;");
        boutonHeuristique.setOnMouseEntered(e -> boutonHeuristique.setStyle("-fx-background-color: #FFA500; -fx-text-fill: white; -fx-font-size: 14px; -fx-border-color: #8B0000; -fx-border-width: 2px; -fx-background-radius: 10px; -fx-border-radius: 10px;"));
        boutonHeuristique.setOnMouseExited(e -> boutonHeuristique.setStyle("-fx-background-color: #FFD700; -fx-text-fill: black; -fx-font-size: 14px; -fx-border-color: #B22222; -fx-border-width: 2px; -fx-background-radius: 10px; -fx-border-radius: 10px;"));

        HBox boutons = new HBox(15, boutonChocoSolver, boutonHeuristique);
        boutons.setAlignment(Pos.CENTER);

        VBox centre = new VBox(30, titre, affichage, boutons);
        centre.setAlignment(Pos.CENTER);

        this.setCenter(centre);
    }

    public Button getBoutonChocoSolver() {
        return boutonChocoSolver;
    }

    public Button getBoutonHeuristique() {
        return boutonHeuristique;
    }

    public void afficherDeduction(String deductions) {
        affichage.setText(deductions);
    }

    @Override
    public void actualiser() {

    }
}
