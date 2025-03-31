package Kronologic.MVC.Controleur;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.MVC.Modele.ModeleJeu;
import Kronologic.MVC.Vue.Observateur;
import Kronologic.MVC.Vue.VueDeduction;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.List;

public class ControleurDeduction implements EventHandler<ActionEvent> {

    private final ModeleJeu modeleJeu;

    // Constantes pour les identifiants
    private static final String RETOUR = "Retour";
    private static final String VALIDER = "Valider";
    private static final String ANNULER = "Annuler mes choix";

    public ControleurDeduction(ModeleJeu modeleJeu) {
        this.modeleJeu = modeleJeu;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        String id = ((Button) actionEvent.getSource()).getId();

        VueDeduction vueDeduction = null;
        // On récupère la vue de la pose de question
        for (Observateur o : modeleJeu.getObservateurs()) {
            if (o instanceof VueDeduction) {
                vueDeduction = (VueDeduction) o;
                break;
            }
        }

        assert vueDeduction != null;

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
                validerChoix(vueDeduction);
                break;
            case ANNULER:
                annulerChoix(vueDeduction);
                break;
            default:
                traiterChoix(id, vueDeduction);
                break;
        }
    }

    private void validerChoix(VueDeduction vueDeduction) {
        reactiverBoutons(vueDeduction.lieuButtons);
        reactiverBoutons(vueDeduction.tempsButtons);
        reactiverBoutons(vueDeduction.personnageButtons);
        modeleJeu.getModeleQuestionDeduction().faireDeduction();
    }

    private void reactiverBoutons(List<Button> buttons) {
        for (Button b : buttons) {
            b.setDisable(false);
        }
    }

    private void annulerChoix(VueDeduction vueDeduction) {
        reactiverBoutons(vueDeduction.lieuButtons);
        reactiverBoutons(vueDeduction.tempsButtons);
        reactiverBoutons(vueDeduction.personnageButtons);
        modeleJeu.getModeleQuestionDeduction().setLieuChoisi(null, vueDeduction);
        modeleJeu.getModeleQuestionDeduction().setTempsChoisi(null, vueDeduction);
        modeleJeu.getModeleQuestionDeduction().setPersonnageChoisi(null, vueDeduction);
    }

    private void traiterChoix(String id, VueDeduction vueDeduction) {
        if (id.startsWith("Lieu")) {
            traiterLieu(id, vueDeduction);
        } else if (id.startsWith("temps")) {
            traiterTemps(id, vueDeduction);
        } else {
            traiterPersonnage(id, vueDeduction);
        }
    }

    private void traiterLieu(String id, VueDeduction vueDeduction) {
        String nomLieu = id.substring(id.indexOf("_") + 1, id.lastIndexOf("_"));
        int indexLieu = Integer.parseInt(id.substring(id.lastIndexOf("_") + 1));
        Lieu lieu = new Lieu(nomLieu, indexLieu, null);
        desactiverAutresBoutons(lieu, vueDeduction.lieuButtons);
        modeleJeu.getModeleQuestionDeduction().setLieuChoisi(lieu, vueDeduction);
    }

    private void traiterTemps(String id, VueDeduction vueDeduction) {
        int indexTemps = Integer.parseInt(id.substring(5));
        Temps temps = new Temps(indexTemps);
        desactiverAutresBoutons(temps, vueDeduction.tempsButtons);
        modeleJeu.getModeleQuestionDeduction().setTempsChoisi(temps, vueDeduction);
    }

    private void traiterPersonnage(String id, VueDeduction vueDeduction) {
        Personnage personnage = new Personnage(id);
        desactiverAutresBoutons(personnage, vueDeduction.personnageButtons);
        modeleJeu.getModeleQuestionDeduction().setPersonnageChoisi(personnage, vueDeduction);
    }

    private void desactiverAutresBoutons(Object choix, List<Button> buttons) {
        if (choix instanceof Lieu l){
            desactiverSelectionBouton(l.getNom(), buttons);
        }
        else if (choix instanceof Temps t){
            desactiverSelectionBouton(String.valueOf(t.getValeur()), buttons);
        }
        else if (choix instanceof Personnage p){
            desactiverSelectionBouton(p.getNom(), buttons);
        }
    }

    private void desactiverSelectionBouton(String s, List<Button> buttons) {
        for (Button b : buttons) {
            if (!b.getId().contains(s)) {
                b.setDisable(true);
            }
        }
    }
}
