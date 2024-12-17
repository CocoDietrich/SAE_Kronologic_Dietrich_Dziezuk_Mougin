package Kronologic.MVC.Controleur;

import Kronologic.MVC.Modele.ModeleJeu;
import Kronologic.MVC.Vue.VueDeductionIA;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ControleurVoirDeductionIA implements EventHandler<ActionEvent> {

    private ModeleJeu modele;

    public ControleurVoirDeductionIA(ModeleJeu modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        // Création de la nouvelle vue
        VueDeductionIA vueDeductionIA = new VueDeductionIA();

        // Récupération des déductions de l'IA
        String historique = modele.voirDeductionIA();

        // Affichage dans la nouvelle vue
        vueDeductionIA.afficherDeduction(historique);

        // Création de la scène
        Stage stage = new Stage();
        stage.setTitle("Déductions de l'IA");
        Scene scene = new Scene(vueDeductionIA, 600, 400);
        stage.setScene(scene);
        stage.show();
    }
}
