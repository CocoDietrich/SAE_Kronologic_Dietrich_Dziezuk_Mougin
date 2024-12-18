package Kronologic.MVC.Modele;

import Kronologic.IA.IAAssistance.IAAssistanceChocoSolver;
import Kronologic.IA.IAAssistance.IAAssistanceHeuristique;
import Kronologic.IA.IADeduction.IADeductionChocoSolver;
import Kronologic.IA.IADeduction.IADeductionHeuristique;
import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Note;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.Jeu.Indice.Indice;
import Kronologic.Jeu.Partie;
import Kronologic.MVC.Controleur.Accueil.ControleurInitialisation;
import Kronologic.MVC.Controleur.Accueil.ControleurQuitterJeu;
import Kronologic.MVC.Vue.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ModeleJeu implements Sujet {

    private final List<Observateur> observateurs;
    private static Partie partie;
    private boolean vueCarte;
    private final IADeductionChocoSolver iaDeductionChocoSolver;
    private final IADeductionHeuristique iaDeductionHeuristique;
    private final IAAssistanceChocoSolver iaAssistanceChocoSolver;
    private final IAAssistanceHeuristique iaAssistanceHeuristique;

    public ModeleJeu(Partie partie){
        this.observateurs = new ArrayList<>();
        ModeleJeu.partie = partie;
        this.vueCarte = true;
        this.iaDeductionChocoSolver = new IADeductionChocoSolver(partie);
        this.iaDeductionHeuristique = new IADeductionHeuristique(partie);
        this.iaAssistanceChocoSolver = new IAAssistanceChocoSolver(partie);
        this.iaAssistanceHeuristique = new IAAssistanceHeuristique(partie);
    }

    // Méthode permettant de retourner à la vue de la carte
    public void retourVueCarte(Stage stage){
        VueCarte vueCarte = null;
        for (Observateur o : observateurs){
            if (o instanceof VueCarte){
                vueCarte = (VueCarte) o;
                break;
            }
        }

        BorderPane bp = new BorderPane(vueCarte);
        Scene scene = new Scene(bp, stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
        stage.show();
    }

    // Méthode permettant de retourner à la vue de la carte
    public void retourVueTableau(Stage stage){
        VueTableau vueTableau = null;
        for (Observateur o : observateurs){
            if (o instanceof VueTableau){
                vueTableau = (VueTableau) o;
                break;
            }
        }

        BorderPane bp = new BorderPane(vueTableau);
        Scene scene = new Scene(bp, stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
        stage.show();
    }

    // Méthode permettant de stocker le lieu choisi pour la question posée ou la déduction faite par le joueur
    public void setLieuChoisi(Lieu lieu, Observateur vue){
        assert vue != null;
        if (vue instanceof VuePoseQuestion){
            VuePoseQuestion vuePoseQuestion = (VuePoseQuestion) vue;
            vuePoseQuestion.lieuChoisi = lieu;
        } else {
            VueDeduction vueDeduction = (VueDeduction) vue;
            vueDeduction.lieuMeurtre = lieu;
        }
    }

    // Méthode permettant de stocker le temps choisi pour la question posée ou la déduction faite par le joueur
    public void setTempsChoisi(Temps temps, Observateur vue){
        assert vue != null;
        if (vue instanceof VuePoseQuestion){
            VuePoseQuestion vuePoseQuestion = (VuePoseQuestion) vue;
            vuePoseQuestion.tempsChoisi = temps;
        } else {
            VueDeduction vueDeduction = (VueDeduction) vue;
            vueDeduction.tempsMeurtre = temps;
        }
    }

    // Méthode permettant de stocker le personnage choisi pour la question posée ou la déduction faite par le joueur
    public void setPersonnageChoisi(Personnage personnage, Observateur vue){
        assert vue != null;
        if (vue instanceof VuePoseQuestion){
            VuePoseQuestion vuePoseQuestion = (VuePoseQuestion) vue;
            vuePoseQuestion.personnageChoisi = personnage;
        } else {
            VueDeduction vueDeduction = (VueDeduction) vue;
            vueDeduction.meurtrier = personnage;
        }
    }

    public void changerAffichage(Stage stage){
        this.vueCarte = !this.vueCarte;
        if (this.vueCarte){
            retourVueCarte(stage);
        } else {
            retourVueTableau(stage);
        }
    }

    public Indice poserQuestion(Stage stage){
        VuePoseQuestion vuePoseQuestion = null;
        for (Observateur o : observateurs){
            if (o instanceof VuePoseQuestion){
                vuePoseQuestion = (VuePoseQuestion) o;
                break;
            }
        }

        assert vuePoseQuestion != null;
        Indice i;
        if (vuePoseQuestion.personnageChoisi != null) {
            i = partie.poserQuestionPersonnage(vuePoseQuestion.lieuChoisi, vuePoseQuestion.personnageChoisi);

            // Ajouter contraintes publiques et privées
//            IndicePersonnage ip= (IndicePersonnage) i;
//            iaDeductionChocoSolver.ajouterContraintePersonnage(
//                    ip.getPersonnage(), ip.getLieu(), ip.getInfoPrive()
//            );
//            iaDeductionChocoSolver.ajouterContrainteNombreDePassages(
//                    ip.getPersonnage(), ip.getLieu(), ip.getInfoPublic()
//            );
        } else {
            i = partie.poserQuestionTemps(vuePoseQuestion.lieuChoisi, vuePoseQuestion.tempsChoisi);

            // Ajouter contraintes publiques et privées
//            IndiceTemps it = (IndiceTemps) i;
//            iaDeductionChocoSolver.ajouterContrainteTemps(
//                    it.getLieu(), it.getTemps(), it.getInfoPublic()
//            );
//
//            if (!it.getInfoPrive().equals("Rejouer")) {
//                iaDeductionChocoSolver.ajouterContraintePersonnage(
//                        new Personnage(it.getInfoPrive()), it.getLieu(), it.getTemps().getValeur()
//                );
//            }
        }

        partie.ajouterIndice(i);
        notifierObservateurs();
        System.out.println("Réponse à la question posée : " + i);
        if (isVueCarte()){
            retourVueCarte(stage);
        } else {
            retourVueTableau(stage);
        }
        return i;
    }

    // Méthode permettant d'afficher la vue de pose de question
    public void visualiserPoseQuestion(Stage stage){
        // On récupère la vuePoseQuestion dans la liste des observateurs
        VuePoseQuestion vuePoseQuestion = null;
        for (Observateur o : observateurs){
            if (o instanceof VuePoseQuestion){
                vuePoseQuestion = (VuePoseQuestion) o;
                break;
            }
        }

        BorderPane bp = new BorderPane(vuePoseQuestion);

        Scene scene = new Scene(bp, stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
        stage.show();
    }

    public boolean faireDeduction(){
        VueDeduction vueDeduction = null;
        for (Observateur o : observateurs){
            if (o instanceof VueDeduction){
                vueDeduction = (VueDeduction) o;
                break;
            }
        }
        assert vueDeduction != null;
        boolean resultat = partie.faireDeduction(vueDeduction.lieuMeurtre, vueDeduction.meurtrier, vueDeduction.tempsMeurtre);

        VuePopUpDeduction vuePopUpDeduction = null;
        for (Observateur o : observateurs){
            if (o instanceof VuePopUpDeduction){
                vuePopUpDeduction = (VuePopUpDeduction) o;
                break;
            }
        }
        assert vuePopUpDeduction != null;
        vuePopUpDeduction.afficherPopUp(resultat);

        if (resultat) {
            System.out.println("Victoire enregistrée dans le modèle.");
        } else {
            System.out.println("Défaite enregistrée dans le modèle.");
        }
        return resultat;
    }

    public void visualiserDeduction(Stage stage){
        // On récupère la vuePoseQuestion dans la liste des observateurs
        VueDeduction vueDeduction = null;
        for (Observateur o : observateurs){
            if (o instanceof VueDeduction){
                vueDeduction = (VueDeduction) o;
                break;
            }
        }

        BorderPane bp = new BorderPane(vueDeduction);

        Scene scene = new Scene(bp, stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
        stage.show();
    }

    // Méthode permettant de stocker les déplacements de pions de personnage du joueur
    public void ajouterNote(Lieu l, Temps t, Personnage p, boolean hypothese, boolean absence) {
        Note n = new Note(l, t, p);
        VueCarte vueCarte = null;
        for (Observateur o : observateurs){
            if (o instanceof VueCarte){
                vueCarte = (VueCarte) o;
                break;
            }
        }
        assert vueCarte != null;

        n.setEstHypothese(hypothese);
        n.setEstAbsence(absence);

        // On ajoute la note à la liste des notes
        partie.ajouterNote(n);
    }

    public void ajouterNote(Lieu l, Temps t, int nbPersonnages, boolean hypothese, boolean absence) {
        Note n = new Note(l, t, nbPersonnages);
        VueCarte vueCarte = null;
        for (Observateur o : observateurs){
            if (o instanceof VueCarte){
                vueCarte = (VueCarte) o;
                break;
            }
        }
        assert vueCarte != null;

        n.setEstHypothese(hypothese);
        n.setEstAbsence(absence);

        // On ajoute la note à la liste des notes
        partie.ajouterNote(n);
    }

    public void modifierNote(Lieu l, Temps t, Personnage p, boolean absence, boolean hypothese) {
        // On retrouve la note correspondante au pion placé
        Note n = null;
        for (Note note : partie.getGestionnaireNotes().getNotes()){
            if (note.getLieu().getNom().equals(l.getNom())
                    && note.getTemps().getValeur() == t.getValeur()
                    && note.getPersonnage().getNom().equals(p.getNom())){
                n = note;
                break;
            }
        }

        // On modifie la note
        partie.modifierNote(n, absence, hypothese);
    }

    public void modifierNote(Lieu l, Temps t, int nbPersonnage, boolean absence, boolean hypothese) {
        // On retrouve la note correspondante au pion placé
        Note n = null;
        for (Note note : partie.getGestionnaireNotes().getNotes()){
            if (note.getLieu().getNom().equals(l.getNom())
                    && note.getTemps().getValeur() == t.getValeur()
                    && note.getNbPersonnages() == nbPersonnage){
                n = note;
                break;
            }
        }

        // On modifie la note
        partie.modifierNote(n, absence, hypothese);
    }

    // Méthode permettant de supprimer une note du joueur
    public void supprimerNote(Lieu l, Temps t, Personnage p) {
        // On retrouve la note correspondante au pion placé
        Note n = null;
        for (Note note : partie.getGestionnaireNotes().getNotes()){
            if (note.getLieu().getNom().equals(l.getNom())
                    && note.getTemps().getValeur() == t.getValeur()
                    && note.getPersonnage().getNom().equals(p.getNom())){
                n = note;
                break;
            }
        }

        // On supprime la note de la liste des notes
        partie.supprimerNote(n);
    }

    public void supprimerNote(Lieu l, Temps t, int nbPersonnages) {
        // On retrouve la note correspondante au pion placé
        Note n = null;
        for (Note note : partie.getGestionnaireNotes().getNotes()){
            if (note.getLieu().getNom().equals(l.getNom())
                    && note.getTemps().getValeur() == t.getValeur()
                    && note.getNbPersonnages() == nbPersonnages){
                n = note;
                break;
            }
        }

        // On supprime la note de la liste des notes
        partie.supprimerNote(n);
    }

    public String voirDeductionIA(){
        return iaDeductionChocoSolver.afficherHistoriqueDeduction();
    }

    public void demanderIndice(){
        //TODO
    }

    public void visualiserFilm(){
        //TODO
    }

    public void visualiserRegle(Stage stage){
        VueRegle vueRegle = new VueRegle();
        for (Observateur o : observateurs){
            if (o instanceof VueRegle){
                vueRegle = (VueRegle) o;
                break;
            }
        }

        BorderPane bp = new BorderPane(vueRegle);

        Scene scene = new Scene(bp, stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
        stage.show();

    }

    public void valider(){
        //TODO
    }

    // Méthode affichant le pop-up de confirmation de quitter la partie
    public void afficherPopUpQuitter(){
        VuePopUpQuitter vuePopUpQuitter = null;

        for (Observateur o : observateurs){
            if (o instanceof VuePopUpQuitter){
                vuePopUpQuitter = (VuePopUpQuitter) o;
                break;
            }
        }

        assert vuePopUpQuitter != null;
        vuePopUpQuitter.afficherPopUp();
    }

    // Méthode permettant de quitter la partie
    public void quitter(String idBouton, Stage stage) {
        switch (idBouton) {
            case "retour":
                ModeleAccueil modeleAccueil = new ModeleAccueil();

                VueAccueil vueAccueil = new VueAccueil();

                modeleAccueil.enregistrerObservateur(vueAccueil);

                BorderPane bp = new BorderPane();
                bp.setCenter(vueAccueil);

                ControleurInitialisation controleurInitialisation = new ControleurInitialisation(modeleAccueil);
                vueAccueil.jouer.setOnAction(controleurInitialisation);
                vueAccueil.IAJoueuse.setOnAction(controleurInitialisation);

                ControleurQuitterJeu controleurQuitterJeu = new ControleurQuitterJeu(modeleAccueil);
                vueAccueil.quitter.setOnAction(controleurQuitterJeu);


                Scene scene = new Scene(bp, stage.getWidth(), stage.getHeight());
                stage.setScene(scene);
                break;
            case "quitter":
                System.exit(0);
                break;
        }
    }

    // Méthode affichant le pop-up de demande d'indice à l'IA
    public void afficherPopUpDemanderIndice() {
        VuePopUpDemanderIndice vuePopUpDemanderIndice = null;

        for (Observateur o : observateurs){
            if (o instanceof VuePopUpDemanderIndice){
                vuePopUpDemanderIndice = (VuePopUpDemanderIndice) o;
                break;
            }
        }

        assert vuePopUpDemanderIndice != null;
        vuePopUpDemanderIndice.afficherPopUp();
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

    public List<Observateur> getObservateurs() {
        return observateurs;
    }

    public static Partie getPartie() {
        return partie;
    }

    public boolean isVueCarte() {
        return vueCarte;
    }
}
