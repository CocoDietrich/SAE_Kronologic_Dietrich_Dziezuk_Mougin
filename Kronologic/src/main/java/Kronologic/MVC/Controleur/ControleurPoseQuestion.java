package Kronologic.MVC.Controleur;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.MVC.Modele.ModeleJeu;
import Kronologic.MVC.Vue.Observateur;
import Kronologic.MVC.Vue.VuePoseQuestion;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.List;

public class ControleurPoseQuestion implements EventHandler<ActionEvent> {

    private final ModeleJeu modeleJeu;

    // Constantes pour les identifiants
    private static final String RETOUR = "Retour";
    private static final String VALIDER = "Valider";
    private static final String ANNULER = "Annuler mes choix";

    public ControleurPoseQuestion(ModeleJeu modeleJeu) {
        this.modeleJeu = modeleJeu;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        String id = ((Button) actionEvent.getSource()).getId();

        VuePoseQuestion vuePoseQuestion = null;
        // On récupère la vue de la pose de question
        for (Observateur o : modeleJeu.getObservateurs()) {
            if (o instanceof VuePoseQuestion) {
                vuePoseQuestion = (VuePoseQuestion) o;
                break;
            }
        }

        assert vuePoseQuestion != null;

        switch (id) {
            case RETOUR:
                if (this.modeleJeu.estVueCarte()){
                    this.modeleJeu.retourVueCarte(stage);
                }
                else {
                    this.modeleJeu.retourVueTableau(stage);
                }
                break;
            case VALIDER:
                validerChoix(stage, vuePoseQuestion);
                break;
            case ANNULER:
                annulerChoix(vuePoseQuestion);
                break;
            default:
                traiterChoix(id, vuePoseQuestion);
                break;
        }
    }

    private void validerChoix(Stage stage, VuePoseQuestion vuePoseQuestion) {
        reactiverBoutons(vuePoseQuestion.getLieuButtons());
        reactiverBoutons(vuePoseQuestion.getTempsButtons());
        reactiverBoutons(vuePoseQuestion.getPersonnageButtons());
        modeleJeu.getModeleIA().ajoutContraintesQuestion(modeleJeu.getModeleQuestionDeduction().poserQuestion(stage, modeleJeu));
    }

    private void reactiverBoutons(List<Button> buttons) {
        for (Button b : buttons) {
            b.setDisable(false);
        }
    }

    private void annulerChoix(VuePoseQuestion vuePoseQuestion) {
        reactiverBoutons(vuePoseQuestion.getLieuButtons());
        reactiverBoutons(vuePoseQuestion.getTempsButtons());
        reactiverBoutons(vuePoseQuestion.getPersonnageButtons());
        modeleJeu.getModeleQuestionDeduction().setLieuChoisi(null, vuePoseQuestion);
        modeleJeu.getModeleQuestionDeduction().setTempsChoisi(null, vuePoseQuestion);
        modeleJeu.getModeleQuestionDeduction().setPersonnageChoisi(null, vuePoseQuestion);
    }

    private void traiterChoix(String id, VuePoseQuestion vuePoseQuestion) {
        if (id.startsWith("Lieu")) {
            traiterLieu(id, vuePoseQuestion);
        } else if (id.startsWith("temps")) {
            traiterTemps(id, vuePoseQuestion);
        } else {
            traiterPersonnage(id, vuePoseQuestion);
        }
    }

    private void traiterLieu(String id, VuePoseQuestion vuePoseQuestion) {
        String nomLieu = id.substring(id.indexOf("_") + 1, id.lastIndexOf("_"));
        int indexLieu = Integer.parseInt(id.substring(id.lastIndexOf("_") + 1));
        Lieu lieu = new Lieu(nomLieu, indexLieu, null);
        desactiverAutresBoutons(lieu, vuePoseQuestion.getLieuButtons(), null);
        modeleJeu.getModeleQuestionDeduction().setLieuChoisi(lieu, vuePoseQuestion);
    }

    private void traiterTemps(String id, VuePoseQuestion vuePoseQuestion) {
        int indexTemps = Integer.parseInt(id.substring(5));
        Temps temps = new Temps(indexTemps);
        desactiverAutresBoutons(temps, vuePoseQuestion.getTempsButtons(), vuePoseQuestion.getPersonnageButtons());
        modeleJeu.getModeleQuestionDeduction().setTempsChoisi(temps, vuePoseQuestion);
    }

    private void traiterPersonnage(String id, VuePoseQuestion vuePoseQuestion) {
        Personnage personnage = new Personnage(id);
        desactiverAutresBoutons(personnage, vuePoseQuestion.getPersonnageButtons(), vuePoseQuestion.getTempsButtons());
        modeleJeu.getModeleQuestionDeduction().setPersonnageChoisi(personnage, vuePoseQuestion);
    }

    private void desactiverAutresBoutons(Object choix, List<Button> buttons, List<Button> tempsOuPersonnage) {
        if (choix instanceof Lieu l){
            desactiverSelectionBouton(l.getNom(), buttons, tempsOuPersonnage);
        }
        else if (choix instanceof Temps t){
            desactiverSelectionBouton(String.valueOf(t.getValeur()), buttons, tempsOuPersonnage);
        }
        else if (choix instanceof Personnage p){
            desactiverSelectionBouton(p.getNom(), buttons, tempsOuPersonnage);
        }
    }

    private void desactiverSelectionBouton(String s, List<Button> buttons, List<Button> tempsOuPersonnage) {
        for (Button b : buttons) {
            if (!b.getId().contains(s)) {
                b.setDisable(true);
            }
        }
        if (tempsOuPersonnage != null) {
            for (Button b : tempsOuPersonnage){
                b.setDisable(true);
            }
        }
    }
}