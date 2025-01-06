package Elements;

import Kronologic.Jeu.Elements.Elements;
import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestElements {

    private Elements elements;

    // On teste le constructeur d'éléments avec les listes à null
    @Test
    public void test_01_ElementsNull(){
        assertThrows(IllegalArgumentException.class, () -> {
            elements = new Elements(null, null);
        });
    }

    // On teste le constructeur d'éléments avec la liste de personnages à null
    @Test
    public void test_02_PersonnagesNull(){
        assertThrows(IllegalArgumentException.class, () -> {
            elements = new Elements(null, List.of(new Lieu(1),
                    new Lieu(2),
                    new Lieu(3),
                    new Lieu(4),
                    new Lieu(5),
                    new Lieu(6)));
        });
    }

    // On teste le constructeur d'éléments avec la liste de lieux à null
    @Test
    public void test_03_LieuxNull(){
        assertThrows(IllegalArgumentException.class, () -> {
            elements = new Elements(List.of(new Personnage("Aventurière"),
                    new Personnage("Baronne"),
                    new Personnage("Chauffeur"),
                    new Personnage("Détective"),
                    new Personnage("Journaliste"),
                    new Personnage("Servante")), null);
        });
    }

    // On teste le constructeur d'éléments avec les deux listes vides
    @Test
    public void test_04_ElementsVides(){
        assertThrows(IllegalArgumentException.class, () -> {
            elements = new Elements(List.of(), List.of());
        });
    }

    // On teste le constructeur d'éléments avec la liste de personnages vide
    @Test
    public void test_05_PersonnagesVide(){
        assertThrows(IllegalArgumentException.class, () -> {
            elements = new Elements(List.of(), List.of(new Lieu(1),
                    new Lieu(2),
                    new Lieu(3),
                    new Lieu(4),
                    new Lieu(5),
                    new Lieu(6)));
        });
    }

    // On teste le constructeur d'éléments avec la liste de lieux vide
    @Test
    public void test_06_LieuxVide(){
        assertThrows(IllegalArgumentException.class, () -> {
            elements = new Elements(List.of(new Personnage("Aventurière"),
                    new Personnage("Baronne"),
                    new Personnage("Chauffeur"),
                    new Personnage("Détective"),
                    new Personnage("Journaliste"),
                    new Personnage("Servante")), List.of());
        });
    }



}
