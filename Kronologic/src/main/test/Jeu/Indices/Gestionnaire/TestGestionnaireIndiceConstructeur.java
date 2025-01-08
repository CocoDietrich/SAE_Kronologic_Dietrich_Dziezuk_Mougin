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

public class TestGestionnaireIndiceConstructeur {

    private GestionnaireIndices gestionnaireIndices;

    // On teste le constructeur avec une liste d'indices null
    @Test
    public void test_01_ConstructeurGestionnaireIndicesListeIndicesNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            gestionnaireIndices = new GestionnaireIndices(null);
        }, "Le constructeur avec valeur nulle devrait lever une exception");
    }

    // On teste le constructeur avec une liste d'indices vide
    @Test
    public void test_02_ConstructeurGestionnaireIndicesListeIndicesVide() {
        assertThrows(IllegalArgumentException.class, () -> {
            gestionnaireIndices = new GestionnaireIndices(List.of());
        }, "Le constructeur avec valeur vide devrait lever une exception");
    }

    // On teste le constructeur avec une liste d'indices non vide
    @Test
    public void test_03_ConstructeurGestionnaireIndicesListeIndicesNonVide() {
        IndicePersonnage indicePersonnage = new IndicePersonnage(new Lieu("Salle", 3, List.of()), 1, new Personnage("Baronne"), 1);
        IndiceTemps indiceTemps = new IndiceTemps(new Lieu("Salle", 3, List.of()), 1, new Temps(1), "Baronne");
        gestionnaireIndices = new GestionnaireIndices(List.of(indicePersonnage, indiceTemps));
        assertEquals(List.of(indicePersonnage, indiceTemps), gestionnaireIndices.getListeIndices(), "La liste d'indices devrait être correcte");
    }

}
