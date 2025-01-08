package Kronologic.MVC.Controleur;

import Kronologic.MVC.Modele.ModeleJeu;
import Kronologic.MVC.Vue.VueDeductionIA;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.control.Button;

public class ControleurVoirDeductionIA implements EventHandler<ActionEvent> {

    private ModeleJeu modele;

    public ControleurVoirDeductionIA(ModeleJeu modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        VueDeductionIA vueDeductionIA = new VueDeductionIA();

        // Définir l'action des boutons
        Button boutonChocoSolver = vueDeductionIA.getBoutonChocoSolver();
        Button boutonHeuristique = vueDeductionIA.getBoutonHeuristique();

        boutonChocoSolver.setOnAction(e -> {
            String historiqueChocoSolver = modele.voirDeductionIAChocoSolver();
            vueDeductionIA.afficherDeduction(historiqueChocoSolver);
        });

        boutonHeuristique.setOnAction(e -> {
            String historiqueHeuristique = modele.voirDeductionIAHeuristique();
            vueDeductionIA.afficherDeduction(historiqueHeuristique);
        });

        // Afficher la vue initiale avec ChocoSolver par défaut
        String historique = modele.voirDeductionIAChocoSolver();
        vueDeductionIA.afficherDeduction(historique);

        Stage stage = new Stage();
        stage.setTitle("Déductions de l'IA");
        Scene scene = new Scene(vueDeductionIA, 600, 400);
        stage.setScene(scene);
        stage.show();
    }
}

