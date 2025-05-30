package Jeu.Deroulement;

import Kronologic.Jeu.Deroulement;
import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Realite;
import Kronologic.Jeu.Elements.Temps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestDeroulementPositionTemps {

    private Deroulement deroulement;
    private Realite realite1;
    private Realite realite2;
    private Realite realite3;

    @BeforeEach
    public void setUp() {
        realite1 = new Realite(new Lieu(1), new Temps(1), new Personnage("A"));
        realite2 = new Realite(new Lieu(2), new Temps(2), new Personnage("B"));
        realite3 = new Realite(new Lieu(3), new Temps(2), new Personnage("C"));
        ArrayList<Realite> listeRealite = new ArrayList<>();
        listeRealite.add(realite1);
        listeRealite.add(realite2);
        listeRealite.add(realite3);
        deroulement = new Deroulement(listeRealite);
    }

    // On teste la méthode positionsAuTemps avec un temps qui existe (1)
    @Test
    public void test_01_PositionsAuTempsExistant() {
        ArrayList<Realite> listeRealite = new ArrayList<>();
        listeRealite.add(realite1);
        assertEquals(listeRealite, deroulement.positionsAuTemps(new Temps(1)),
                "La liste de réalité devrait être correcte");
    }

    // On teste la méthode positionsAuTemps avec un temps qui existe (2)
    @Test
    public void test_02_PositionsAuTempsExistant() {
        ArrayList<Realite> listeRealite = new ArrayList<>();
        listeRealite.add(realite2);
        listeRealite.add(realite3);
        assertEquals(listeRealite, deroulement.positionsAuTemps(new Temps(2)),
                "La liste de réalité devrait être correcte");
    }

    // On teste la méthode positionsAuTemps avec un temps qui n'existe pas (3)
    @Test
    public void test_03_PositionsAuTempsInexistant() {
        ArrayList<Realite> listeRealite = new ArrayList<>();
        assertEquals(listeRealite, deroulement.positionsAuTemps(new Temps(3)),
                "La liste de réalité devrait être vide");
    }

    // On teste la méthode positionsAuTemps avec un temps null
    @Test
    public void test_04_PositionsAuTempsNegatif() {
        assertThrows(NullPointerException.class, () -> {
            deroulement.positionsAuTemps(null);
        }, "La méthode avec valeur nulle devrait lever une exception");
    }

}
