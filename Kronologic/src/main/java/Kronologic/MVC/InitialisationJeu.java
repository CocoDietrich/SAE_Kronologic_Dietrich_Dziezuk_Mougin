package Kronologic.MVC;

import Kronologic.Data.JsonReader;
import Kronologic.IA.GenerateurScenarios.GenerateurScenario;
import Kronologic.Jeu.Elements.Pion;
import Kronologic.Jeu.Partie;
import Kronologic.MVC.Controleur.*;
import Kronologic.MVC.Controleur.Accueil.ControleurIAJoueuse;
import Kronologic.MVC.Controleur.PopUps.ControleurPopUpDeduction;
import Kronologic.MVC.Controleur.PopUps.ControleurPopUpDemanderIndice;
import Kronologic.MVC.Controleur.PopUps.ControleurPopUpPoseQuestion;
import Kronologic.MVC.Controleur.PopUps.ControleurPopUpQuitter;
import Kronologic.MVC.Modele.ModeleAccueil;
import Kronologic.MVC.Modele.ModeleJeu;
import Kronologic.MVC.Vue.PopUps.*;
import Kronologic.MVC.Vue.*;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class InitialisationJeu {

    private final Stage stage;
    private VuePopUpEnigme vuePopUpEnigme;
    private VueCarte vueCarte;

    public InitialisationJeu(Stage stage) {
        this.stage = stage;
    }

    public void initialiser() {

        // Choix du type d'enquête
        Alert choixAlert = new Alert(Alert.AlertType.NONE);
        choixAlert.setTitle("Choix de l'enquête");
        choixAlert.setHeaderText("Choisissez votre type d'enquête :");

        ButtonType btnClassique = new ButtonType("Enquête classique");
        ButtonType btnGeneree = new ButtonType("Enquête générée");
        ButtonType btnAnnuler = ButtonType.CANCEL;

        choixAlert.getButtonTypes().setAll(btnClassique, btnGeneree, btnAnnuler);

        Optional<ButtonType> resultat = choixAlert.showAndWait();
        if (resultat.isEmpty()) {
            return;
        }

        String fichierJson;

        if (resultat.get() == btnGeneree) {
            try {
                Partie partieGeneree = GenerateurScenario.genererScenario();
                GenerateurScenario.exporterJson(partieGeneree, "data/enquete_generee.json");
                fichierJson = "data/enquete_generee.json";
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        } else if (resultat.get() == btnClassique) {
            fichierJson = "data/enquete_base.json";
        } else {
            return;
        }
        // Création du modèle
        ModeleJeu modeleJeu = new ModeleJeu(JsonReader.lirePartieDepuisJson(fichierJson));
        ModeleAccueil modeleAccueil = new ModeleAccueil();

        // Création des vues
        this.vuePopUpEnigme = new VuePopUpEnigme();
        this.vueCarte = new VueCarte(modeleJeu);
        VueTableau vueTableau = new VueTableau();
        VuePoseQuestion vuePoseQuestion = new VuePoseQuestion();
        VueDeduction vueDeduction = new VueDeduction();
        VueDeductionIA vueDeductionIA = new VueDeductionIA();
        VuePopUpDeduction vuePopUpDeduction = new VuePopUpDeduction(stage, new Stage());
        VuePopUpQuitter vuePopUpQuitter = new VuePopUpQuitter(stage);
        VueRegle vueRegle = new VueRegle();
        VuePopUpDemanderIndice vuePopUpDemanderIndice = new VuePopUpDemanderIndice();
        VuePopUpPoseQuestion vuePopUpPoseQuestion = new VuePopUpPoseQuestion();
        VueAccueil vueAccueil = new VueAccueil();

        // Création des controleurs
        ControleurQuitter controleurQuitter = new ControleurQuitter(modeleJeu);
        ControleurVisualiserPoseQuestion controleurVisualiserPoseQuestion = new ControleurVisualiserPoseQuestion(modeleJeu);
        ControleurPoseQuestion controleurPoseQuestion = new ControleurPoseQuestion(modeleJeu);
        ControleurAffichage controleurAffichage = new ControleurAffichage(modeleJeu);
        ControleurVisualiserDeductionIA controleurVoirDeductionIA = new ControleurVisualiserDeductionIA(modeleJeu.getModeleIA());
        ControleurChoixTableau controleurChoixTableau = new ControleurChoixTableau(modeleJeu.getModeleNotes());
        ControleurDemanderIndice controleurDemanderIndice = new ControleurDemanderIndice(modeleJeu.getModeleIA());
        ControleurVisualiserDeduction controleurVisualiserDeduction = new ControleurVisualiserDeduction(modeleJeu);
        ControleurDeduction controleurDeduction = new ControleurDeduction(modeleJeu);
        // Controleurs
        ControleurPopUpDeduction controleurPopUpDeduction = new ControleurPopUpDeduction(modeleJeu);
        ControleurVisualiserRegle controleurVisualiserRegle = new ControleurVisualiserRegle(modeleJeu);
        ControleurPopUpQuitter controleurPopUpQuitter = new ControleurPopUpQuitter(modeleJeu);
        ControleurQuitterRegleFilm controleurQuitterRegleFilm = new ControleurQuitterRegleFilm(modeleJeu);
        ControleurPopUpDemanderIndice controleurPopUpDemanderIndice = new ControleurPopUpDemanderIndice(modeleJeu);
        ControleurChoixCarte controleurChoixCarte = new ControleurChoixCarte(modeleJeu.getModeleNotes());
        ControleurPopUpPoseQuestion controleurPopUpPoseQuestion = new ControleurPopUpPoseQuestion();
        ControleurVisualiserFilmJoueur controleurVisualiserFilmJoueur = new ControleurVisualiserFilmJoueur(modeleJeu);
        ControleurVisualiserFilmRealite controleurVisualiserFilmRealite = new ControleurVisualiserFilmRealite(modeleJeu);
        ControleurImagePions controleurImagePions = new ControleurImagePions(modeleJeu);

        // Assignation des controleurs aux vues
        vueCarte.getRetour().setOnAction(controleurQuitter);
        vueTableau.getRetour().setOnAction(controleurQuitter);
        vueCarte.getPoserQuestion().setOnAction(controleurVisualiserPoseQuestion);
        vueTableau.getPoserQuestion().setOnAction(controleurVisualiserPoseQuestion);
        vueCarte.getChangerAffichage().setOnAction(controleurAffichage);
        vueTableau.getChangerAffichage().setOnAction(controleurAffichage);
        vueCarte.getRegle().setOnAction(controleurVisualiserRegle);
        vueTableau.getRegle().setOnAction(controleurVisualiserRegle);
        vueCarte.getDemanderIndice().setOnAction(controleurDemanderIndice);
        vueTableau.getDemanderIndice().setOnAction(controleurDemanderIndice);
        vueCarte.getDeductionIA().setOnAction(controleurVoirDeductionIA);
        vueTableau.getDeductionIA().setOnAction(controleurVoirDeductionIA);
        vueCarte.getFilmJoueur().setOnAction(controleurVisualiserFilmJoueur);
        vueTableau.getFilmJoueur().setOnAction(controleurVisualiserFilmJoueur);
        vueCarte.getFilmRealite().setOnAction(controleurVisualiserFilmRealite);
        vueTableau.getFilmRealite().setOnAction(controleurVisualiserFilmRealite);
        vueCarte.getFaireDeduction().setOnAction(controleurVisualiserDeduction);
        vueTableau.getFaireDeduction().setOnAction(controleurVisualiserDeduction);

        vueCarte.getHypothese().setOnAction(controleurImagePions);
        vueCarte.getAbsence().setOnAction(controleurImagePions);
        vueCarte.getMasquerHypothese().setOnAction(controleurImagePions);
        vueCarte.getAfficherPresences().setOnAction(controleurImagePions);
        vueCarte.getAfficherAbsences().setOnAction(controleurImagePions);

        vueRegle.getRetour().setOnAction(controleurQuitterRegleFilm);
        for (TextCase tc : vueTableau.getListeCases()) {
            if (!tc.getInfo().split(" - ")[1].equals("1")) {
                tc.setOnMouseClicked(controleurChoixTableau);
            }
        }
        vuePoseQuestion.getRetour().setOnAction(controleurPoseQuestion);
        vuePoseQuestion.getAnnuler().setOnAction(controleurPoseQuestion);
        vuePoseQuestion.getValider().setOnAction(controleurPoseQuestion);
        for (Button b : vuePoseQuestion.getLieuButtons()){
            b.setOnAction(controleurPoseQuestion);
        }
        for (Button b : vuePoseQuestion.getTempsButtons()){
            b.setOnAction(controleurPoseQuestion);
        }
        for (Button b : vuePoseQuestion.getPersonnageButtons()){
            b.setOnAction(controleurPoseQuestion);
        }
        vueDeduction.getRetour().setOnAction(controleurDeduction);
        vueDeduction.getAnnuler().setOnAction(controleurDeduction);
        vueDeduction.getValider().setOnAction(controleurDeduction);
        for (Button b : vueDeduction.getLieuButtons()){
            b.setOnAction(controleurDeduction);
        }
        for (Button b : vueDeduction.getTempsButtons()){
            b.setOnAction(controleurDeduction);
        }
        for (Button b : vueDeduction.getPersonnageButtons()){
            b.setOnAction(controleurDeduction);
        }
        for (Pion p : vueCarte.getPions()){
            p.setOnDragDone(controleurChoixCarte);
        }
        vuePopUpDeduction.getQuitter().setOnAction(controleurPopUpDeduction);
        vuePopUpDeduction.getVoirFilm().setOnAction(controleurPopUpDeduction);
        vuePopUpQuitter.getAnnuler().setOnAction(controleurPopUpQuitter);
        vuePopUpQuitter.getValider().setOnAction(controleurPopUpQuitter);
        vuePopUpDemanderIndice.getAnnuler().setOnAction(controleurPopUpDemanderIndice);
        vuePopUpDemanderIndice.getValider().setOnAction(controleurPopUpDemanderIndice);
        vuePopUpPoseQuestion.getContinuer().setOnAction(controleurPopUpPoseQuestion);

        // Enregistrement des observateurs
        // Modele Jeu
        modeleJeu.enregistrerObservateur(vueCarte);
        modeleJeu.enregistrerObservateur(vuePoseQuestion);
        modeleJeu.enregistrerObservateur(vueTableau);
        modeleJeu.enregistrerObservateur(vueDeduction);
        modeleJeu.enregistrerObservateur(vuePopUpDeduction);
        modeleJeu.enregistrerObservateur(vuePopUpQuitter);
        modeleJeu.enregistrerObservateur(vuePopUpDemanderIndice);
        modeleJeu.enregistrerObservateur(vueRegle);
        modeleJeu.enregistrerObservateur(vuePopUpPoseQuestion);

        // Modele Notes
        modeleJeu.getModeleNotes().enregistrerObservateur(vueCarte);
        modeleJeu.getModeleNotes().enregistrerObservateur(vueTableau);

        // Modele Question Deduction
        modeleJeu.getModeleQuestionDeduction().enregistrerObservateur(vueDeduction);
        modeleJeu.getModeleQuestionDeduction().enregistrerObservateur(vuePopUpDeduction);
        modeleJeu.getModeleQuestionDeduction().enregistrerObservateur(vuePoseQuestion);
        modeleJeu.getModeleQuestionDeduction().enregistrerObservateur(vuePopUpPoseQuestion);

        // Modele IA
        modeleJeu.getModeleIA().enregistrerObservateur(vueDeductionIA);
        modeleJeu.getModeleIA().enregistrerObservateur(vuePopUpDemanderIndice);

        modeleAccueil.enregistrerObservateur(vueAccueil);
        afficherVuePrincipale();

    }

    private void afficherVuePrincipale() {
        BorderPane bp = new BorderPane(vueCarte);
        Scene scene = new Scene(bp, stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
        stage.show();

        // Affichage de la pop-up d'énigme
        vuePopUpEnigme.afficherPopUp(ModeleJeu.getPartie().getEnquete());
    }

    public void initialiserAvecIA() {
        // Choix du type d'enquête
        Alert choixAlert = new Alert(Alert.AlertType.NONE);
        choixAlert.setTitle("Choix de l'enquête pour l'IA");
        choixAlert.setHeaderText("L'IA Joueuse va résoudre une enquête.\nQuel type de scénario souhaitez-vous utiliser ?");

        ButtonType btnClassique = new ButtonType("Enquête classique");
        ButtonType btnGeneree = new ButtonType("Enquête générée");
        ButtonType btnAnnuler = ButtonType.CANCEL;

        choixAlert.getButtonTypes().setAll(btnClassique, btnGeneree, btnAnnuler);

        Optional<ButtonType> resultat = choixAlert.showAndWait();
        if (resultat.isEmpty()) {
            return;
        }

        String fichierJson;

        if (resultat.get() == btnGeneree) {
            try {
                Partie partieGeneree = GenerateurScenario.genererScenario();
                GenerateurScenario.exporterJson(partieGeneree, "data/enquete_generee.json");
                fichierJson = "data/enquete_generee.json";
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        } else if (resultat.get() == btnClassique) {
            fichierJson = "data/enquete_base.json";
        } else {
            return;
        }

        // Création du modèle et lancement de l'IA Joueuse
        Partie partie = JsonReader.lirePartieDepuisJson(fichierJson);
        ModeleJeu modeleJeu = new ModeleJeu(partie);
        ControleurIAJoueuse controleurIA = new ControleurIAJoueuse(modeleJeu);
        controleurIA.handle(new ActionEvent());
    }
}
