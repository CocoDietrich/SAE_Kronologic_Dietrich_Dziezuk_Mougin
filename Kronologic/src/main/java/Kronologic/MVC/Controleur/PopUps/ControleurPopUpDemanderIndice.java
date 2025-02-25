package Kronologic.MVC.Controleur.PopUps;

import Kronologic.MVC.Modele.ModeleJeu;
import Kronologic.MVC.Vue.VueIndiceRecommande;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ControleurPopUpDemanderIndice implements EventHandler<ActionEvent> {

    private final ModeleJeu modele;

    public ControleurPopUpDemanderIndice(ModeleJeu modele) {
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
            try {
                String message = modele.getModeleIA().demanderIndice();
                String erreurs = modele.getModeleIA().afficherMauvaisesDeductions();

                VueIndiceRecommande vue = new VueIndiceRecommande(message, erreurs);
                vue.afficher();
                modele.notifierObservateurs();
                stage.close();
            } catch (Exception e) {
                System.err.println("Erreur lors de la récupération de l'indice : " + e.getMessage());
            }
        }
    }
}
