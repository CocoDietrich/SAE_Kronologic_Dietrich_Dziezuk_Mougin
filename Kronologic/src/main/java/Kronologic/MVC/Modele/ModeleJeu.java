package Kronologic.MVC.Modele;

import Kronologic.IA.IAAssistance.IAAssistanceChocoSolver;
import Kronologic.IA.IAAssistance.IAAssistanceHeuristique;
import Kronologic.IA.IADeduction.IADeductionChocoSolver;
import Kronologic.IA.IADeduction.IADeductionHeuristique;
import Kronologic.Jeu.Elements.*;
import Kronologic.Jeu.Images;
import Kronologic.Jeu.Indice.Indice;
import Kronologic.Jeu.Indice.IndicePersonnage;
import Kronologic.Jeu.Indice.IndiceTemps;
import Kronologic.Jeu.Partie;
import Kronologic.MVC.Controleur.Accueil.ControleurInitialisation;
import Kronologic.MVC.Controleur.Accueil.ControleurQuitterJeu;
import Kronologic.MVC.Vue.*;
import Kronologic.MVC.Vue.PopUps.VuePopUpDeduction;
import Kronologic.MVC.Vue.PopUps.VuePopUpDemanderIndice;
import Kronologic.MVC.Vue.PopUps.VuePopUpPoseQuestion;
import Kronologic.MVC.Vue.PopUps.VuePopUpQuitter;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.util.*;

public class ModeleJeu implements Sujet {

    private final List<Observateur> observateurs;
    private static Partie partie;
    private boolean vueCarte;
    private final IADeductionChocoSolver iaDeductionChocoSolver;
    private final IADeductionHeuristique iaDeductionHeuristique;
    private final IAAssistanceChocoSolver iaAssistanceChocoSolver;
    private final IAAssistanceHeuristique iaAssistanceHeuristique;

    public ModeleJeu(Partie partie) {
        this.observateurs = new ArrayList<>();
        ModeleJeu.partie = partie;
        this.vueCarte = true;
        this.iaDeductionChocoSolver = new IADeductionChocoSolver(partie);
        this.iaDeductionHeuristique = new IADeductionHeuristique(partie);
        this.iaAssistanceChocoSolver = new IAAssistanceChocoSolver(partie);
        this.iaAssistanceHeuristique = new IAAssistanceHeuristique(partie);
    }

    // Méthode permettant de retourner à la vue de la carte
    public void retourVueCarte(Stage stage) {
        VueCarte vueCarte = null;
        for (Observateur o : observateurs) {
            if (o instanceof VueCarte) {
                vueCarte = (VueCarte) o;
                break;
            }
        }

        notifierObservateurs();
        BorderPane bp = new BorderPane(vueCarte);
        Scene scene = new Scene(bp, stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
        stage.show();
    }

    // Méthode permettant de retourner à la vue de la carte
    public void retourVueTableau(Stage stage) {
        VueTableau vueTableau = null;
        for (Observateur o : observateurs) {
            if (o instanceof VueTableau) {
                vueTableau = (VueTableau) o;
                break;
            }
        }

        notifierObservateurs();
        BorderPane bp = new BorderPane(vueTableau);
        Scene scene = new Scene(bp, stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
        stage.show();
    }

    // Méthode permettant de stocker le lieu choisi pour la question posée ou la déduction faite par le joueur
    public void setLieuChoisi(Lieu lieu, Observateur vue) {
        assert vue != null;
        if (vue instanceof VuePoseQuestion vuePoseQuestion) {
            vuePoseQuestion.lieuChoisi = lieu;
        } else {
            VueDeduction vueDeduction = (VueDeduction) vue;
            vueDeduction.lieuMeurtre = lieu;
        }
    }

    // Méthode permettant de stocker le temps choisi pour la question posée ou la déduction faite par le joueur
    public void setTempsChoisi(Temps temps, Observateur vue) {
        assert vue != null;
        if (vue instanceof VuePoseQuestion) {
            VuePoseQuestion vuePoseQuestion = (VuePoseQuestion) vue;
            vuePoseQuestion.tempsChoisi = temps;
        } else {
            VueDeduction vueDeduction = (VueDeduction) vue;
            vueDeduction.tempsMeurtre = temps;
        }
    }

    // Méthode permettant de stocker le personnage choisi pour la question posée ou la déduction faite par le joueur
    public void setPersonnageChoisi(Personnage personnage, Observateur vue) {
        assert vue != null;
        if (vue instanceof VuePoseQuestion) {
            VuePoseQuestion vuePoseQuestion = (VuePoseQuestion) vue;
            vuePoseQuestion.personnageChoisi = personnage;
        } else {
            VueDeduction vueDeduction = (VueDeduction) vue;
            vueDeduction.meurtrier = personnage;
        }
    }

    public void changerAffichage(Stage stage) {
        this.vueCarte = !this.vueCarte;
        if (this.vueCarte) {
            retourVueCarte(stage);
        } else {
            retourVueTableau(stage);
        }
    }

    public Indice poserQuestion(Stage stage) {
        VuePoseQuestion vuePoseQuestion = null;
        for (Observateur o : observateurs) {
            if (o instanceof VuePoseQuestion) {
                vuePoseQuestion = (VuePoseQuestion) o;
                break;
            }
        }

        assert vuePoseQuestion != null;
        Indice i;
        if (vuePoseQuestion.personnageChoisi != null) {
            i = partie.poserQuestionPersonnage(vuePoseQuestion.lieuChoisi, vuePoseQuestion.personnageChoisi);

            // Ajouter contraintes publiques et privées
            IndicePersonnage ip = (IndicePersonnage) i;
            iaDeductionChocoSolver.poserQuestionPersonnage(
                    ip.getPersonnage(), ip.getLieu(), ip.getInfoPublic(), ip.getInfoPrive()
            );
            iaDeductionHeuristique.poserQuestionPersonnage(
                    ip.getPersonnage(), ip.getLieu(), ip.getInfoPublic(), ip.getInfoPrive()
            );
        } else {
            i = partie.poserQuestionTemps(vuePoseQuestion.lieuChoisi, vuePoseQuestion.tempsChoisi);

            // Ajouter contraintes publiques et privées
            IndiceTemps it = (IndiceTemps) i;
            iaDeductionChocoSolver.poserQuestionTemps(
                    it.getLieu(), it.getTemps(), it.getInfoPublic(), it.getInfoPrive()
            );
            iaDeductionHeuristique.poserQuestionTemps(
                    it.getLieu(), it.getTemps(), it.getInfoPublic(), it.getInfoPrive()
            );
        }

        partie.ajouterIndice(i);
        notifierObservateurs();
        System.out.println("Réponse à la question posée : " + i);

        VuePopUpPoseQuestion vuePopUpPoseQuestion = null;
        for (Observateur o : observateurs) {
            if (o instanceof VuePopUpPoseQuestion) {
                vuePopUpPoseQuestion = (VuePopUpPoseQuestion) o;
                break;
            }
        }
        assert vuePopUpPoseQuestion != null;
        vuePopUpPoseQuestion.afficherPopUp(i);
        if (isVueCarte()) {
            retourVueCarte(stage);
        } else {
            retourVueTableau(stage);
        }
        return i;
    }

    // Méthode permettant d'afficher la vue de pose de question
    public void visualiserPoseQuestion(Stage stage) {
        // On récupère la vuePoseQuestion dans la liste des observateurs
        VuePoseQuestion vuePoseQuestion = null;
        for (Observateur o : observateurs) {
            if (o instanceof VuePoseQuestion) {
                vuePoseQuestion = (VuePoseQuestion) o;
                break;
            }
        }

        BorderPane bp = new BorderPane(vuePoseQuestion);

        Scene scene = new Scene(bp, stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
        stage.show();
    }

    public void faireDeduction() {
        VueDeduction vueDeduction = null;
        for (Observateur o : observateurs) {
            if (o instanceof VueDeduction) {
                vueDeduction = (VueDeduction) o;
                break;
            }
        }
        assert vueDeduction != null;
        boolean resultat = partie.faireDeduction(vueDeduction.lieuMeurtre, vueDeduction.meurtrier, vueDeduction.tempsMeurtre);

        VuePopUpDeduction vuePopUpDeduction = null;
        for (Observateur o : observateurs) {
            if (o instanceof VuePopUpDeduction) {
                vuePopUpDeduction = (VuePopUpDeduction) o;
                break;
            }
        }
        assert vuePopUpDeduction != null;
        vuePopUpDeduction.afficherPopUp(resultat, partie.verifierLoupe());

        notifierObservateurs();
    }

    public void visualiserDeduction(Stage stage) {
        // On récupère la vuePoseQuestion dans la liste des observateurs
        VueDeduction vueDeduction = null;
        for (Observateur o : observateurs) {
            if (o instanceof VueDeduction) {
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
    public void ajouterNote(Lieu l, Temps t, Personnage p) {
        Note n = new Note(l, t, p);
        VueCarte vueCarte = null;
        for (Observateur o : observateurs) {
            if (o instanceof VueCarte) {
                vueCarte = (VueCarte) o;
                break;
            }
        }
        assert vueCarte != null;

        Pion pion = new Pion(n,
                Objects.requireNonNull(Images.Personnages.get(Images.Personnages.getPersonnages().indexOf(p.getNom()))).getUrl());
        pion.setUserData(t.getValeur() + "-" + l.getNom() + "-");

        ajouterPion(n, Objects.requireNonNull(Images.Personnages.get(Images.Personnages.getPersonnages().indexOf(p.getNom()))), 0, 0);


        vueCarte.pions.add(pion);
        notifierObservateurs();
    }

    // Méthode permettant de stocker les déplacements de pions de nombers du joueur
    public void ajouterNote(Lieu l, Temps t, int nbPersonnages) {
        Note n = new Note(l, t, nbPersonnages);
        VueCarte vueCarte = null;
        for (Observateur o : observateurs) {
            if (o instanceof VueCarte) {
                vueCarte = (VueCarte) o;
                break;
            }
        }
        assert vueCarte != null;

        // On ajoute la note à la liste des notes
        Pion pion = new Pion(n,
                Objects.requireNonNull(Images.Nombre.get(Images.Nombre.getNombres().indexOf(nbPersonnages)+1)).getUrl());
        pion.setUserData(t.getValeur() + "-" + l.getNom() + "-");

        ajouterPion(n, Objects.requireNonNull(Images.Nombre.get(Images.Nombre.getNombres().indexOf(nbPersonnages)+1)), 0, 0);

        vueCarte.pions.add(pion);
        notifierObservateurs();
    }

    public void modifierNote(Lieu l, Temps t, Personnage p, boolean absence, boolean hypothese) {
        // On retrouve la note correspondante au pion placé
        Note n = null;
        for (Note note : partie.getGestionnaireNotes().getNotes()) {
            if (note.getLieu().getNom().equals(l.getNom())
                    && note.getTemps().getValeur() == t.getValeur()
                    && note.getPersonnage().getNom().equals(p.getNom())) {
                n = note;
                break;
            }
        }

        VueCarte vueCarte = null;
        for (Observateur o : observateurs) {
            if (o instanceof VueCarte) {
                vueCarte = (VueCarte) o;
                break;
            }
        }
        assert vueCarte != null;

        // On modifie la note
        partie.modifierNote(n, absence, hypothese);

        for (Pion pion : vueCarte.pions) {
            if (pion.getNote() != null && pion.getNote().equals(n)) {
                pion.getNote().setEstAbsence(absence);
                pion.getNote().setEstHypothese(hypothese);
            }
        }

        notifierObservateurs();
    }

    public void modifierNote(Lieu l, Temps t, int nbPersonnage, boolean absence, boolean hypothese) {
        // On retrouve la note correspondante au pion placé
        Note n = null;
        for (Note note : partie.getGestionnaireNotes().getNotes()) {
            if (note.getLieu().getNom().equals(l.getNom())
                    && note.getTemps().getValeur() == t.getValeur()
                    && note.getNbPersonnages() == nbPersonnage) {
                n = note;
                break;
            }
        }

        VueCarte vueCarte = null;
        for (Observateur o : observateurs) {
            if (o instanceof VueCarte) {
                vueCarte = (VueCarte) o;
                break;
            }
        }
        assert vueCarte != null;

        // On modifie la note
        partie.modifierNote(n, absence, hypothese);

        for (Pion pion : vueCarte.pions) {
            if (pion.getNote() != null && pion.getNote().equals(n)) {
                pion.getNote().setEstAbsence(absence);
                pion.getNote().setEstHypothese(hypothese);
            }
        }

        notifierObservateurs();
    }

    // Méthode permettant de supprimer une note du joueur
    public void supprimerNote(Lieu l, Temps t, Personnage p) {
        // On retrouve la note correspondante au pion placé
        Note n = null;
        for (Note note : partie.getGestionnaireNotes().getNotes()) {
            if (note.getLieu().getNom().equals(l.getNom())
                    && note.getTemps().getValeur() == t.getValeur()
                    && note.getPersonnage().getNom().equals(p.getNom())) {
                n = note;
                break;
            }
        }

        VueCarte vueCarte = null;
        for (Observateur o : observateurs) {
            if (o instanceof VueCarte) {
                vueCarte = (VueCarte) o;
                break;
            }
        }
        assert vueCarte != null;

        for (Pion pion : vueCarte.pions) {
            if (pion.getNote() != null && pion.getNote().equals(n)) {
                vueCarte.pions.remove(pion);
                vueCarte.getChildren().remove(pion);
                supprimerPion(pion);
                for (Polygon zone : vueCarte.zonesContenantPions) {
                    if (zone.getUserData().equals(pion.getUserData())) {
                        vueCarte.zonesContenantPions.remove(zone);
                        break;
                    }
                }
                break;
            }
        }

        notifierObservateurs();
    }

    public void supprimerNote(Lieu l, Temps t, int nbPersonnages) {
        // On retrouve la note correspondante au pion placé
        Note n = null;
        for (Note note : partie.getGestionnaireNotes().getNotes()) {
            if (note.getLieu().getNom().equals(l.getNom())
                    && note.getTemps().getValeur() == t.getValeur()
                    && note.getNbPersonnages() == nbPersonnages) {
                n = note;
                break;
            }
        }

        VueCarte vueCarte = null;
        for (Observateur o : observateurs) {
            if (o instanceof VueCarte) {
                vueCarte = (VueCarte) o;
                break;
            }
        }
        assert vueCarte != null;

        for (Pion pion : vueCarte.pions) {
            if (pion.getNote() != null && pion.getNote().equals(n)) {
                vueCarte.pions.remove(pion);
                vueCarte.getChildren().remove(pion);
                supprimerPion(pion);
                for (Polygon zone : vueCarte.zonesContenantPions) {
                    if (zone.getUserData().equals(pion.getUserData())) {
                        vueCarte.zonesContenantPions.remove(zone);
                        break;
                    }
                }
                break;
            }
        }

        notifierObservateurs();
    }

    public void ajouterPion(Note note, Image image, int x, int y) {
        Pion pion = new Pion(note, image.getUrl());
        pion.deplacerPion(x, y);
        partie.ajouterPion(pion);
    }

    public void deplacerPion(Pion pion, Lieu nouveauLieu, Temps nouveauTemps, int x, int y) {
        partie.deplacerPion(pion, nouveauLieu, nouveauTemps, x, y);
    }

    public void supprimerPion(Pion pion) {
        partie.supprimerPion(pion);
    }

    // Méthode pour afficher les déductions de l'IA ChocoSolver
    public String voirDeductionIAChocoSolver() {
        return iaDeductionChocoSolver.afficherHistoriqueDeduction();
    }

    // Méthode pour afficher les déductions de l'IA Heuristique
    public String voirDeductionIAHeuristique() {
        return iaDeductionHeuristique.afficherHistoriqueDeduction();
    }

    public void demanderIndice() {
        //TODO
    }

    public void visualiserFilmJoueur(Stage stage) {
        VueFilmJoueur vueFilmJoueur = new VueFilmJoueur(this);
        for (Observateur o : observateurs) {
            if (o instanceof VueFilmJoueur) {
                vueFilmJoueur = (VueFilmJoueur) o;
                break;
            }
        }

        notifierObservateurs();

        BorderPane bp = new BorderPane(vueFilmJoueur);

        Scene scene = new Scene(bp, stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
        stage.show();

        // Récupération de l'historique
        Map<Integer, List<Note>> historique = partie.getHistorique();

        // Tri des tours pour garantir l'ordre croissant
        List<Integer> tours = new ArrayList<>(historique.keySet());
        Collections.sort(tours);

        // Parcourir chaque tour dans l'ordre
//        if (tours.isEmpty()) {
//            System.out.println("Aucune note n'a été posé.");
//            return;
//        }
        for (int i = 0; i <= tours.getLast(); i++) {
            System.out.println("--------------------");
            // Afficher le numéro du tour
            System.out.println("Tour " + i + " :");

            // Afficher les notes associées au tour
            if (!historique.containsKey(i)) {
                continue;
            }
            List<Note> notes = historique.get(i);
            for (Note note : notes) {
                System.out.println(note);
            }
        }
    }

    public void visualiserFilmRealite(Stage stage) {
        VueFilmRealite vueFilmRealite = new VueFilmRealite(this);
        for (Observateur o : observateurs) {
            if (o instanceof VueFilmRealite) {
                vueFilmRealite = (VueFilmRealite) o;
                break;
            }
        }

        BorderPane bp = new BorderPane(vueFilmRealite);

        Scene scene = new Scene(bp, stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
        stage.show();

        // Récupération du déroulement de la partie
        List<Realite> positions = partie.getDeroulement().getListePositions();

        for (int i = 1; i < 7; i++) {
            System.out.println("--------------------");
            System.out.println("Tour " + i + " :");
            for (Realite r : positions) {
                if (r.getTemps().getValeur() == i) {
                    System.out.println(r.toString());
                }
            }
        }
    }

    public void actualiserFilmRealite() {
        VueFilmRealite vueFilmRealite = null;
        for (Observateur o : observateurs) {
            if (o instanceof VueFilmRealite) {
                vueFilmRealite = (VueFilmRealite) o;
                break;
            }
        }

        assert vueFilmRealite != null;

        for (Node node : vueFilmRealite.getChildren()) {
            if (node instanceof HBox h) {
                if (h.getId() != null) {
                    if (h.getId().equals("carte")) {
                        vueFilmRealite.getChildren().remove(h);
                        // On met à jour la carte
                        HBox nouvelleHbox = vueFilmRealite.afficherCarte(this);
                        vueFilmRealite.add(nouvelleHbox, 1, 1);
                        break;
                    }
                }
            }
        }
        vueFilmRealite.actualiser();
    }

    public void visualiserRegle(Stage stage) {
        VueRegle vueRegle = new VueRegle();
        for (Observateur o : observateurs) {
            if (o instanceof VueRegle) {
                vueRegle = (VueRegle) o;
                break;
            }
        }


        BorderPane bp = new BorderPane(vueRegle);

        Scene scene = new Scene(bp, stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
        stage.show();
    }

    public void valider() {
        //TODO
    }

    // Méthode affichant le pop-up de confirmation de quitter la partie
    public void afficherPopUpQuitter() {
        VuePopUpQuitter vuePopUpQuitter = null;

        for (Observateur o : observateurs) {
            if (o instanceof VuePopUpQuitter) {
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

        for (Observateur o : observateurs) {
            if (o instanceof VuePopUpDemanderIndice) {
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
        for (Observateur o : observateurs) {
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
