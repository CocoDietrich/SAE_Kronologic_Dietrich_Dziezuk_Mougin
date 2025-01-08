package Jeu.Elements.Pions;

import Kronologic.Jeu.Elements.Pion;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import kronologic.outils.InitialiseurJavaFX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestPionDeplacerPion {

    private Pion pion;

    @BeforeAll
    public static void setUp() {
        InitialiseurJavaFX.initToolkit();
    }

    // On teste le déplacement d'un pion avec des valeurs possibles
    @Test
    public void test_01_DeplacerPionPossible() {
        pion = new Pion(null, "file:img/film.png");
        pion.deplacerPion(1, 2);
        assertEquals(1, pion.getXPion(), "La valeur de x devrait être 1");
        assertEquals(2, pion.getYPion(), "La valeur de y devrait être 2");
    }

    // On teste le déplacement d'un pion avec des valeurs négatives
    @Test
    public void test_02_DeplacerPionNegatif() {
        pion = new Pion(null, "file:img/film.png");
        assertThrows(IllegalArgumentException.class, () -> {
            pion.deplacerPion(-1, -2);
        }, "Le déplacement avec des valeurs négatives devrait lever une exception");
    }

    // On teste le déplacement d'un pion avec x négatif
    @Test
    public void test_03_DeplacerPionXNegatif() {
        pion = new Pion(null, "file:img/film.png");
        assertThrows(IllegalArgumentException.class, () -> {
            pion.deplacerPion(-1, 2);
        }, "Le déplacement avec x négatif devrait lever une exception");
    }

    // On teste le déplacement d'un pion avec y négatif
    @Test
    public void test_04_DeplacerPionYNegatif() {
        pion = new Pion(null, "file:img/film.png");
        assertThrows(IllegalArgumentException.class, () -> {
            pion.deplacerPion(1, -2);
        }, "Le déplacement avec y négatif devrait lever une exception");
    }

}
