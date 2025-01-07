package Elements.Notes;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Note;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestNoteConstructeur {

    private Note note;

    // CONSTRUCTEUR DE NOTE PERSONNAGE

    // On teste le constructeur avec les éléments à null
    @Test
    public void test_01_ConstructeurNoteNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            note = new Note(null, null, null);
        }, "Le constructeur avec valeur nulle devrait lever une exception");
    }

    // On teste le constructeur avec le lieu à null
    @Test
    public void test_02_ConstructeurNoteLieuNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            note = new Note(null, new Temps(1), new Personnage("Personnage"));
        }, "Le constructeur avec valeur nulle devrait lever une exception");
    }

    // On teste le constructeur avec le temps à null
    @Test
    public void test_03_ConstructeurNoteTempsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            note = new Note(new Lieu("Salle", 1, List.of()), null, new Personnage("Personnage"));
        }, "Le constructeur avec valeur nulle devrait lever une exception");
    }

    // On teste le constructeur avec le personnage à null
    @Test
    public void test_04_ConstructeurNotePersonnageNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            note = new Note(new Lieu("Salle", 1, List.of()), new Temps(1), null);
        }, "Le constructeur avec valeur nulle devrait lever une exception");
    }

    // On teste le constructeur avec les éléments non null
    @Test
    public void test_05_ConstructeurNotePossible() {
        note = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière"));
        assertEquals("Salle", note.getLieu().getNom(), "Le nom du lieu devrait être Salle");
        assertEquals(1, note.getTemps().getValeur(), "La valeur du temps devrait être 1");
        assertEquals("Aventurière", note.getPersonnage().getNom(), "Le nom du personnage devrait être Aventurière");
    }


    // CONSTRUCTEUR DE NOTE NOMBRE DE PERSONNAGES

    // On teste le constructeur avec les éléments à null
    @Test
    public void test_06_ConstructeurNoteNombreNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            note = new Note(null, null, 0);
        }, "Le constructeur avec valeur nulle devrait lever une exception");
    }

    // On teste le constructeur avec le lieu à null
    @Test
    public void test_07_ConstructeurNoteNombreLieuNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            note = new Note(null, new Temps(1), 1);
        }, "Le constructeur avec valeur nulle devrait lever une exception");
    }

    // On teste le constructeur avec le temps à null
    @Test
    public void test_08_ConstructeurNoteNombreTempsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            note = new Note(new Lieu("Salle", 1, List.of()), null, 1);
        }, "Le constructeur avec valeur nulle devrait lever une exception");
    }

    // On teste le constructeur avec le nombre de personnage en négatif
    @Test
    public void test_09_ConstructeurNoteNombrePersonnageZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            note = new Note(new Lieu("Salle", 1, List.of()), new Temps(1), -2);
        }, "Le constructeur avec valeur nulle devrait lever une exception");
    }

    // On teste le constructeur avec le nombre de personnage positif supérieur à 6
    @Test
    public void test_10_ConstructeurNoteNombrePersonnageImpossible() {
        assertThrows(IllegalArgumentException.class, () -> {
            note = new Note(new Lieu("Salle", 1, List.of()), new Temps(1), 7);
        }, "Le constructeur avec une valeur impossible devrait lever une exception");
    }

    // On teste le constructeur avec les éléments non null
    @Test
    public void test_11_ConstructeurNoteNombrePossible() {
        note = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), 1);
        assertEquals("Salle", note.getLieu().getNom(), "Le nom du lieu devrait être Salle");
        assertEquals(1, note.getTemps().getValeur(), "La valeur du temps devrait être 1");
        assertEquals(1, note.getNbPersonnages(), "Le nombre de personnage devrait être 1");
    }
}
