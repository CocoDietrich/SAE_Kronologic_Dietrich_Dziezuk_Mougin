package Elements;

import Kronologic.Jeu.Elements.Temps;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestTemps {

    private Temps temps;

    // On teste le constructeur de temps avec valeur égale à 0
    @Test
    public void test_01_ConstructeurTemps0() {
        assertThrows(NullPointerException.class, () -> {
            temps = new Temps(0);
        }, "Le constructeur avec valeur nulle devrait lever une exception");
    }

    // On teste le constructeur de temps avec valeur nulle
    @Test
    public void test_02_ConstructeurTempsNul() {
        assertThrows(NullPointerException.class, () -> {
            temps = new Temps((Integer) null);
        }, "Le constructeur avec valeur nulle devrait lever une exception");
    }

    // On teste le constructeur de temps avec valeur négative
    @Test
    public void test_03_ConstructeurTempsNegatif() {
        assertThrows(IllegalArgumentException.class, () -> {
            temps = new Temps(-5);
        }, "Le constructeur avec une valeur négative devrait lever une exception");
    }

    // On teste le constructeur de temps avec valeur positive
    @Test
    public void test_04_ConstructeurTempsPositif() {
        temps = new Temps(3);
        assertEquals(3, temps.getValeur(), "La valeur du temps devrait être 3");
    }

    // On teste le constructeur de temps avec valeur positive supérieure à 6
    @Test
    public void test_05_ConstructeurTempsSup6() {
        assertThrows(IllegalArgumentException.class, () -> {
            temps = new Temps(7);
        }, "Le constructeur avec une valeur supérieure à 6 devrait lever une exception");
    }
}
