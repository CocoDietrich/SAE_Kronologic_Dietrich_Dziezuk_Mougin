package Jeu.Enquete;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.Jeu.Enquete;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestEnqueteFaireDeduction {

    private Enquete enquete;

    @BeforeEach
    public void setUp() {
        enquete = new Enquete(1,
                "Enquete 1",
                "Synopsis 1",
                "Enigme 1",
                5,
                10,
                new Personnage("Baronne"),
                new Lieu("Salle", 3, List.of()),
                new Temps(1));
    }

    // Test avec tous les éléments à null
    @Test
    public void test_01_FaireDeductionNull() {
        assertThrows(NullPointerException.class, () -> {
            enquete.faireDeduction(null, null, null);
        });
    }

    // Test avec le personnage à null
    @Test
    public void test_02_FaireDeductionPersonnageNull() {
        assertThrows(NullPointerException.class, () -> {
            enquete.faireDeduction(new Lieu("Salle", 3, List.of()), null, new Temps(1));
        });
    }

    // Test avec le lieu à null
    @Test
    public void test_03_FaireDeductionLieuNull() {
        assertThrows(NullPointerException.class, () -> {
            enquete.faireDeduction(null, new Personnage("Baronne"), new Temps(1));
        });
    }

    // Test avec le temps à null
    @Test
    public void test_04_FaireDeductionTempsNull() {
        assertThrows(NullPointerException.class, () -> {
            enquete.faireDeduction(new Lieu("Salle", 3, List.of()), new Personnage("Baronne"), null);
        });
    }

    // Test avec tous les éléments valides mais déduction fausse (Lieu, Personnage, Temps différents)
    @Test
    public void test_05_FaireDeductionFaux() {
        assertFalse(enquete.faireDeduction(new Lieu("Scène", 4, List.of()), new Personnage("Aventurière"), new Temps(2)));
    }

    // Test avec tous les éléments valides mais déduction fausse (Lieu différent)
    @Test
    public void test_06_FaireDeductionFauxLieu() {
        assertFalse(enquete.faireDeduction(new Lieu("Scène", 4, List.of()), new Personnage("Baronne"), new Temps(1)));
    }

    // Test avec tous les éléments valides mais déduction fausse (Personnage différent)
    @Test
    public void test_07_FaireDeductionFauxPersonnage() {
        assertFalse(enquete.faireDeduction(new Lieu("Salle", 3, List.of()), new Personnage("Aventurière"), new Temps(1)));
    }

    // Test avec tous les éléments valides mais déduction fausse (Temps différent)
    @Test
    public void test_08_FaireDeductionFauxTemps() {
        assertFalse(enquete.faireDeduction(new Lieu("Salle", 3, List.of()), new Personnage("Baronne"), new Temps(2)));
    }

    // Test avec tous les éléments valides et déduction vraie
    @Test
    public void test_09_FaireDeductionVrai() {
        assert(enquete.faireDeduction(new Lieu("Salle", 3, List.of()), new Personnage("Baronne"), new Temps(1)));
    }
}
