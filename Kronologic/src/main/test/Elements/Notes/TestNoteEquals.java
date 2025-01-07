package Elements.Notes;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Note;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestNoteEquals {

    private Note note1;
    private Note note2;
    private Note note3;

    @BeforeEach
    public void setUp() {
        note1 = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière"));
        note3 = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), 2);
    }

    // On teste le cas où les notes sont égales (Personnage)
    @Test
    public void test_01_EqualsTrue() {
        note2 = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière"));
        assert(note1.equals(note2));
    }

    // On teste le cas où les notes sont égales (Nombre de personnage)
    @Test
    public void test_02_EqualsTrue() {
        note2 = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), 2);
        assert(note3.equals(note2));
    }

    // On teste le cas où les notes sont différentes (Valeurs)
    @Test
    public void test_03_EqualsFalse() {
        note2 = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Baronne"));
        assert(!note1.equals(note2));
    }

    // On teste le cas où les notes sont différentes (Personnage et Nombre de personnage)
    @Test
    public void test_04_EqualsFalse() {
        note2 = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), 1);
        assert(!note1.equals(note2));
    }
}
