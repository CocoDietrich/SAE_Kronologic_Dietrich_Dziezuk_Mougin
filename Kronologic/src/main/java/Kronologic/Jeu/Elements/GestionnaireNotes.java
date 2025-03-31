package Kronologic.Jeu.Elements;

import java.util.ArrayList;
import java.util.List;

public class GestionnaireNotes {

    private final List<Note> notes;

    public GestionnaireNotes() {
        this.notes = new ArrayList<>();
    }

    // Méthode permettant d'ajouter une note
    public void ajouterNote(Note n) {
        if (notes.contains(n) || n == null) {
            return;
        }
        notes.add(n);
    }

    public void modifierNote(Note n, boolean absence, boolean hypothese) {
        Note noteASupprimer = null;
        for (Note note : notes) {
            if (note.equals(n)) {
                note.setEstAbsence(absence);
                note.setEstHypothese(hypothese);

                int compteurDeNotesIdentiques = 0;
                for (Note note2 : notes) {
                    if (note2.equals(note)) {
                        compteurDeNotesIdentiques++;
                    }
                    if (compteurDeNotesIdentiques == 2) {
                        noteASupprimer = note2;
                        break;
                    }
                }
            }
        }
        if (noteASupprimer != null) {
            notes.remove(noteASupprimer);
        }
    }

    public void deplacerNote(Note n, Lieu l, Temps t) {
        Note noteASupprimer = null;
        for (Note note : notes) {
            if (note.equals(n)) {
                if (l == null) {
                    note.setTemps(t);
                    break;
                }
                if (t == null) {
                    note.setLieu(l);
                    break;
                }
                note.setLieu(l);
                note.setTemps(t);

                int compteurDeNotesIdentiques = 0;
                for (Note note2 : notes) {
                    if (note2.equals(note)) {
                        compteurDeNotesIdentiques++;
                    }
                    if (compteurDeNotesIdentiques == 2) {
                        noteASupprimer = note2;
                        break;
                    }
                }
            }
        }
        if (noteASupprimer != null) {
            notes.remove(noteASupprimer);
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
