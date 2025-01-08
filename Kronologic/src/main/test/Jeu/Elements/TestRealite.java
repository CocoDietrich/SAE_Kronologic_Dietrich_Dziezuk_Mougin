package Jeu.Elements;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Realite;
import Kronologic.Jeu.Elements.Temps;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestRealite {

    // On teste le constructeur de Realite avec les paramètres à null
    @Test
    public void test_01_RealiteNull(){
        assertThrows(IllegalArgumentException.class, () -> {
            Realite realite = new Realite(null, null, null);
        });
    }

    // On teste le constructeur de Realite avec le paramètre lieu à null
    @Test
    public void test_02_RealiteLieuNull(){
        assertThrows(IllegalArgumentException.class, () -> {
            Realite realite = new Realite(null, new Temps(1), new Personnage("A"));
        });
    }

    // On teste le constructeur de Realite avec le paramètre temps à null
    @Test
    public void test_03_RealiteTempsNull(){
        assertThrows(IllegalArgumentException.class, () -> {
            Realite realite = new Realite(new Lieu(1), null, new Personnage("A"));
        });
    }

    // On teste le constructeur de Realite avec le paramètre personnage à null
    @Test
    public void test_04_RealitePersonnageNull(){
        assertThrows(IllegalArgumentException.class, () -> {
            Realite realite = new Realite(new Lieu(1), new Temps(1), null);
        });
    }

    // On teste le constructeur de Realite avec les paramètres non null
    @Test
    public void test_05_Realite(){
        Realite realite = new Realite(new Lieu(1), new Temps(1), new Personnage("A"));
        assertEquals(1, realite.getLieu().getId());
        assertEquals(1, realite.getTemps().getValeur());
        assertEquals("A", realite.getPersonnage().getNom());
    }
}
