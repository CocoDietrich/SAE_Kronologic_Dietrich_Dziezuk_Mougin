package Jeu.Indices.Gestionnaire;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.Jeu.Indice.GestionnaireIndices;
import Kronologic.Jeu.Indice.IndicePersonnage;
import Kronologic.Jeu.Indice.IndiceTemps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestGestionnaireIndicePoserQuestionTemps {

    private GestionnaireIndices gestionnaireIndices;
    private IndiceTemps indiceTemps;

    @BeforeEach
    public void setUp() {
        indiceTemps = new IndiceTemps(new Lieu("Salle", 3, List.of()), 1, new Temps(1), "Baronne");
        gestionnaireIndices = new GestionnaireIndices(List.of(indiceTemps));
    }

    // On teste la méthode poserQuestion avec les bonnes valeurs
    @Test
    public void test_01_PoserQuestionTempsPossible(){
        assertEquals(indiceTemps, gestionnaireIndices.poserQuestionTemps(
                new Lieu("Salle", 3, List.of()), new Temps(1)),
                "La méthode poserQuestion devrait retourner l'indice correspondant");
    }

    // On teste la méthode poserQuestion avec un lieu null
    @Test
    public void test_02_PoserQuestionTempsLieuNull(){
        assertThrows(NullPointerException.class, () -> {
            gestionnaireIndices.poserQuestionTemps(null, new Temps(1));
        }, "La méthode poserQuestion avec valeur nulle devrait lever une exception");
    }

    // On teste la méthode poserQuestion avec un personnage null
    @Test
    public void test_03_PoserQuestionTempsTempsNull(){
        assertThrows(NullPointerException.class, () -> {
            gestionnaireIndices.poserQuestionTemps(new Lieu("Salle", 3, List.of()), null);
        }, "La méthode poserQuestion avec valeur nulle devrait lever une exception");
    }
}
