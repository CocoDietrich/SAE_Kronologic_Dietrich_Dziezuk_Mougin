package Kronologic.MVC.Controleur;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Note;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.MVC.Modele.ModeleJeu;
import Kronologic.Jeu.Elements.Pion;
import Kronologic.MVC.Vue.Observateur;
import Kronologic.MVC.Vue.VueCarte;
import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;

public class ControleurChoixCarte implements EventHandler<DragEvent> {

    private ModeleJeu modeleJeu;
    private static int nbPionsPlaces = 0;
    private static Pion pionAvantAvant = null;

    public ControleurChoixCarte(ModeleJeu modeleJeu) {
        this.modeleJeu = modeleJeu;
    }

    @Override
    public void handle(DragEvent dragEvent) {
        System.out.println("--------------------");
        Pion pionAvant = (Pion) dragEvent.getGestureSource();

        VueCarte vueCarte = null;
        for (Observateur observateur : modeleJeu.getObservateurs()) {
            if (observateur instanceof VueCarte) {
                vueCarte = (VueCarte) observateur;
            }
        }
        assert vueCarte != null;

        Pion pionActuel = vueCarte.pions.getLast();
        System.out.println("Pion avant : " + pionAvant.getId());
        System.out.println("Pion actuel : " + pionActuel.getId());

        // Cas du pion déplacé depuis la réserve de pion vers un lieu
        if (pionAvant.getNote() == null) {
            // On récupère le lieu et le temps ciblé
            String nomLieu = ((String) pionActuel.getUserData()).split("-")[1];
            Lieu nouveauLieu = null;
            for (Lieu lieu : ModeleJeu.getPartie().getElements().getLieux()) {
                if (lieu.getNom().equals(nomLieu)) {
                    nouveauLieu = lieu;
                }
            }
            // Récupérer Temps
            String nomTemps = ((String) pionActuel.getUserData()).split("-")[0];
            Temps temps = new Temps(Integer.parseInt(String.valueOf(nomTemps.charAt(nomTemps.length() - 1))));

            // On crée la note
            Note note = new Note(nouveauLieu, temps, new Personnage((pionActuel.getImage().getUrl().split("/"))[2].split(".png")[0]));

            vueCarte.pions.getLast().setNote(note);

            modeleJeu.ajouterPion(note, pionActuel.getImage(), (int) pionActuel.getLayoutX(), (int) pionActuel.getLayoutY());
        } else if (pionActuel.getNote() == null) {
            // Cas du pion déplacé depuis un lieu vers un autre lieu
            modeleJeu.supprimerPion(pionAvant);
            vueCarte.pions.removeLast();
            vueCarte.pions.remove(pionAvant);
        } else {
            // On récupère le lieu et le temps ciblé
            String nomLieu = ((String) pionActuel.getUserData()).split("-")[1];
            Lieu nouveauLieu = null;
            for (Lieu lieu : ModeleJeu.getPartie().getElements().getLieux()) {
                if (lieu.getNom().equals(nomLieu)) {
                    nouveauLieu = lieu;
                }
            }
            // Récupérer Temps
            String nomTemps = ((String) pionActuel.getUserData()).split("-")[0];
            Temps temps = new Temps(Integer.parseInt(String.valueOf(nomTemps.charAt(nomTemps.length() - 1))));

            // On crée la note
            Note note = new Note(nouveauLieu, temps, new Personnage((pionActuel.getImage().getUrl().split("/"))[2].split(".png")[0]));

            vueCarte.pions.getLast().setNote(note);

            modeleJeu.deplacerPion(pionAvant, nouveauLieu, temps, (int) pionActuel.getLayoutX(), (int) pionActuel.getLayoutY());
            vueCarte.pions.remove(pionAvant);
        }

        System.out.println("Liste des notes : ");
        for (Note n : ModeleJeu.getPartie().getGestionnaireNotes().getNotes()) {
            System.out.println(n);
        }

        System.out.println("Liste des pions : ");
        for (Pion p : vueCarte.pions) {
            System.out.println(p.getUserData());
        }
    }
}
