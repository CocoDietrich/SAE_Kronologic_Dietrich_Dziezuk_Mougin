package Jeu.Enquete;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.Jeu.Enquete;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestEnqueteVerifierLoupe {

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

    // Test avec un nombre de coups négatif
    @Test
    public void test_01_VerifierLoupeNegatif() {
        assertThrows(IllegalArgumentException.class, () -> {
            enquete.verifierLoupe(-1);
        });
    }

    // Test avec un nombre de coups équivalent à la loupe d'or
    @Test
    public void test_02_VerifierLoupeLoupeOr() {
        assertEquals("loupe Or en 5 coups !", enquete.verifierLoupe(5));
    }

    // Test avec un nombre de coups inférieur à la loupe d'or
    @Test
    public void test_03_VerifierLoupeInferieurLoupeOr() {
        assertEquals("loupe Or en 4 coups !", enquete.verifierLoupe(4));
    }

    // Test avec un nombre de coups équivalent à la loupe de bronze
    @Test
    public void test_04_VerifierLoupeLoupeBronze() {
        assertEquals("loupe Bronze en 10 coups !", enquete.verifierLoupe(10));
    }

    // Test avec un nombre de coups supérieur à la loupe de bronze
    @Test
    public void test_05_VerifierLoupeSuperieurLoupeBronze() {
        assertEquals("loupe Bronze en 11 coups !", enquete.verifierLoupe(11));
    }

    // Test avec un nombre de coups supérieur à la loupe d'or et inférieur à la loupe de bronze
    @Test
    public void test_06_VerifierLoupeEntreLoupeOrEtLoupeBronze() {
        assertEquals("loupe Argent en 6 coups !", enquete.verifierLoupe(6));
    }
}
