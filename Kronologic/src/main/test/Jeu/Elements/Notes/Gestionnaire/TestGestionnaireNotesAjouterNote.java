package Jeu.Elements.Notes.Gestionnaire;

import Kronologic.Jeu.Elements.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestGestionnaireNotesAjouterNote {

    private GestionnaireNotes gestionnaireNotes;

    @BeforeEach
    public void setUp() {
        gestionnaireNotes = new GestionnaireNotes();
    }

    // On teste l'ajout d'une note
    @Test
    public void test_01_ajoutNote() {
        Note note = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière"));
        gestionnaireNotes.ajouterNote(note);
        assertTrue(gestionnaireNotes.getNotes().contains(note), "La note devrait être ajoutée");
    }

    // On teste l'ajout de plusieurs notes
    @Test
    public void test_02_ajoutNotes() {
        Note note1 = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière"));
        Note note2 = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Baronne"));
        Note note3 = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Chauffeur"));
        gestionnaireNotes.ajouterNote(note1);
        gestionnaireNotes.ajouterNote(note2);
        gestionnaireNotes.ajouterNote(note3);
        assertTrue(gestionnaireNotes.getNotes().contains(note1), "La note 1 devrait être ajoutée");
        assertTrue(gestionnaireNotes.getNotes().contains(note2), "La note 2 devrait être ajoutée");
        assertTrue(gestionnaireNotes.getNotes().contains(note3), "La note 3 devrait être ajoutée");
    }

    // On teste l'ajout de la même note en double
    @Test
    public void test_03_ajoutNoteDouble() {
        Note note = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière"));
        gestionnaireNotes.ajouterNote(note);
        gestionnaireNotes.ajouterNote(note);
        assertEquals(1, gestionnaireNotes.getNotes().size(), "La note ne devrait pas être ajoutée deux fois");
    }

    // On teste l'ajout d'une note null
    @Test
    public void test_04_ajoutNoteNull() {
        gestionnaireNotes.ajouterNote(null);
        assertEquals(0, gestionnaireNotes.getNotes().size(), "La note ne devrait pas être ajoutée");
    }
}
