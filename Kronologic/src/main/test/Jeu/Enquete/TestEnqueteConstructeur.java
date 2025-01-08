package Jeu.Enquete;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.Jeu.Enquete;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestEnqueteConstructeur {

    private Enquete enquete;

    // On teste le constructeur de la classe Enquete
    @Test
    public void test_01_EnqueteConstructeur() {
        enquete = new Enquete(1,
                "Enquete 1",
                "Synopsis 1",
                "Enigme 1",
                5,
                10,
                new Personnage("Baronne"),
                new Lieu("Salle", 3, List.of()),
                new Temps(2));
        assertEquals(1, enquete.getIdEnquete());
        assertEquals("Enquete 1", enquete.getNomEnquete());
        assertEquals("Synopsis 1", enquete.getSynopsis());
        assertEquals("Enigme 1", enquete.getEnigme());
        assertEquals(5, enquete.getLoupeOr());
        assertEquals(10, enquete.getLoupeBronze());
        assertEquals("Baronne", enquete.getMeurtrier().getNom());
        assertEquals("Salle", enquete.getLieuMeurtre().getNom());
        assertEquals(2, enquete.getTempsMeurtre().getValeur());
    }

    // On teste le constructeur de la classe Enquete avec un id négatif
    @Test
    public void test_02_EnqueteConstructeurIdNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            enquete = new Enquete(-1,
                    "Enquete 1",
                    "Synopsis 1",
                    "Enigme 1",
                    5,
                    10,
                    new Personnage("Baronne"),
                    new Lieu("Salle", 3, List.of()),
                    new Temps(2));
        });
    }

    // On teste le constructeur de la classe Enquete avec un nom null
    @Test
    public void test_03_EnqueteConstructeurNomNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            enquete = new Enquete(1,
                    null,
                    "Synopsis 1",
                    "Enigme 1",
                    5,
                    10,
                    new Personnage("Baronne"),
                    new Lieu("Salle", 3, List.of()),
                    new Temps(2));
        });
    }

    // On teste le constructeur de la classe Enquete avec un nom vide
    @Test
    public void test_04_EnqueteConstructeurNomVide() {
        assertThrows(IllegalArgumentException.class, () -> {
            enquete = new Enquete(1,
                    "",
                    "Synopsis 1",
                    "Enigme 1",
                    5,
                    10,
                    new Personnage("Baronne"),
                    new Lieu("Salle", 3, List.of()),
                    new Temps(2));
        });
    }

    // On teste le constructeur de la classe Enquete avec un synopsis null
    @Test
    public void test_05_EnqueteConstructeurSynopsisNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            enquete = new Enquete(1,
                    "Enquete 1",
                    null,
                    "Enigme 1",
                    5,
                    10,
                    new Personnage("Baronne"),
                    new Lieu("Salle", 3, List.of()),
                    new Temps(2));
        });
    }

    // On teste le constructeur de la classe Enquete avec un synopsis vide
    @Test
    public void test_06_EnqueteConstructeurSynopsisVide() {
        assertThrows(IllegalArgumentException.class, () -> {
            enquete = new Enquete(1,
                    "Enquete 1",
                    "",
                    "Enigme 1",
                    5,
                    10,
                    new Personnage("Baronne"),
                    new Lieu("Salle", 3, List.of()),
                    new Temps(2));
        });
    }

    // On teste le constructeur de la classe Enquete avec une enigme null
    @Test
    public void test_07_EnqueteConstructeurEnigmeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            enquete = new Enquete(1,
                    "Enquete 1",
                    "Synopsis 1",
                    null,
                    5,
                    10,
                    new Personnage("Baronne"),
                    new Lieu("Salle", 3, List.of()),
                    new Temps(2));
        });
    }

    // On teste le constructeur de la classe Enquete avec une enigme vide
    @Test
    public void test_08_EnqueteConstructeurEnigmeVide() {
        assertThrows(IllegalArgumentException.class, () -> {
            enquete = new Enquete(1,
                    "Enquete 1",
                    "Synopsis 1",
                    "",
                    5,
                    10,
                    new Personnage("Baronne"),
                    new Lieu("Salle", 3, List.of()),
                    new Temps(2));
        });
    }

    // On teste le constructeur de la classe Enquete avec un loupeOr négatif
    @Test
    public void test_09_EnqueteConstructeurLoupeOrNegatif() {
        assertThrows(IllegalArgumentException.class, () -> {
            enquete = new Enquete(1,
                    "Enquete 1",
                    "Synopsis 1",
                    "Enigme 1",
                    -5,
                    10,
                    new Personnage("Baronne"),
                    new Lieu("Salle", 3, List.of()),
                    new Temps(2));
        });
    }

    // On teste le constructeur de la classe Enquete avec un loupeBronze négatif
    @Test
    public void test_10_EnqueteConstructeurLoupeBronzeNegatif() {
        assertThrows(IllegalArgumentException.class, () -> {
            enquete = new Enquete(1,
                    "Enquete 1",
                    "Synopsis 1",
                    "Enigme 1",
                    5,
                    -10,
                    new Personnage("Baronne"),
                    new Lieu("Salle", 3, List.of()),
                    new Temps(2));
        });
    }

    // On teste le constructeur de la classe Enquete avec un meurtrier null
    @Test
    public void test_11_EnqueteConstructeurMeurtrierNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            enquete = new Enquete(1,
                    "Enquete 1",
                    "Synopsis 1",
                    "Enigme 1",
                    5,
                    10,
                    null,
                    new Lieu("Salle", 3, List.of()),
                    new Temps(2));
        });
    }

    // On teste le constructeur de la classe Enquete avec un lieuMeurtre null
    @Test
    public void test_12_EnqueteConstructeurLieuMeurtreNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            enquete = new Enquete(1,
                    "Enquete 1",
                    "Synopsis 1",
                    "Enigme 1",
                    5,
                    10,
                    new Personnage("Baronne"),
                    null,
                    new Temps(2));
        });
    }

    // On teste le constructeur de la classe Enquete avec un tempsMeurtre null
    @Test
    public void test_13_EnqueteConstructeurTempsMeurtreNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            enquete = new Enquete(1,
                    "Enquete 1",
                    "Synopsis 1",
                    "Enigme 1",
                    5,
                    10,
                    new Personnage("Baronne"),
                    new Lieu("Salle", 3, List.of()),
                    null);
        });
    }

    // On teste le constructeur de la classe Enquete avec tous les arguments null
    @Test
    public void test_14_EnqueteConstructeurTousNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            enquete = new Enquete(-1,
                    null,
                    null,
                    null,
                    -5,
                    -10,
                    null,
                    null,
                    null);
        });
    }
}
