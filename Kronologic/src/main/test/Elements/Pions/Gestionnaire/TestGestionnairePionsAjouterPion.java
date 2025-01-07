package Elements.Pions.Gestionnaire;

import Kronologic.Jeu.Elements.*;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestGestionnairePionsAjouterPion {

    private GestionnairePions gestionnairePions;

    @BeforeEach
    public void setUp() {
        gestionnairePions = new GestionnairePions();
    }

    @BeforeAll
    public static void initToolkit() {
        // Initialise JavaFX
        Platform.startup(() -> {});
    }

    // On teste l'ajout d'un pion
    @Test
    public void test_01_ajoutPion() {
        Pion pion = new Pion(new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière")), "file:img/pions_personnages/Aventurière.png");
        gestionnairePions.ajouterPion(pion);
        assertTrue(gestionnairePions.getPions().contains(pion), "Le pion devrait être ajouté");
    }

    // On teste l'ajout de plusieurs pions
    @Test
    public void test_02_ajoutPions() {
        Pion pion1 = new Pion(new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière")), "file:img/pions_personnages/Aventurière.png");
        Pion pion2 = new Pion(new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Baronne")), "file:img/pions_personnages/Baronne.png");
        gestionnairePions.ajouterPion(pion1);
        gestionnairePions.ajouterPion(pion2);
        assertTrue(gestionnairePions.getPions().contains(pion1), "Le pion 1 devrait être ajouté");
        assertTrue(gestionnairePions.getPions().contains(pion2), "Le pion 2 devrait être ajouté");
    }

    // On teste l'ajout de le même pion en double
    @Test
    public void test_03_ajoutNoteDouble() {
        Pion pion = new Pion(new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière")), "file:img/pions_personnages/Aventurière.png");
        gestionnairePions.ajouterPion(pion);
        gestionnairePions.ajouterPion(pion);
        assertEquals(1, gestionnairePions.getPions().size(), "Le pion ne devrait pas être ajouté en double");
    }

    // On teste l'ajout d'un pion null
    @Test
    public void test_04_ajoutNoteNull() {
        gestionnairePions.ajouterPion(null);
        assertEquals(0, gestionnairePions.getPions().size(), "Le pion ne devrait pas être ajouté");
    }
}
