package Jeu.Deroulement;

import Kronologic.Jeu.Deroulement;
import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Realite;
import Kronologic.Jeu.Elements.Temps;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestDeroulementConstructeur {

    private Deroulement deroulement;

    // On teste le constructeur de Deroulement avec une liste de Réalité null
    @Test
    public void test_01_ConstructeurDeroulementListeRealiteNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            deroulement = new Deroulement(null);
        }, "Le constructeur avec valeur nulle devrait lever une exception");
    }

    // On teste le constructeur de Deroulement avec une liste de Réalité vide
    @Test
    public void test_02_ConstructeurDeroulementListeRealiteVide() {
        assertThrows(IllegalArgumentException.class, () -> {
            deroulement = new Deroulement(new ArrayList<>());
        }, "Le constructeur avec valeur vide devrait lever une exception");
    }

    // On teste le constructeur de Deroulement avec une liste de Réalité non vide
    @Test
    public void test_03_ConstructeurDeroulementListeRealiteNonVide() {
        Realite realite1 = new Realite(new Lieu(1), new Temps(1), new Personnage("A"));
        Realite realite2 = new Realite(new Lieu(1), new Temps(1), new Personnage("B"));
        Realite realite3 = new Realite(new Lieu(1), new Temps(1), new Personnage("C"));
        ArrayList<Realite> listeRealite = new ArrayList<>();
        listeRealite.add(realite1);
        listeRealite.add(realite2);
        listeRealite.add(realite3);
        deroulement = new Deroulement(listeRealite);
        assertEquals(listeRealite, deroulement.getListePositions(), "La liste de réalité devrait être correcte");
    }

}
