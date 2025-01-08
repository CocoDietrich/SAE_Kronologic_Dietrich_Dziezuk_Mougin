package Jeu.Deroulement;

import Kronologic.Jeu.Deroulement;
import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Realite;
import Kronologic.Jeu.Elements.Temps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestDeroulementPositionLieu {

    private Deroulement deroulement;
    private Realite realite1;
    private Realite realite2;
    private Realite realite3;

    @BeforeEach
    public void setUp() {
        realite1 = new Realite(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("A"));
        realite2 = new Realite(new Lieu("Scène", 4, List.of()), new Temps(2), new Personnage("B"));
        realite3 = new Realite(new Lieu("Scène", 4, List.of()), new Temps(3), new Personnage("C"));
        ArrayList<Realite> listeRealite = new ArrayList<>();
        listeRealite.add(realite1);
        listeRealite.add(realite2);
        listeRealite.add(realite3);
        deroulement = new Deroulement(listeRealite);
    }

    // On teste la méthode positionsAuTemps avec un lieu qui existe (Salle)
    @Test
    public void test_01_PositionsLieuExistant() {
        ArrayList<Realite> listeRealite = new ArrayList<>();
        listeRealite.add(realite1);
        assertEquals(listeRealite, deroulement.positionsDansLieu(new Lieu("Salle", 3, List.of())),
                "La liste de réalité devrait être correcte");
    }

    // On teste la méthode positionsAuTemps avec un lieu qui existe (Scène)
    @Test
    public void test_02_PositionsLieuExistant() {
        ArrayList<Realite> listeRealite = new ArrayList<>();
        listeRealite.add(realite2);
        listeRealite.add(realite3);
        assertEquals(listeRealite, deroulement.positionsDansLieu(new Lieu("Scène", 4, List.of())),
                "La liste de réalité devrait être correcte");
    }

    // On teste la méthode positionsAuTemps avec un temps qui n'existe pas (Grand foyer)
    @Test
    public void test_03_PositionsLieuInexistant() {
        ArrayList<Realite> listeRealite = new ArrayList<>();
        assertEquals(listeRealite, deroulement.positionsDansLieu(new Lieu("Grand foyer", 1, List.of())),
                "La liste de réalité devrait être vide");
    }

    // On teste la méthode positionsAuTemps avec un lieu null
    @Test
    public void test_04_PositionsLieuNegatif() {
        assertThrows(NullPointerException.class, () -> {
            deroulement.positionsDansLieu(null);
        });
    }

}
