package Kronologic.MVC.Controleur;

import Kronologic.MVC.Modele.SousModeleJeu.ModeleIA;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class ControleurDemanderIndice implements EventHandler<ActionEvent> {
    private final ModeleIA modeleIA;

    private static final String DEMANDER_INDICE_ID = "Demander un indice";

    public ControleurDemanderIndice(ModeleIA modeleIA) {
        this.modeleIA = modeleIA;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        if (actionEvent.getSource() instanceof Button) {
            String id = ((Button) (actionEvent.getSource())).getId();
            if (DEMANDER_INDICE_ID.equals(id)) {
                this.modeleIA.afficherPopUpDemanderIndice();
            }
        }
    }

    // 👇 Ajouter cette méthode pour attacher les boutons de choix d'IA
    public void attacherChoixIA(Button boutonChoco, Button boutonHeuris, Button boutonTriche, Button boutonSansTriche) {
        boutonChoco.setOnAction(e -> modeleIA.utiliserIAAssistanceChoco());
        boutonHeuris.setOnAction(e -> modeleIA.utiliserIAAssistanceHeuristique());
        boutonTriche.setOnAction(e -> modeleIA.activerTriche());
        boutonSansTriche.setOnAction(e -> modeleIA.desactiverTriche());
    }
}
