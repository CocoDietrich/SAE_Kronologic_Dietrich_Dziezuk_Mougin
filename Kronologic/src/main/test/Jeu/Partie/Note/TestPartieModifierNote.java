package Jeu.Partie.Note;

import Kronologic.Jeu.Deroulement;
import Kronologic.Jeu.Elements.*;
import Kronologic.Jeu.Enquete;
import Kronologic.Jeu.Indice.GestionnaireIndices;
import Kronologic.Jeu.Indice.IndicePersonnage;
import Kronologic.Jeu.Partie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestPartieModifierNote {

    private Partie partie;
    private Enquete enquete;
    private Deroulement deroulement;
    private GestionnaireIndices gestionnaireIndices;
    private GestionnaireNotes gestionnaireNotes;
    private GestionnairePions gestionnairePions;
    private Elements elements;

    @BeforeEach
    public void setUp() {
        // Création des éléments
        Personnage baronne = new Personnage("Baronne");
        Personnage aventuriere = new Personnage("Aventurière");
        Lieu scene = new Lieu("Scène", 4, List.of());
        Lieu salle = new Lieu("Salle", 3, List.of());

        // Création des éléments de déroulement
        Realite r1 = new Realite(scene, new Temps(1), baronne);
        Realite r2 = new Realite(salle, new Temps(2), baronne);
        Realite r3 = new Realite(scene, new Temps(1), aventuriere);
        Realite r4 = new Realite(salle, new Temps(2), aventuriere);
        ArrayList<Realite> listePositions = new ArrayList<>();
        listePositions.add(r1);
        listePositions.add(r2);
        listePositions.add(r3);
        listePositions.add(r4);

        // Création des indices
        IndicePersonnage i1 = new IndicePersonnage(scene, 1, baronne, 1);
        IndicePersonnage i2 = new IndicePersonnage(salle, 1, baronne, 2);
        IndicePersonnage i3 = new IndicePersonnage(scene, 1, aventuriere, 1);
        IndicePersonnage i4 = new IndicePersonnage(salle, 1, aventuriere, 2);

        // Création de l'enquête
        enquete = new Enquete(1,
                "Enquete 1",
                "Synopsis 1",
                "Enigme 1",
                5,
                10,
                baronne,
                salle,
                new Temps(2));

        // Création du déroulement
        deroulement = new Deroulement(listePositions);

        // Création du gestionnaire d'indices
        gestionnaireIndices = new GestionnaireIndices(List.of(i1, i2, i3, i4));

        // Création du gestionnaire de notes
        gestionnaireNotes = new GestionnaireNotes();

        // Création du gestionnaire de pions
        gestionnairePions = new GestionnairePions();

        // Création des éléments
        elements = new Elements(List.of(baronne, aventuriere), List.of(scene, salle));

        partie = new Partie(enquete, deroulement, gestionnaireIndices, gestionnaireNotes, gestionnairePions, elements);
    }

    // On teste la modification d'une note avec une absence
    @Test
    public void test_01_modifierNote() {
        Note note = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière"));
        partie.ajouterNote(note);
        assertEquals(1, partie.getHistorique().get(0).size(), "La note devrait être ajoutée");
        partie.modifierNote(note, true, false);
        assertTrue(partie.getGestionnaireNotes().getNotes().contains(note), "La note devrait être modifiée");
        assertTrue(note.estAbsence(), "La note devrait être une absence");
        assertFalse(note.estHypothese(), "La note ne devrait pas être une hypothèse");
        assertEquals(1, partie.getHistorique().get(0).size(), "La note devrait être ajoutée");
    }

    // On teste la modification d'une note avec une hypothèse
    @Test
    public void test_02_modifierNote() {
        Note note = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière"));
        partie.ajouterNote(note);
        assertEquals(1, partie.getHistorique().get(0).size(), "La note devrait être ajoutée");
        partie.modifierNote(note, false, true);
        assertTrue(partie.getGestionnaireNotes().getNotes().contains(note), "La note devrait être modifiée");
        assertFalse(note.estAbsence(), "La note ne devrait pas être une absence");
        assertTrue(note.estHypothese(), "La note devrait être une hypothèse");
        assertEquals(1, partie.getHistorique().get(0).size(), "La note devrait être ajoutée");
    }

    // On teste la modification d'une note avec une absence et une hypothèse
    @Test
    public void test_03_modifierNote() {
        Note note = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière"));
        partie.ajouterNote(note);
        assertEquals(1, partie.getHistorique().get(0).size(), "La note devrait être ajoutée");
        partie.modifierNote(note, true, true);
        assertTrue(partie.getGestionnaireNotes().getNotes().contains(note), "La note devrait être modifiée");
        assertTrue(note.estAbsence(), "La note devrait être une absence");
        assertTrue(note.estHypothese(), "La note devrait être une hypothèse");
        assertEquals(1, partie.getHistorique().get(0).size(), "La note devrait être ajoutée");
    }

    // On teste la modification d'une note inexistante
    @Test
    public void test_04_modifierNoteInexistante() {
        Note note = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière"));
        partie.ajouterNote(note);
        assertEquals(1, partie.getHistorique().get(0).size(), "La note devrait être ajoutée");
        Note note2 = new Note(new Lieu("Scène", 4, List.of()), new Temps(2), new Personnage("Baronne"));
        partie.modifierNote(note2, true, true);
        assertFalse(partie.getGestionnaireNotes().getNotes().getFirst().estAbsence(), "La note ne devrait pas être une absence");
        assertFalse(partie.getGestionnaireNotes().getNotes().getFirst().estHypothese(), "La note ne devrait pas être une hypothèse");
        assertEquals(1, partie.getHistorique().get(0).size(), "La note devrait être ajoutée");
    }

    // On teste la modification d'une note null
    @Test
    public void test_05_modifierNoteNull() {
        Note note = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière"));
        partie.ajouterNote(note);
        assertEquals(1, partie.getHistorique().get(0).size(), "La note devrait être ajoutée");
        partie.modifierNote(null, true, true);
        assertFalse(note.estAbsence(), "La note ne devrait pas être une absence");
        assertFalse(note.estHypothese(), "La note ne devrait pas être une hypothèse");
        assertEquals(1, partie.getHistorique().get(0).size(), "La note devrait être ajoutée");
    }

    // On teste la modification d'une note de façon à ce qu'elle devienne identique à une autre note
    @Test
    public void test_06_modifierNoteIdentique() {
        // Première partie
        Note note1 = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière"));
        partie.ajouterNote(note1);
        assertEquals(1, partie.getHistorique().get(0).size(), "La note devrait être ajoutée");
        partie.modifierNote(note1, true, true);
        assertTrue(partie.getGestionnaireNotes().getNotes().getFirst().estHypothese(), "La note devrait être une hypothèse");
        assertTrue(partie.getGestionnaireNotes().getNotes().getFirst().estAbsence(), "La note devrait être une absence");
        assertEquals(1, partie.getHistorique().get(0).size(), "La note devrait être ajoutée");

        // Deuxième partie
        Note note2 = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière"));
        partie.ajouterNote(note2);
        assertEquals(2, partie.getHistorique().get(0).size(), "La note devrait être ajoutée");
        partie.modifierNote(note2, true, true);
        assertTrue(partie.getGestionnaireNotes().getNotes().getFirst().estHypothese(), "La note devrait être une hypothèse");
        assertTrue(partie.getGestionnaireNotes().getNotes().getFirst().estAbsence(), "La note devrait être une absence");
        assertEquals(1, partie.getHistorique().get(0).size(), "La note devrait être ajoutée");

        // Il doit rester qu'une seule note
        assertEquals(1, partie.getGestionnaireNotes().getNotes().size(), "La note ne devrait pas être ajoutée");
    }
}
