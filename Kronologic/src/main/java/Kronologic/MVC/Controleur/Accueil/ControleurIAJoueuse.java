package Kronologic.MVC.Controleur.Accueil;

import Kronologic.IA.IAAssistance.IAAssistance;
import Kronologic.IA.IAJoueuse;
import Kronologic.MVC.Modele.ModeleJeu;
import Kronologic.MVC.Vue.VueChargementIA;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ControleurIAJoueuse implements EventHandler<ActionEvent> {

    private final ModeleJeu modeleJeu;

    public ControleurIAJoueuse(ModeleJeu modeleJeu) {
        this.modeleJeu = modeleJeu;
    }

    @Override
    public void handle(ActionEvent event) {
        VueChargementIA vueChargement = new VueChargementIA();
        vueChargement.afficher();

        Thread threadIA = new Thread(() -> {
            IAAssistance iaActive = modeleJeu.getModeleIA().getIaAssistanceChocoSolver();
            IAJoueuse iaJoueuse = new IAJoueuse(iaActive, ModeleJeu.getPartie());

            iaJoueuse.jouerJusquaTrouverCoupable();

            Platform.runLater(() -> vueChargement.afficherResultat("🎯 L’IA a trouvé le coupable !"));
        });

        threadIA.setDaemon(true);
        threadIA.start();
    }
}
