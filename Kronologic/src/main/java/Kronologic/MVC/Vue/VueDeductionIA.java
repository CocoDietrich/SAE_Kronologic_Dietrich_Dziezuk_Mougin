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
        this.setBackground(creerFond());

        Text titre = creerTitre();
        affichage = creerZoneDeTexte();

        boutonChocoSolver = creerBouton("IA ChocoSolver");
        boutonHeuristique = creerBouton("IA Heuristique");

        HBox boutons = creerHBoxBoutons(boutonChocoSolver, boutonHeuristique);
        VBox centre = new VBox(30, titre, affichage, boutons);
        centre.setAlignment(Pos.CENTER);

        this.setCenter(centre);
    }

    private Background creerFond() {
        return new Background(
                new BackgroundFill(
                        new javafx.scene.paint.LinearGradient(0, 0, 1, 0, true,
                                javafx.scene.paint.CycleMethod.NO_CYCLE,
                                new javafx.scene.paint.Stop(0, Color.YELLOW),
                                new javafx.scene.paint.Stop(1, Color.RED)),
                        CornerRadii.EMPTY,
                        null
                )
        );
    }

    private Text creerTitre() {
        Text title = new Text("Déductions de l'IA");
        title.setFont(Font.font("Arial", 36));
        title.setFill(Color.DARKRED);
        title.setTextAlignment(TextAlignment.CENTER);

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.BLACK);
        shadow.setOffsetX(3);
        shadow.setOffsetY(3);
        title.setEffect(shadow);

        return title;
    }

    private TextArea creerZoneDeTexte() {
        TextArea textArea = new TextArea();
        textArea.setEditable(false);  // Lecture seule
        textArea.setWrapText(true);   // Ajuster le texte
        textArea.setStyle("-fx-control-inner-background: #FFFDD0; -fx-font-family: 'Courier New'; -fx-font-size: 14px; -fx-border-color: #A52A2A; -fx-border-width: 2px;");
        textArea.setPrefHeight(300);
        return textArea;
    }

    private Button creerBouton(String buttonText) {
        Button button = new Button(buttonText);
        button.setStyle("-fx-background-color: #FFD700; -fx-text-fill: black; -fx-font-size: 14px; -fx-border-color: #B22222; -fx-border-width: 2px; -fx-background-radius: 10px; -fx-border-radius: 10px;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #FFA500; -fx-text-fill: white; -fx-font-size: 14px; -fx-border-color: #8B0000; -fx-border-width: 2px; -fx-background-radius: 10px; -fx-border-radius: 10px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #FFD700; -fx-text-fill: black; -fx-font-size: 14px; -fx-border-color: #B22222; -fx-border-width: 2px; -fx-background-radius: 10px; -fx-border-radius: 10px;"));
        return button;
    }

    private HBox creerHBoxBoutons(Button... buttons) {
        HBox buttonBox = new HBox(15, buttons);
        buttonBox.setAlignment(Pos.CENTER);
        return buttonBox;
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
        // Mettre à jour l'interface si nécessaire
    }
}
