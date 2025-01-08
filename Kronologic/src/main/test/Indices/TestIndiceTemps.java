package Indices;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.Jeu.Indice.IndiceTemps;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestIndiceTemps {


    private IndiceTemps indiceTemps;

    // On teste le constructeur avec les bonnes valeurs
    @Test
    public void test_01_ConstructeurIndiceTempsPossible() {
        indiceTemps = new IndiceTemps(new Lieu("Salle", 3, List.of()), 1, new Temps(1), "Baronne");
        assertEquals("Il y a 1 personne(s) dans le lieu Salle au pas de temps 1."
                + "\n De plus, le personnage Baronne est dans ce lieu à ce pas de temps.", indiceTemps.toString(), "Le toString devrait être correct");
    }

    // On teste le constructeur avec un lieu null
    @Test
    public void test_02_ConstructeurIndiceTempsLieuNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            indiceTemps = new IndiceTemps(null, 1, new Temps(1), "Baronne");
        }, "Le constructeur avec valeur nulle devrait lever une exception");
    }

    // On teste le constructeur avec une info publique négative
    @Test
    public void test_03_ConstructeurIndiceTempsInfoPubliqueNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            indiceTemps = new IndiceTemps(new Lieu("Salle", 3, List.of()), -1, new Temps(1), "Baronne");
        }, "Le constructeur avec valeur négative devrait lever une exception");
    }

    // On teste le constructeur avec un temps null
    @Test
    public void test_04_ConstructeurIndiceTempsTempsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            indiceTemps = new IndiceTemps(new Lieu("Salle", 3, List.of()), 1, null, "Baronne");
        }, "Le constructeur avec valeur nulle devrait lever une exception");
    }

    // On teste le constructeur avec une info privée nulle
    @Test
    public void test_05_ConstructeurIndiceTempsInfoPriveNulle() {
        assertThrows(IllegalArgumentException.class, () -> {
            indiceTemps = new IndiceTemps(new Lieu("Salle", 3, List.of()), 1, new Temps(1), "");
        }, "Le constructeur avec valeur nulle devrait lever une exception");
    }

    // On teste le constructeur avec une info privée vide
    @Test
    public void test_06_ConstructeurIndiceTempsInfoPriveVide() {
        assertThrows(IllegalArgumentException.class, () -> {
            indiceTemps = new IndiceTemps(new Lieu("Salle", 3, List.of()), 1, new Temps(1), "");
        }, "Le constructeur avec valeur vide devrait lever une exception");
    }


}
