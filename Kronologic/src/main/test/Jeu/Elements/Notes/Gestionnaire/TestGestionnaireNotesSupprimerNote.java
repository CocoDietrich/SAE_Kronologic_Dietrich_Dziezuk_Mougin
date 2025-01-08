package Jeu.Elements.Notes.Gestionnaire;

import Kronologic.Jeu.Elements.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class TestGestionnaireNotesSupprimerNote {

    private GestionnaireNotes gestionnaireNotes;

    @BeforeEach
    public void setUp() {
        gestionnaireNotes = new GestionnaireNotes();
    }

    // On teste la suppression d'une note
    @Test
    public void test_01_supprimerNote() {
        Note note = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière"));
        gestionnaireNotes.ajouterNote(note);
        gestionnaireNotes.supprimerNote(note);
        assertFalse(gestionnaireNotes.getNotes().contains(note), "La note devrait être supprimée");
    }

    // On teste la suppression d'une note null
    @Test
    public void test_02_supprimerNoteNull() {
        Note note = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière"));
        gestionnaireNotes.ajouterNote(note);
        gestionnaireNotes.supprimerNote(null);
        assertFalse(gestionnaireNotes.getNotes().contains(null), "La note ne devrait pas être supprimée");
    }

    // On teste la suppression d'une note non présente
    @Test
    public void test_03_supprimerNoteNonPresente() {
        Note note = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière"));
        Note note2 = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Baronne"));
        gestionnaireNotes.ajouterNote(note);
        gestionnaireNotes.supprimerNote(note2);
        assertFalse(gestionnaireNotes.getNotes().contains(note2), "La note ne devrait pas être supprimée");
    }

}
