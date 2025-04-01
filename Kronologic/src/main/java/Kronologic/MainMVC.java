package Kronologic;

import Kronologic.MVC.Controleur.Accueil.ControleurFilmIAJoueuse;
import Kronologic.MVC.Controleur.Accueil.ControleurInitialisation;
import Kronologic.MVC.Controleur.Accueil.ControleurQuitterJeu;
import Kronologic.MVC.Modele.ModeleAccueil;
import Kronologic.MVC.Controleur.Accueil.ControleurIAAccueil;
import Kronologic.MVC.Vue.VueAccueil;
import Kronologic.MVC.Vue.VueChargementIA;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

import javafx.stage.Stage;

public class MainMVC extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        ModeleAccueil modeleAccueil = new ModeleAccueil();
        VueAccueil vueAccueil = new VueAccueil();
        VueChargementIA vueChargementIA = new VueChargementIA();
        modeleAccueil.enregistrerObservateur(vueAccueil);
        modeleAccueil.enregistrerObservateur(vueChargementIA);

        BorderPane bp = new BorderPane();
        bp.setCenter(vueAccueil);

        ControleurInitialisation controleurInitialisation = new ControleurInitialisation(modeleAccueil);
        ControleurIAAccueil controleurIAAccueil = new ControleurIAAccueil();
        ControleurQuitterJeu controleurQuitterJeu = new ControleurQuitterJeu(modeleAccueil);

        vueAccueil.getJouer().setOnAction(controleurInitialisation);
        vueAccueil.getIAJoueuse().setOnAction(controleurIAAccueil);
        vueAccueil.getQuitter().setOnAction(controleurQuitterJeu);

        Scene scene = new Scene(bp);
        stage.setTitle("Kronologic");
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }
}