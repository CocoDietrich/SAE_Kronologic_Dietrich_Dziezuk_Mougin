package Kronologic.MVC.Controleur;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Note;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.MVC.Modele.ModeleJeu;
import Kronologic.Jeu.Elements.Pion;
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
        System.out.println("--------------------");

        Pion pionAvant = (Pion) dragEvent.getGestureSource();

        System.out.println("1-" + pionAvant.getNote());
        System.out.println("2-" + pionAvant);
        System.out.println("3-" + pionAvant.getParent());

        VueCarte vueCarte;
        if (pionAvant.getNote() == null) {
            vueCarte = (VueCarte) pionAvant.getParent().getParent().getParent().getParent().getParent();
        } else {
            vueCarte = (VueCarte) pionAvant.getParent();
        }

        Pion pionActuel = vueCarte.pions.getLast();

        if (pionAvant.getNote() == null) {
            // Récupérer Lieu
            System.out.println("Pion actuel : " + pionActuel.getUserData());
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

            vueCarte.pions.getLast().setOnDragDone(this);
        } else if (pionAvant.getNote() != null) {
            // Rajouter une condition pour savoir si le pion a été déposé sur une zone ou pas.

            // Récupérer Lieu
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

            modeleJeu.deplacerPion(pionAvant, nouveauLieu, temps, (int) pionActuel.getLayoutX(), (int) pionActuel.getLayoutY());

            // On met à jour le pion dans la vue
            for (Pion p : vueCarte.pions) {
                if (p.getNote() != null) {
                    if (p.getNote().toString().equals(pionAvant.getNote().toString())) {
                        System.out.println("Pion trouvé : " + p.getNote());
                        p.setOnDragDone(this);
                        p.deplacerPion((int) pionActuel.getLayoutX(), (int) pionActuel.getLayoutY());
                        p.setNote(new Note(nouveauLieu, temps, p.getNote().getPersonnage()));
                        vueCarte.pions.set(vueCarte.pions.indexOf(p), p);
                        break;
                    }
                }
            }
        }
        // On affiche la liste des notes du joueur
        System.out.println("Liste des notes du joueur : ");
        for (Note note : ModeleJeu.getPartie().getGestionnaireNotes().getNotes()) {
            System.out.println(note.toString());
        }
    }
}
