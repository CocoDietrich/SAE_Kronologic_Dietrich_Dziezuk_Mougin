package Kronologic.MVC.Modele;


import Kronologic.Data.JsonReader;
import Kronologic.MVC.Controleur.ControleurQuitter;
import Kronologic.MVC.Vue.Observateur;
import Kronologic.MVC.Vue.VueCarte;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ModeleAccueil implements Sujet {

    private List<Observateur> observateurs;

    public ModeleAccueil() {
        this.observateurs = new ArrayList<>();
    }

    public void initialiserPartie(String s, Stage stage) {
        switch (s){
            case "Jouer" :
                ModeleJeu modeleJeu = new ModeleJeu(JsonReader.lirePartieDepuisJson("data/enquete_base.json"));

                VueCarte vueCarte = new VueCarte();

                ControleurQuitter controleurQuitter = new ControleurQuitter(modeleJeu);

                vueCarte.retour.setOnAction(controleurQuitter);

                modeleJeu.enregistrerObservateur(vueCarte);

                BorderPane bp = new BorderPane(vueCarte);

                Scene scene = new Scene(bp, stage.getWidth(), stage.getHeight());
                stage.setScene(scene);
                stage.show();
                break;
            case "IAJoueuse":
                // TODO : à implémenter quand l'IA le sera
                break;
            default:
                break;
        }
    }

    public void quitterJeu() {
        System.exit(0);
    }


    @Override
    public void enregistrerObservateur(Observateur o) {
        this.observateurs.add(o);
    }

    @Override
    public void supprimerObservateur(Observateur o) {
        this.observateurs.remove(o);
    }

    @Override
    public void notifierObservateurs() {
        for (Observateur o : observateurs){
            o.actualiser();
        }
    }
}
