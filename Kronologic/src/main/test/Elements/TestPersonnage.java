package Elements;

import Kronologic.Jeu.Elements.Personnage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestPersonnage {

    private Personnage personnage;

    // On teste le constructeur de personnage avec valeur égale à ""
    @Test
    public void test_01_ConstructeurPersonnageVide() {
        assertThrows(IllegalArgumentException.class, () -> {
            personnage = new Personnage("");
        }, "Le constructeur avec valeur nulle devrait lever une exception");
    }

    // On teste le constructeur de personnage avec valeur nulle
    @Test
    public void test_02_ConstructeurPersonnageNul() {
        assertThrows(IllegalArgumentException.class, () -> {
            personnage = new Personnage(null);
        }, "Le constructeur avec valeur nulle devrait lever une exception");
    }

    // On teste le constructeur de personnage avec valeur possible
    @Test
    public void test_03_ConstructeurPersonnagePossible() {
        personnage = new Personnage("Aventurière");
        assertEquals("Aventurière", personnage.getNom(), "Le nom du personnage devrait être Aventurière");
    }

    // On teste le constructeur de personnage avec valeur impossible
    @Test
    public void test_04_ConstructeurPersonnageImpossible() {
        assertThrows(IllegalArgumentException.class, () -> {
            personnage = new Personnage("Policier");
        }, "Le constructeur avec une valeur impossible devrait lever une exception");
    }
}
