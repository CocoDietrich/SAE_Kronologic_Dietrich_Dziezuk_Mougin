package Kronologic;

import Kronologic.MVC.Controleur.Accueil.ControleurInitialisation;
import Kronologic.MVC.Controleur.Accueil.ControleurQuitterJeu;
import Kronologic.MVC.Modele.ModeleAccueil;
import Kronologic.MVC.Vue.VueAccueil;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

import javafx.stage.Stage;

public class MainMVC extends Application {
    public static void main() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        ModeleAccueil modeleAccueil = new ModeleAccueil();

        VueAccueil vueAccueil = new VueAccueil();

        modeleAccueil.enregistrerObservateur(vueAccueil);

        BorderPane bp = new BorderPane();
        bp.setCenter(vueAccueil);

        ControleurInitialisation controleurInitialisation = new ControleurInitialisation(modeleAccueil);
        vueAccueil.jouer.setOnAction(controleurInitialisation);

        ControleurQuitterJeu controleurQuitterJeu = new ControleurQuitterJeu(modeleAccueil);
        vueAccueil.quitter.setOnAction(controleurQuitterJeu);

        Scene scene = new Scene(bp);
        stage.setTitle("Kronologic");
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }
}