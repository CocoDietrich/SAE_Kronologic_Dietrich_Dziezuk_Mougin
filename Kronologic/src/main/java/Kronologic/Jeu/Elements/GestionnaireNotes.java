package Kronologic.Jeu.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GestionnaireNotes {

    private List<Note> notes;

    public GestionnaireNotes() {
        this.notes = new ArrayList<>();
    }

    // Méthode permettant d'ajouter une note
    public void ajouterNote(Note n) {
        notes.add(n);
    }

    public void modifierNote(Note n, boolean absence, boolean hypothese) {
        for (Note note : notes) {
            if (note.equals(n)) {
                note.setEstAbsence(absence);
                note.setEstHypothese(hypothese);
            }
        }
    }

    public void deplacerNote(Note n, Lieu l) {
        for (Note note : notes) {
            if (note.equals(n)) {
                System.out.println("note trouvée");
                note.setLieu(l);
            }
        }
    }

    // Méthode permettant de supprimer une note
    public void supprimerNote(Note n) {
        notes.remove(n);
    }

    // Getter pour chaque attribut
    public List<Note> getNotes() {
        return notes;
    }

}
