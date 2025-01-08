package Jeu.Elements.Notes.Gestionnaire;

import Kronologic.Jeu.Elements.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestGestionnaireNoteModifierNote {

    private GestionnaireNotes gestionnaireNotes;

    @BeforeEach
    public void setUp() {
        gestionnaireNotes = new GestionnaireNotes();
    }

    // On teste la modification d'une note
    @Test
    public void test_01_modifierNote() {
        Note note = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière"));
        gestionnaireNotes.ajouterNote(note);
        gestionnaireNotes.modifierNote(note, true, false);
        assertTrue(gestionnaireNotes.getNotes().contains(note), "La note devrait être modifiée");
        assertTrue(note.estAbsence(), "La note devrait être une absence");
        assertFalse(note.estHypothese(), "La note ne devrait pas être une hypothèse");
    }

    // On teste la modification d'une note inexistante
    @Test
    public void test_02_modifierNoteInexistante() {
        Note note = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière"));
        gestionnaireNotes.ajouterNote(note);
        gestionnaireNotes.modifierNote(new Note(new Lieu("Scène", 4, List.of()), new Temps(2), new Personnage("Baronne")), true, true);
        assertFalse(gestionnaireNotes.getNotes().getFirst().estAbsence(), "La note ne devrait pas être une absence");
        assertFalse(gestionnaireNotes.getNotes().getFirst().estHypothese(), "La note ne devrait pas être une hypothèse");
    }

    // On teste la modification d'une note null
    @Test
    public void test_03_modifierNoteNull() {
        Note note = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière"));
        gestionnaireNotes.ajouterNote(note);
        gestionnaireNotes.modifierNote(null, true, true);
        assertFalse(note.estAbsence(), "La note ne devrait pas être une absence");
        assertFalse(note.estHypothese(), "La note ne devrait pas être une hypothèse");
    }

    // On teste la modification d'une note de façon à ce qu'elle devienne identique à une autre note
    @Test
    public void test_04_modifierNoteIdentique() {
        // Première partie
        Note note1 = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière"));
        gestionnaireNotes.ajouterNote(note1);
        gestionnaireNotes.modifierNote(note1, true, true);
        assertTrue(gestionnaireNotes.getNotes().getFirst().estHypothese(), "La note devrait être une hypothèse");
        assertTrue(gestionnaireNotes.getNotes().getFirst().estAbsence(), "La note devrait être une absence");

        // Deuxième partie
        Note note2 = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière"));
        gestionnaireNotes.ajouterNote(note2);
        gestionnaireNotes.modifierNote(note2, true, true);
        assertEquals(1, gestionnaireNotes.getNotes().size(), "La note ne devrait pas être ajoutée");
    }
}
