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

    // Méthode permettant de supprimer une note
    public void supprimerNote(Note n) {
        notes.remove(n);
    }

    // Getter pour chaque attribut
    public List<Note> getNotes() {
        return notes;
    }

}
