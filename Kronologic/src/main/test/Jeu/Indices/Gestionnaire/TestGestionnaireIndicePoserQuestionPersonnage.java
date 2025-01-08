package Jeu.Indices.Gestionnaire;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Indice.GestionnaireIndices;
import Kronologic.Jeu.Indice.IndicePersonnage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestGestionnaireIndicePoserQuestionPersonnage {

    private GestionnaireIndices gestionnaireIndices;
    private IndicePersonnage indicePersonnage;

    @BeforeEach
    public void setUp() {
        indicePersonnage = new IndicePersonnage(new Lieu("Salle", 3, List.of()), 1, new Personnage("Baronne"), 1);
        gestionnaireIndices = new GestionnaireIndices(List.of(indicePersonnage));
    }

    // On teste la méthode poserQuestion avec les bonnes valeurs
    @Test
    public void test_01_PoserQuestionPersonnagePossible(){
        assertEquals(indicePersonnage, gestionnaireIndices.poserQuestionPersonnage(
                new Lieu("Salle", 3, List.of()), new Personnage("Baronne")),
                "La méthode poserQuestion devrait retourner l'indice correspondant");
    }

    // On teste la méthode poserQuestion avec un lieu null
    @Test
    public void test_02_PoserQuestionPersonnageLieuNull(){
        assertThrows(NullPointerException.class, () -> {
            gestionnaireIndices.poserQuestionPersonnage(null, new Personnage("Baronne"));
        }, "La méthode poserQuestion avec valeur nulle devrait lever une exception");
    }

    // On teste la méthode poserQuestion avec un personnage null
    @Test
    public void test_03_PoserQuestionPersonnagePersonnageNull(){
        assertThrows(NullPointerException.class, () -> {
            gestionnaireIndices.poserQuestionPersonnage(new Lieu("Salle", 3, List.of()), null);
        }, "La méthode poserQuestion avec valeur nulle devrait lever une exception");
    }
}
