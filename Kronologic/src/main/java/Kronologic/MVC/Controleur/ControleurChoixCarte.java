package Kronologic.MVC.Controleur;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Note;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.MVC.Modele.ModeleJeu;
import Kronologic.MVC.Vue.Pion;
import Kronologic.MVC.Vue.VueCarte;
import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;

public class ControleurChoixCarte implements EventHandler<DragEvent> {

    private ModeleJeu modeleJeu;

    public ControleurChoixCarte(ModeleJeu modeleJeu) {
        this.modeleJeu = modeleJeu;
    }

    @Override
    public void handle(DragEvent dragEvent) {
        Pion pionAvant = (Pion) dragEvent.getGestureSource();

        System.out.println("Pion avant : " + pionAvant.getNote());

        VueCarte vueCarte;
        if (pionAvant.getNote() == null) {
            vueCarte = (VueCarte) pionAvant.getParent().getParent().getParent().getParent().getParent();
        }
        else {
            vueCarte = (VueCarte) pionAvant.getParent();
        }

        Pion pionActuel = vueCarte.pions.getLast();

        // Récupérer x
        int x = (int) pionActuel.getLayoutX();
        // Récupérer y
        int y = (int) pionActuel.getLayoutY();
        // Récupérer Lieu
        String nomLieu = vueCarte.lieuTempsPion.split("-")[1];

        Lieu nouveauLieu = null;
        for (Lieu lieu : ModeleJeu.getPartie().getElements().getLieux()) {
            if (lieu.getNom().equals(nomLieu)) {
                nouveauLieu = lieu;
            }
        }

        // Récupérer Personnage
        Personnage personnage = new Personnage((pionActuel.getImage().getUrl().split("/"))[1].split(".png")[0]);

        // Récupérer Temps
        String nomTemps = vueCarte.lieuTempsPion.split("-")[0];
        Temps temps = new Temps(Integer.parseInt(String.valueOf(nomTemps.charAt(nomTemps.length()-1))));

        // On crée la note
        Note note = new Note(nouveauLieu, temps, personnage);

        vueCarte.pions.getLast().setNote(note);

        modeleJeu.ajouterPion(note, pionActuel.getImage(), x, y);

        vueCarte.pions.getLast().setOnDragDone(this);
    }
}
