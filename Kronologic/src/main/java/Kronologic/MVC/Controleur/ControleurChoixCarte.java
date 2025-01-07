package Kronologic.MVC.Controleur;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Note;
import Kronologic.Jeu.Elements.Personnage;
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

        System.out.println("1-" + pionAvant.getNote());
        System.out.println("2-" + pionAvant);

        VueCarte vueCarte = null;
        for (Observateur observateur : modeleJeu.getObservateurs()) {
            if (observateur instanceof VueCarte) {
                vueCarte = (VueCarte) observateur;
            }
        }
        assert vueCarte != null;

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

            if (pionAvantAvant != null) {
                // Dans le cas où un pion a été déplacé dans la même salle
                if (pionAvant.getUserData() != pionAvantAvant.getUserData()) {
                    // Un pion a été placé
                    incrementerNbPionsPlaces();
                }
            } else {
                // Un pion a été placé
                incrementerNbPionsPlaces();
            }

        } else if (pionAvant.getNote() != null) {

            // Récupérer Lieu
            System.out.println("Pion actuel : " + pionActuel.getUserData());
            String nomLieu = ((String) pionActuel.getUserData()).split("-")[1];

            Lieu nouveauLieu = null;
            for (Lieu lieu : ModeleJeu.getPartie().getElements().getLieux()) {
                if (lieu.getNom().equals(nomLieu)) {
                    nouveauLieu = lieu;
                }
            }

            // Si on déplace le pion dans le vide, on le supprime
            if (pionAvant.getUserData() == pionActuel.getUserData()) {
                modeleJeu.supprimerPion(pionActuel);
            } else {
                // Récupérer Temps
                String nomTemps = ((String) pionActuel.getUserData()).split("-")[0];
                Temps temps = new Temps(Integer.parseInt(String.valueOf(nomTemps.charAt(nomTemps.length() - 1))));

                vueCarte.pions.getLast().setNote(new Note(nouveauLieu, temps, pionAvant.getNote().getPersonnage()));

                modeleJeu.deplacerPion(pionAvant, nouveauLieu, temps, (int) pionActuel.getLayoutX(), (int) pionActuel.getLayoutY());
            }

        }

        // On vérifie si le nombre de notes correspond au nombre de pions placés
        // (pour le cas où un pion s'était déplacé dans la même salle auparavant)
        if (nbPionsPlaces != ModeleJeu.getPartie().getGestionnaireNotes().getNotes().size()) {
            // On supprime la mauvaise note (l'avant-dernière)
            ModeleJeu.getPartie().getGestionnaireNotes().supprimerNote(ModeleJeu.getPartie().getGestionnaireNotes().getNotes().get(ModeleJeu.getPartie().getGestionnaireNotes().getNotes().size() - 2));
        }

        // On affiche la liste des notes du joueur
        System.out.println("Liste des notes du joueur : ");
        for (Note note : ModeleJeu.getPartie().getGestionnaireNotes().getNotes()) {
            System.out.println(note.toString());
        }

        // On stocke le pion avant le pion actuel
        pionAvantAvant = pionAvant;
    }

    // Méthode d'incrémentation du nombre de pions placés
    public static void incrementerNbPionsPlaces() {
        nbPionsPlaces++;
    }
}
