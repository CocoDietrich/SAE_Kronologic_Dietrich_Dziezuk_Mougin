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

    // Vues
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

        if (resultat.isPresent() && resultat.get() == btnGeneree) {
            try {
                Partie partieGeneree = GenerateurScenario.genererScenario();
                GenerateurScenario.exporterJson(partieGeneree, "data/enquete_generee.json");
                fichierJson = "data/enquete_generee.json";
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        } else if (resultat.isPresent() && resultat.get() == btnClassique) {
            fichierJson = "data/enquete_base.json";
        } else {
            return;
        }
        // Création du modèle
        ModeleJeu modeleJeu = new ModeleJeu(JsonReader.lirePartieDepuisJson(fichierJson));
        ModeleAccueil modeleAccueil = new ModeleAccueil();

        // Création des vues
        this.vuePopUpEnigme = new VuePopUpEnigme(stage);
        this.vueCarte = new VueCarte(modeleJeu);
        VueTableau vueTableau = new VueTableau();
        VuePoseQuestion vuePoseQuestion = new VuePoseQuestion();
        VueDeduction vueDeduction = new VueDeduction();
        VueDeductionIA vueDeductionIA = new VueDeductionIA();
        VuePopUpDeduction vuePopUpDeduction = new VuePopUpDeduction(stage, new Stage());
        VuePopUpQuitter vuePopUpQuitter = new VuePopUpQuitter(stage);
        VueRegle vueRegle = new VueRegle();
        VuePopUpDemanderIndice vuePopUpDemanderIndice = new VuePopUpDemanderIndice(stage);
        VuePopUpPoseQuestion vuePopUpPoseQuestion = new VuePopUpPoseQuestion(stage);
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
        ControleurPopUpPoseQuestion controleurPopUpPoseQuestion = new ControleurPopUpPoseQuestion(modeleJeu);
        ControleurVisualiserFilmJoueur controleurVisualiserFilmJoueur = new ControleurVisualiserFilmJoueur(modeleJeu);
        ControleurVisualiserFilmRealite controleurVisualiserFilmRealite = new ControleurVisualiserFilmRealite(modeleJeu);
        ControleurImagePions controleurImagePions = new ControleurImagePions(modeleJeu);

        // Assignation des controleurs aux vues
        vueCarte.retour.setOnAction(controleurQuitter);
        vueCarte.poserQuestion.setOnAction(controleurVisualiserPoseQuestion);
        vueCarte.changerAffichage.setOnAction(controleurAffichage);
        vueCarte.regle.setOnAction(controleurVisualiserRegle);
        vueCarte.demanderIndice.setOnAction(controleurDemanderIndice);
        vueCarte.deductionIA.setOnAction(controleurVoirDeductionIA);
        vueCarte.filmJoueur.setOnAction(controleurVisualiserFilmJoueur);
        vueCarte.filmRealite.setOnAction(controleurVisualiserFilmRealite);
        vueCarte.hypothese.setOnAction(controleurImagePions);
        vueCarte.absence.setOnAction(controleurImagePions);
        vueCarte.masquerHypothese.setOnAction(controleurImagePions);
        vueCarte.afficherPresences.setOnAction(controleurImagePions);
        vueCarte.afficherAbsences.setOnAction(controleurImagePions);
        vueTableau.retour.setOnAction(controleurQuitter);
        vueTableau.poserQuestion.setOnAction(controleurVisualiserPoseQuestion);
        vueTableau.changerAffichage.setOnAction(controleurAffichage);
        vueTableau.regle.setOnAction(controleurVisualiserRegle);
        vueTableau.deductionIA.setOnAction(controleurVoirDeductionIA);
        vueTableau.filmJoueur.setOnAction(controleurVisualiserFilmJoueur);
        vueTableau.filmRealite.setOnAction(controleurVisualiserFilmRealite);
        vueCarte.faireDeduction.setOnAction(controleurVisualiserDeduction);
        vueTableau.faireDeduction.setOnAction(controleurVisualiserDeduction);
        vueRegle.retour.setOnAction(controleurQuitterRegleFilm);
        for (TextCase tc : vueTableau.listeCases) {
            if (!tc.getInfo().split(" - ")[1].equals("1")) {
                tc.setOnMouseClicked(controleurChoixTableau);
            }
        }
        vuePoseQuestion.retour.setOnAction(controleurPoseQuestion);
        vuePoseQuestion.annuler.setOnAction(controleurPoseQuestion);
        vuePoseQuestion.valider.setOnAction(controleurPoseQuestion);
        for (Button b : vuePoseQuestion.lieuButtons){
            b.setOnAction(controleurPoseQuestion);
        }
        for (Button b : vuePoseQuestion.tempsButtons){
            b.setOnAction(controleurPoseQuestion);
        }
        for (Button b : vuePoseQuestion.personnageButtons){
            b.setOnAction(controleurPoseQuestion);
        }
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
        vuePopUpDeduction.voirFilm.setOnAction(controleurPopUpDeduction);
        vuePopUpQuitter.annuler.setOnAction(controleurPopUpQuitter);
        vuePopUpQuitter.valider.setOnAction(controleurPopUpQuitter);
        vuePopUpDemanderIndice.annuler.setOnAction(controleurPopUpDemanderIndice);
        vuePopUpDemanderIndice.valider.setOnAction(controleurPopUpDemanderIndice);
        vuePopUpPoseQuestion.continuer.setOnAction(controleurPopUpPoseQuestion);

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
        vueAccueil.IAJoueuse.setOnAction(e -> modeleAccueil.initialiserPartie("IAJoueuse"));
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
        Partie partie = JsonReader.lirePartieDepuisJson("data/enquete_base.json");
        ModeleJeu modeleJeu = new ModeleJeu(partie);
        ControleurIAJoueuse controleurIA = new ControleurIAJoueuse(modeleJeu);
        controleurIA.handle(new ActionEvent());
    }

}
