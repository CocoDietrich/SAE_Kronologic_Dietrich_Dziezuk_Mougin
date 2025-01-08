package Elements;

import Kronologic.Jeu.Elements.Lieu;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestLieu {

    private Lieu lieu;

    // CONSTRUCTEURS DE LIEU 1

    // PARTIE DU NOM

    // On teste le constructeur de lieu avec valeur égale à ""
    @Test
    public void test_01_ConstructeurLieuVide() {
        assertThrows(NullPointerException.class, () -> {
            lieu = new Lieu("", 1, List.of());
        }, "Le constructeur avec valeur nulle devrait lever une exception");
    }

    // On teste le constructeur de temps avec valeur nulle
    @Test
    public void test_02_ConstructeurLieuNul() {
        assertThrows(NullPointerException.class, () -> {
            lieu = new Lieu(null, 1, List.of());
        }, "Le constructeur avec valeur nulle devrait lever une exception");
    }

    // On teste le constructeur de temps avec valeur négative
    @Test
    public void test_03_ConstructeurLieuPossible() {
        lieu = new Lieu("Salle", 3, List.of());
        assertEquals("Salle", lieu.getNom(), "Le nom du lieu devrait être Salle");
    }

    // On teste le constructeur de temps avec valeur positive
    @Test
    public void test_04_ConstructeurLieuImpossible() {
        assertThrows(IllegalArgumentException.class, () -> {
            lieu = new Lieu("Chemin", 1, List.of());
        }, "Le constructeur avec une valeur impossible devrait lever une exception");
    }

    // PARTIE DE L'ID

    // On teste le constructeur de lieu avec valeur égale à 0
    @Test
    public void test_05_ConstructeurLieuIdNul() {
        assertThrows(IllegalArgumentException.class, () -> {
            lieu = new Lieu("Salle", 0, List.of());
        }, "Le constructeur avec valeur nulle devrait lever une exception");
    }

    // On teste le constructeur de lieu avec valeur null
    @Test
    public void test_06_ConstructeurLieuIdNegatif() {
        assertThrows(NullPointerException.class, () -> {
            lieu = new Lieu("Salle", (Integer) null, List.of());
        }, "Le constructeur avec valeur nulle devrait lever une exception");
    }

    // On teste le constructeur de lieu avec valeur négative
    @Test
    public void test_07_ConstructeurLieuIdNegatif() {
        assertThrows(NullPointerException.class, () -> {
            lieu = new Lieu("Salle", -1, List.of());
        }, "Le constructeur avec une valeur négative devrait lever une exception");
    }

    // On teste le constructeur de lieu avec valeur positive possible
    @Test
    public void test_08_ConstructeurLieuIdPositifPossible() {
        lieu = new Lieu("Salle", 3, List.of());
        assertEquals(3, lieu.getId(), "L'id du lieu devrait être 3");
    }

    // On teste le constructeur de lieu avec valeur positive impossible
    @Test
    public void test_09_ConstructeurLieuIdPositifImpossible() {
        assertThrows(IllegalArgumentException.class, () -> {
            lieu = new Lieu("Salle", 7, List.of());
        }, "Le constructeur avec une valeur positive impossible devrait lever une exception");
    }

    // ON TESTE LE CONSTRUCTEUR DE LIEU AVEC VALEURS CORRECTES

    @Test
    public void test_10_ConstructeurLieuCorrect() {
        lieu = new Lieu("Salle", 3, List.of());
        assertEquals("Salle", lieu.getNom(), "Le nom du lieu devrait être Salle");
        assertEquals(3, lieu.getId(), "L'id du lieu devrait être 3");
    }

    // CONSTRUCTEURS DE LIEU 2

    // PARTIE DE L'ID

    // On teste le constructeur de lieu avec valeur égale à 0
    @Test
    public void test_11_ConstructeurLieuIdNul() {
        assertThrows(IllegalArgumentException.class, () -> {
            lieu = new Lieu(0);
        }, "Le constructeur avec valeur nulle devrait lever une exception");
    }

    // On teste le constructeur de lieu avec valeur null
    @Test
    public void test_12_ConstructeurLieuIdNegatif() {
        assertThrows(NullPointerException.class, () -> {
            lieu = new Lieu((Integer) null);
        }, "Le constructeur avec valeur nulle devrait lever une exception");
    }

    // On teste le constructeur de lieu avec valeur négative
    @Test
    public void test_13_ConstructeurLieuIdNegatif() {
        assertThrows(NullPointerException.class, () -> {
            lieu = new Lieu(-1);
        }, "Le constructeur avec une valeur négative devrait lever une exception");
    }

    // On teste le constructeur de lieu avec valeur positive possible
    @Test
    public void test_14_ConstructeurLieuIdPositifPossible() {
        lieu = new Lieu(3);
        assertEquals(3, lieu.getId(), "L'id du lieu devrait être 3");
    }

    // On teste le constructeur de lieu avec valeur positive impossible
    @Test
    public void test_15_ConstructeurLieuIdPositifImpossible() {
        assertThrows(IllegalArgumentException.class, () -> {
            lieu = new Lieu(7);
        }, "Le constructeur avec une valeur positive impossible devrait lever une exception");
    }

}
