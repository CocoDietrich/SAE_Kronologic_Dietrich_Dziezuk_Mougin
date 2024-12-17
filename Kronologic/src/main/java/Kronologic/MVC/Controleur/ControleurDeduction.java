package Kronologic.MVC.Controleur;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.MVC.Modele.ModeleJeu;
import Kronologic.MVC.Vue.Observateur;
import Kronologic.MVC.Vue.VueDeduction;
import Kronologic.MVC.Vue.VuePoseQuestion;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ControleurDeduction implements EventHandler<ActionEvent> {
    private ModeleJeu modele;

    public ControleurDeduction(ModeleJeu modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Lieu lieu = null;
        Temps temps = null;
        Personnage personnage = null;

        VueDeduction vueDeduction = null;
        // On récupère la vue de la pose de question
        for (Observateur o : modele.getObservateurs()) {
            if (o instanceof VueDeduction) {
                vueDeduction = (VueDeduction) o;
                break;
            }
        }

        // On récupère l'id du bouton
        String id = ((Button) actionEvent.getSource()).getId();
        if (id.equals("Retour")) {
            // On retourne à la vue de la carte
            this.modele.retourVueCarte(stage);

        } else if (id.startsWith("Lieu")) {
            // On récupère le nom et l'index du lieu
            String nomLieu = id.substring(id.indexOf("_") + 1, id.lastIndexOf("_"));
            int indexLieu = Integer.parseInt(id.substring(id.lastIndexOf("_") + 1));
            lieu = new Lieu(nomLieu, indexLieu, null);

            // On désactive les boutons des autres lieux
            for (Button b : vueDeduction.lieuButtons) {
                if (!b.getId().equals(id)) {
                    b.setDisable(true);
                }
            }
            modele.setLieuChoisi(lieu, vueDeduction);

        } else if (id.startsWith("temps")) {
            // On récupère l'index du temps
            int indexTemps = Integer.parseInt(id.substring(5));
            temps = new Temps(indexTemps);

            // On désactive les boutons des autres temps
            for (Button b : vueDeduction.tempsButtons) {
                if (!b.getId().equals(id)) {
                    b.setDisable(true);
                }
            }
            modele.setTempsChoisi(temps, vueDeduction);

        } else if (id.equals("Valider")) {
            // On réactive tous les boutons pour les prichaines questions
            for (Button b : vueDeduction.lieuButtons) {
                b.setDisable(false);
            }
            for (Button b : vueDeduction.tempsButtons) {
                b.setDisable(false);
            }
            for (Button b : vueDeduction.personnageButtons) {
                b.setDisable(false);
            }
            // On pose la question
            modele.faireDeduction();

            // On retourne à la vue de la carte
            this.modele.quitter("retour", stage);

        } else if (id.equals("Annuler mes choix")) {
            // On réactive les boutons des lieux
            assert vueDeduction != null;
            for (Button b : vueDeduction.lieuButtons) {
                b.setDisable(false);
            }
            // On réactive les boutons des temps
            for (Button b : vueDeduction.tempsButtons) {
                b.setDisable(false);
            }
            // On réactive les boutons des personnages
            for (Button b : vueDeduction.personnageButtons) {
                b.setDisable(false);
            }
            // On réinitialise les choix
            modele.setLieuChoisi(null, vueDeduction);
            modele.setTempsChoisi(null, vueDeduction);
            modele.setPersonnageChoisi(null, vueDeduction);
        } else {
            // On récupère l'id du personnage
            personnage = new Personnage(id);

            // On désactive les boutons des autres personnages
            assert vueDeduction != null;
            for (Button b : vueDeduction.personnageButtons) {
                if (!b.getId().equals(id)) {
                    b.setDisable(true);
                }
            }
            modele.setPersonnageChoisi(personnage, vueDeduction);
        }
    }
}
