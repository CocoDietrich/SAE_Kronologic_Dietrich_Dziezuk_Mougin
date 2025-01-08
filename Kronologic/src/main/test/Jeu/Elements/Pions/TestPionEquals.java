package Jeu.Elements.Pions;

import Kronologic.Jeu.Elements.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import kronologic.outils.InitialiseurJavaFX;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPionEquals {

    private Pion pion1;
    private Pion pion2;
    private Pion pion3;

    @BeforeEach
    public void init() {
        pion1 = new Pion(new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière")),
                "file:img/pions_temps/temps1.png");
        pion3 = new Pion(null, "file:img/pions_temps/temps3.png");
    }

    @BeforeAll
    public static void setUp() {
        InitialiseurJavaFX.initToolkit();
    }

    // On teste le cas où les pions sont égaux (note non null)
    @Test
    public void test_01_EqualsTrue() {
        pion2 = new Pion(new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière")),
                "file:img/pions_temps/temps1.png");
        assertEquals(pion1, pion2);
    }

    // On teste le cas où les pions sont égaux (note null)
    @Test
    public void test_02_EqualsTrue() {
        pion2 = new Pion(null, "file:img/pions_temps/temps3.png");
        assertEquals(pion3, pion2);
    }

    // On teste le cas où les pions sont différents (note null)
    @Test
    public void test_03_EqualsFalse() {
        pion2 = new Pion(null, "file:img/pions_temps/temps1.png");
        assert(!pion1.equals(pion2));
    }

    // On teste le cas où les pions sont différents (image différente)
    @Test
    public void test_04_EqualsFalse() {
        pion2 = new Pion(new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière")),
                "file:img/pions_temps/temps2.png");
        assert(!pion1.equals(pion2));
    }

    // On teste le cas où les pions sont différents (note différente)
    @Test
    public void test_05_EqualsFalse() {
        pion2 = new Pion(new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Baronne")),
                "file:img/pions_temps/temps1.png");
        assert(!pion1.equals(pion2));
    }
}
