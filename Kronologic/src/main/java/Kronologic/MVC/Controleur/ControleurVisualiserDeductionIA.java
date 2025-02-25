package Kronologic.MVC.Controleur;

import Kronologic.MVC.Modele.SousModeleJeu.ModeleIA;
import Kronologic.MVC.Vue.VueDeductionIA;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ControleurVisualiserDeductionIA implements EventHandler<ActionEvent> {

    private ModeleIA modeleIA;

    public ControleurVisualiserDeductionIA(ModeleIA modeleIA) {
        this.modeleIA = modeleIA;
    }

    private void initialiserBoutons(VueDeductionIA vueDeductionIA){
        // Définir l'action des boutons
        Button boutonChocoSolver = vueDeductionIA.getBoutonChocoSolver();
        Button boutonHeuristique = vueDeductionIA.getBoutonHeuristique();

        boutonChocoSolver.setOnAction(e -> {
            String historiqueChocoSolver = modeleIA.voirDeductionIAChocoSolver();
            vueDeductionIA.afficherDeduction(historiqueChocoSolver);
        });

        boutonHeuristique.setOnAction(e -> {
            String historiqueHeuristique = modeleIA.voirDeductionIAHeuristique();
            vueDeductionIA.afficherDeduction(historiqueHeuristique);
        });
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        VueDeductionIA vueDeductionIA = new VueDeductionIA();

        initialiserBoutons(vueDeductionIA);

        // Afficher la vue initiale avec ChocoSolver par défaut
        String historique = modeleIA.voirDeductionIAChocoSolver();
        vueDeductionIA.afficherDeduction(historique);

        Stage stage = new Stage();
        stage.setTitle("Déductions de l'IA");
        Scene scene = new Scene(vueDeductionIA, 600, 400);
        stage.setScene(scene);
        stage.show();
    }
}

