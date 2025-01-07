package Elements.Pions;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Note;
import Kronologic.Jeu.Elements.Pion;
import Kronologic.Jeu.Elements.Temps;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestPionConstructeur {

    private Pion pion;

    @BeforeAll
    public static void initToolkit() {
        // Initialise JavaFX
        Platform.startup(() -> {});
    }

    // NOTE

    // On teste le constructeur avec la note à null
    @Test
    public void test_01_ConstructeurPionNoteNull() {
        pion = new Pion(null, "file:img/film.png");
        assertNull(pion.getNote(), "La note devrait être null");
    }

    // On teste le constructeur avec la note non null
    @Test
    public void test_02_ConstructeurPionNotePossible() {
        pion = new Pion(new Note(new Lieu(3), new Temps(1), 4), "file:img/film.png");
        assertNotNull(pion.getNote(), "La note ne devrait pas être null");
    }

    // IMAGE

    // On teste le constructeur avec l'image à null
    @Test
    public void test_03_ConstructeurPionImageNull() {
        assertThrows(NullPointerException.class, () -> {
            pion = new Pion(new Note(new Lieu(3), new Temps(1), 4), null);
        }, "Le constructeur avec valeur nulle devrait lever une exception");
    }

    // On teste le constructeur avec l'image non null
    @Test
    public void test_04_ConstructeurPionImagePossible() {
        pion = new Pion(new Note(new Lieu(3), new Temps(1), 4), "file:img/film.png");
        assertEquals("file:img/film.png", pion.getImage().getUrl(), "L'URL de l'image devrait être file:img/film.png");
    }

}
