package Indices;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Indice.IndicePersonnage;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestIndicePersonnage {

    private IndicePersonnage indicePersonnage;

    // On teste le constructeur avec les bonnes valeurs
    @Test
    public void test_01_ConstructeurIndicePersonnagePossible() {
        indicePersonnage = new IndicePersonnage(new Lieu("Salle", 3, List.of()), 1, new Personnage("Baronne"), 1);
        assertEquals("Le personnage Baronne est passé 1 fois dans le lieu Salle."
                + "\n De plus, il était présent dans ce le lieu au pas de temps 1.", indicePersonnage.toString(), "Le toString devrait être correct");
    }

    // On teste le constructeur avec un lieu null
    @Test
    public void test_02_ConstructeurIndicePersonnageLieuNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            indicePersonnage = new IndicePersonnage(null, 1, new Personnage("Baronne"), 1);
        }, "Le constructeur avec valeur nulle devrait lever une exception");
    }

    // On teste le constructeur avec une info publique négative
    @Test
    public void test_03_ConstructeurIndicePersonnageInfoPubliqueNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            indicePersonnage = new IndicePersonnage(new Lieu("Salle", 3, List.of()), -1, new Personnage("Baronne"), 1);
        }, "Le constructeur avec valeur négative devrait lever une exception");
    }

    // On teste le constructeur avec un personnage null
    @Test
    public void test_04_ConstructeurIndicePersonnagePersonnageNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            indicePersonnage = new IndicePersonnage(new Lieu("Salle", 3, List.of()), 1, null, 1);
        }, "Le constructeur avec valeur nulle devrait lever une exception");
    }

    // On teste le constructeur avec une info privée négative
    @Test
    public void test_05_ConstructeurIndicePersonnageInfoPriveNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            indicePersonnage = new IndicePersonnage(new Lieu("Salle", 3, List.of()), 1, new Personnage("Baronne"), -1);
        }, "Le constructeur avec valeur négative devrait lever une exception");
    }
}
