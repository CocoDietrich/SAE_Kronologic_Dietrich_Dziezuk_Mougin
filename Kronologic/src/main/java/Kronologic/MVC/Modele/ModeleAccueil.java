package Kronologic.MVC.Modele;


import Kronologic.Data.JsonReader;
import Kronologic.MVC.Controleur.ControleurAffichage;
import Kronologic.MVC.Controleur.ControleurPoseQuestion;
import Kronologic.MVC.Controleur.ControleurQuitter;
import Kronologic.MVC.Controleur.ControleurVisualiserPoseQuestion;
import Kronologic.MVC.Controleur.ControleurVoirDeductionIA;
import Kronologic.MVC.Vue.Observateur;
import Kronologic.MVC.Vue.VueCarte;
import Kronologic.MVC.Vue.VuePoseQuestion;
import Kronologic.MVC.Vue.VueTableau;
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

    private final List<Observateur> observateurs;

    public ModeleAccueil() {
        this.observateurs = new ArrayList<>();
    }

    public void initialiserPartie(String s, Stage stage) {
        switch (s){
            case "Jouer" :
                ModeleJeu modeleJeu = new ModeleJeu(JsonReader.lirePartieDepuisJson("data/enquete_base.json"));

                VueCarte vueCarte = new VueCarte();
                VueTableau vueTableau = new VueTableau();
                VuePoseQuestion vuePoseQuestion = new VuePoseQuestion();

                ControleurQuitter controleurQuitter = new ControleurQuitter(modeleJeu);
                ControleurVisualiserPoseQuestion controleurVisualiserPoseQuestion = new ControleurVisualiserPoseQuestion(modeleJeu);
                ControleurPoseQuestion controleurPoseQuestion = new ControleurPoseQuestion(modeleJeu);
                ControleurAffichage controleurAffichage = new ControleurAffichage(modeleJeu);
                ControleurVoirDeductionIA controleurVoirDeductionIA = new ControleurVoirDeductionIA(modeleJeu);


                vueCarte.retour.setOnAction(controleurQuitter);
                vueCarte.poserQuestion.setOnAction(controleurVisualiserPoseQuestion);
                vueCarte.changerAffichage.setOnAction(controleurAffichage);
                vueTableau.poserQuestion.setOnAction(controleurVisualiserPoseQuestion);
                vueTableau.changerAffichage.setOnAction(controleurAffichage);
                vuePoseQuestion.retour.setOnAction(controleurPoseQuestion);
                vuePoseQuestion.annuler.setOnAction(controleurPoseQuestion);
                vuePoseQuestion.valider.setOnAction(controleurPoseQuestion);
                vueCarte.deductionIA.setOnAction(controleurVoirDeductionIA);
                for (Button b : vuePoseQuestion.lieuButtons){
                    b.setOnAction(controleurPoseQuestion);
                }
                for (Button b : vuePoseQuestion.tempsButtons){
                    b.setOnAction(controleurPoseQuestion);
                }
                for (Button b : vuePoseQuestion.personnageButtons){
                    b.setOnAction(controleurPoseQuestion);
                }

                modeleJeu.enregistrerObservateur(vueCarte);
                modeleJeu.enregistrerObservateur(vuePoseQuestion);
                modeleJeu.enregistrerObservateur(vueTableau);

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
