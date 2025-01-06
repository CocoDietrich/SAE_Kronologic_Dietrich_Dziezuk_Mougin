package Kronologic.MVC.Modele;

import Kronologic.Data.JsonReader;
import Kronologic.Jeu.Elements.Pion;
import Kronologic.MVC.Controleur.*;
import Kronologic.MVC.Controleur.PopUps.ControleurPopUpDeduction;
import Kronologic.MVC.Controleur.PopUps.ControleurPopUpDemanderIndice;
import Kronologic.MVC.Controleur.PopUps.ControleurPopUpPoseQuestion;
import Kronologic.MVC.Controleur.PopUps.ControleurPopUpQuitter;
import Kronologic.MVC.Vue.*;
import Kronologic.MVC.Vue.PopUps.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
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

                VuePopUpEnigme vuePopUpEnigme = new VuePopUpEnigme(stage);
                VueCarte vueCarte = new VueCarte(modeleJeu);
                VueTableau vueTableau = new VueTableau();
                VuePoseQuestion vuePoseQuestion = new VuePoseQuestion();
                VueDeduction vueDeduction = new VueDeduction();
                VuePopUpDeduction vuePopUpDeduction = new VuePopUpDeduction(stage, new Stage());
                VuePopUpQuitter vuePopUpQuitter = new VuePopUpQuitter(stage);
                VueRegle vueRegle = new VueRegle();
                VuePopUpDemanderIndice vuePopUpDemanderIndice = new VuePopUpDemanderIndice(stage);
                VuePopUpPoseQuestion vuePopUpPoseQuestion = new VuePopUpPoseQuestion(stage);

                ControleurQuitter controleurQuitter = new ControleurQuitter(modeleJeu);
                ControleurVisualiserPoseQuestion controleurVisualiserPoseQuestion = new ControleurVisualiserPoseQuestion(modeleJeu);
                ControleurPoseQuestion controleurPoseQuestion = new ControleurPoseQuestion(modeleJeu);
                ControleurAffichage controleurAffichage = new ControleurAffichage(modeleJeu);
                ControleurVoirDeductionIA controleurVoirDeductionIA = new ControleurVoirDeductionIA(modeleJeu);
                ControleurChoixTableau controleurChoixTableau = new ControleurChoixTableau(modeleJeu);
                ControleurDemanderIndice controleurDemanderIndice = new ControleurDemanderIndice(modeleJeu);

                ControleurVisualiserDeduction controleurVisualiserDeduction = new ControleurVisualiserDeduction(modeleJeu);
                ControleurDeduction controleurDeduction = new ControleurDeduction(modeleJeu);
                ControleurPopUpDeduction controleurPopUpDeduction = new ControleurPopUpDeduction(modeleJeu);
                ControleurVisualiserRegle controleurVisualiserRegle = new ControleurVisualiserRegle(modeleJeu);
                ControleurPopUpQuitter controleurPopUpQuitter = new ControleurPopUpQuitter(modeleJeu);
                ControleurRegle controleurRegle = new ControleurRegle(modeleJeu);
                ControleurPopUpDemanderIndice controleurPopUpDemanderIndice = new ControleurPopUpDemanderIndice(modeleJeu);
                ControleurChoixCarte controleurChoixCarte = new ControleurChoixCarte(modeleJeu);
                ControleurPopUpPoseQuestion controleurPopUpPoseQuestion = new ControleurPopUpPoseQuestion(modeleJeu);

                vueCarte.retour.setOnAction(controleurQuitter);
                vueCarte.poserQuestion.setOnAction(controleurVisualiserPoseQuestion);
                vueCarte.changerAffichage.setOnAction(controleurAffichage);
                vueCarte.regle.setOnAction(controleurVisualiserRegle);
                vueCarte.demanderIndice.setOnAction(controleurDemanderIndice);
                vueTableau.retour.setOnAction(controleurQuitter);
                vueTableau.poserQuestion.setOnAction(controleurVisualiserPoseQuestion);
                vueTableau.changerAffichage.setOnAction(controleurAffichage);
                vueTableau.regle.setOnAction(controleurVisualiserRegle);
                vueRegle.retour.setOnAction(controleurRegle);

                for (TextCase tc : vueTableau.listeCases){
                    tc.setOnMouseClicked(controleurChoixTableau);
                }

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

                vueCarte.faireDeduction.setOnAction(controleurVisualiserDeduction);
                vueDeduction.retour.setOnAction(controleurDeduction);
                vueDeduction.annuler.setOnAction(controleurDeduction);
                vueDeduction.valider.setOnAction(controleurDeduction);
                for (Button b : vueDeduction.lieuButtons){
                    b.setOnAction(controleurDeduction);
                }
                for (Button b : vueDeduction.tempsButtons){
                    b.setOnAction(controleurDeduction);
                }
                for (Button b : vueDeduction.personnageButtons){
                    b.setOnAction(controleurDeduction);
                }
                for (Pion p : vueCarte.pions){
                    p.setOnDragDone(controleurChoixCarte);
                }

                vuePopUpDeduction.quitter.setOnAction(controleurPopUpDeduction);
                vuePopUpQuitter.annuler.setOnAction(controleurPopUpQuitter);
                vuePopUpQuitter.valider.setOnAction(controleurPopUpQuitter);
                vuePopUpDemanderIndice.annuler.setOnAction(controleurPopUpDemanderIndice);
                vuePopUpDemanderIndice.valider.setOnAction(controleurPopUpDemanderIndice);
                vuePopUpPoseQuestion.continuer.setOnAction(controleurPopUpPoseQuestion);

                modeleJeu.enregistrerObservateur(vueCarte);
                modeleJeu.enregistrerObservateur(vuePoseQuestion);
                modeleJeu.enregistrerObservateur(vueTableau);
                modeleJeu.enregistrerObservateur(vueDeduction);
                modeleJeu.enregistrerObservateur(vuePopUpDeduction);
                modeleJeu.enregistrerObservateur(vuePopUpQuitter);
                modeleJeu.enregistrerObservateur(vuePopUpDemanderIndice);
                modeleJeu.enregistrerObservateur(vueRegle);
                modeleJeu.enregistrerObservateur(vuePopUpPoseQuestion);

                BorderPane bp = new BorderPane(vueCarte);

                Scene scene = new Scene(bp, stage.getWidth(), stage.getHeight());
                stage.setScene(scene);
                stage.show();
                vuePopUpEnigme.afficherPopUp(ModeleJeu.getPartie().getEnquete());
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
