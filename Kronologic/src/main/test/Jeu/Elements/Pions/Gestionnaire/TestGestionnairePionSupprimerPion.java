package Jeu.Elements.Pions.Gestionnaire;

import Kronologic.Jeu.Elements.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import kronologic.outils.InitialiseurJavaFX;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestGestionnairePionSupprimerPion {

    private GestionnairePions gestionnairePions;

    @BeforeEach
    public void init() {
        gestionnairePions = new GestionnairePions();
    }

    @BeforeAll
    public static void setUp() {
        InitialiseurJavaFX.initToolkit();
    }

    // On teste la suppression d'un pion
    @Test
    public void test_01_supprimerPion() {
        Pion pion = new Pion(new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière")),
                "file:img/pions_personnages/Aventurière.png");
        gestionnairePions.ajouterPion(pion);
        gestionnairePions.supprimerPion(pion);
        assertFalse(gestionnairePions.getPions().contains(pion), "Le pion devrait être supprimé");
    }

    // On teste la suppression d'un pion null
    @Test
    public void test_02_supprimerPionNull() {
        Pion pion = new Pion(new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière")),
                "file:img/pions_personnages/Aventurière.png");
        gestionnairePions.ajouterPion(pion);
        gestionnairePions.supprimerPion(null);
        assertFalse(gestionnairePions.getPions().contains(null), "Le pion ne devrait pas être supprimé");
    }

    // On teste la suppression d'un pion non présent
    @Test
    public void test_03_supprimerPionNonPresent() {
        Pion pion = new Pion(new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière")),
                "file:img/pions_personnages/Aventurière.png");
        Pion pion2 = new Pion(new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Baronne")),
                "file:img/pions_personnages/Baronne.png");
        gestionnairePions.ajouterPion(pion);
        gestionnairePions.supprimerPion(pion2);
        assertTrue(gestionnairePions.getPions().contains(pion), "Le pion ne devrait pas être supprimé");
    }

}
