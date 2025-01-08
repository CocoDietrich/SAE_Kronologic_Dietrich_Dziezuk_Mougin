package Jeu.Elements.Notes.Gestionnaire;

import Kronologic.Jeu.Elements.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TestGestionnaireNotesDeplacerNote {

    private GestionnaireNotes gestionnaireNotes;

    @BeforeEach
    public void setUp() {
        gestionnaireNotes = new GestionnaireNotes();
    }

    // On teste de déplacer une note dans un lieu et un temps
    @Test
    public void test_01_deplacerNoteLieuTemps() {
        Note note = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière"));
        Lieu lieu = new Lieu("Scène", 4, List.of());
        Temps temps = new Temps(2);
        gestionnaireNotes.ajouterNote(note);
        gestionnaireNotes.deplacerNote(note, lieu, temps);
        assertEquals(lieu, gestionnaireNotes.getNotes().getFirst().getLieu(), "La note devrait être déplacée");
        assertEquals(temps, gestionnaireNotes.getNotes().getFirst().getTemps(), "La note devrait être déplacée");
    }

    // On teste de déplacer une note dans un lieu
    @Test
    public void test_02_deplacerNoteLieu() {
        Note note = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière"));
        Lieu lieu = new Lieu("Scène", 4, List.of());
        gestionnaireNotes.ajouterNote(note);
        gestionnaireNotes.deplacerNote(note, lieu, null);
        assertEquals(lieu, note.getLieu(), "La note devrait être déplacée");
    }

    // On teste de déplacer une note dans un temps
    @Test
    public void test_03_deplacerNoteTemps() {
        Note note = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière"));
        Temps temps = new Temps(2);
        gestionnaireNotes.ajouterNote(note);
        gestionnaireNotes.deplacerNote(note, null, temps);
        assertEquals(temps, note.getTemps(), "La note devrait être déplacée");
    }

    // On teste de déplacer une note null
    @Test
    public void test_04_deplacerNoteNull() {
        Note note = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière"));
        gestionnaireNotes.ajouterNote(note);
        Lieu lieu = new Lieu("Scène", 4, List.of());
        Temps temps = new Temps(2);
        gestionnaireNotes.deplacerNote(null, lieu, temps);
        assertNotEquals(lieu, note.getLieu(), "La note devrait être déplacée");
        assertNotEquals(temps, note.getTemps(), "La note devrait être déplacée");
    }

    // On teste de déplacer une note qui n'existe pas
    @Test
    public void test_05_deplacerNoteInexistante() {
        Note noteExistante = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Baronne"));
        gestionnaireNotes.ajouterNote(noteExistante);
        Note note = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière"));
        Lieu lieu = new Lieu("Scène", 4, List.of());
        Temps temps = new Temps(2);
        gestionnaireNotes.deplacerNote(note, lieu, temps);
        assertNotEquals(lieu, note.getLieu(), "La note ne devrait pas être déplacée");
        assertNotEquals(temps, note.getTemps(), "La note ne devrait pas être déplacée");
    }

    // On teste de déplacer une note dans un lieu et un temps pour créer un double d'une note déjà existante
    @Test
    public void test_06_deplacerNoteLieuTempsDouble() {
        Note note = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière"));
        gestionnaireNotes.ajouterNote(note);

        Note note2 = new Note(new Lieu("Scène", 4, List.of()), new Temps(2), new Personnage("Aventurière"));
        gestionnaireNotes.ajouterNote(note2);

        Lieu lieu = new Lieu("Salle", 3, List.of());
        Temps temps = new Temps(1);
        gestionnaireNotes.deplacerNote(note2, lieu, temps);
        assertEquals(1, gestionnaireNotes.getNotes().size(), "La note ne devrait pas être ajoutée deux fois");
    }


}
