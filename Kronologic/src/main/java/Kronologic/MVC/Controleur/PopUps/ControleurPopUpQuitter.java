package Kronologic.MVC.Controleur.PopUps;

import Kronologic.MVC.Modele.ModeleJeu;
import Kronologic.MVC.Vue.PopUps.VuePopUpQuitter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Optional;

public class ControleurPopUpQuitter implements EventHandler<ActionEvent> {

    private final ModeleJeu modele;

    public ControleurPopUpQuitter(ModeleJeu modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if (!(source instanceof Button button)) {
            return;  // Sécurité : on ignore si ce n'est pas un bouton
        }

        Stage stage = (Stage) ((Node) button).getScene().getWindow();
        String buttonId = button.getId();

        if ("annuler".equals(buttonId)) {
            stage.close();
        } else if ("valider".equals(buttonId)) {
            // Recherche optimisée de VuePopUpQuitter parmi les observateurs
            Optional<VuePopUpQuitter> vuePopUpQuitter = modele.getObservateurs().stream()
                    .filter(VuePopUpQuitter.class::isInstance)
                    .map(VuePopUpQuitter.class::cast)
                    .findFirst();

            if (vuePopUpQuitter.isPresent()) {
                Stage stageGlobal = vuePopUpQuitter.get().stageGlobal;
                modele.quitter("retour", stageGlobal);
            } else {
                System.err.println("Erreur : VuePopUpQuitter non trouvée parmi les observateurs.");
            }

            stage.close();
        }
    }
}
