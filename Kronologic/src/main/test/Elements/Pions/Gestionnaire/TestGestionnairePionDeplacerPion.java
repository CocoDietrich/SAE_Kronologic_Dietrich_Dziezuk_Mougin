package Elements.Pions.Gestionnaire;

import Kronologic.Jeu.Elements.*;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGestionnairePionDeplacerPion {

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

    // TEST AVEC PERSONNAGE

    // On teste la méthode avec un lieu
    @Test
    public void test_01_deplacerPionLieu(){
        Pion pion = new Pion(new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière")),
                "file:img/pions_personnages/Aventurière.png");
        gestionnairePions.ajouterPion(pion);
        gestionnairePions.deplacerPion(pion, new Lieu("Scène", 4, List.of()), new Temps(1), 0, 0);
        assertEquals("Scène", gestionnairePions.getPions().getFirst().getNote().getLieu().getNom());
    }

    // On teste la méthode avec un temps
    @Test
    public void test_02_deplacerPionTemps(){
        Pion pion = new Pion(new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière")),
                "file:img/pions_personnages/Aventurière.png");
        gestionnairePions.ajouterPion(pion);
        gestionnairePions.deplacerPion(pion, new Lieu("Salle", 3, List.of()), new Temps(2), 0, 0);
        assertEquals(2, gestionnairePions.getPions().getFirst().getNote().getTemps().getValeur());
    }

    // On teste la méthode avec un lieu et un temps
    @Test
    public void test_03_deplacerPionLieuTemps(){
        Pion pion = new Pion(new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière")),
                "file:img/pions_personnages/Aventurière.png");
        gestionnairePions.ajouterPion(pion);
        gestionnairePions.deplacerPion(pion, new Lieu("Scène", 4, List.of()), new Temps(2), 0, 0);
        assertEquals("Scène", gestionnairePions.getPions().getFirst().getNote().getLieu().getNom());
        assertEquals(2, gestionnairePions.getPions().getFirst().getNote().getTemps().getValeur());
    }

    // On teste la méthode avec un lieu null et un temps null
    @Test
    public void test_04_deplacerPionLieuTempsNull(){
        Pion pion = new Pion(new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière")),
                "file:img/pions_personnages/Aventurière.png");
        gestionnairePions.ajouterPion(pion);
        gestionnairePions.deplacerPion(pion, null, null, 0, 0);
        assertEquals("Salle", gestionnairePions.getPions().getFirst().getNote().getLieu().getNom());
        assertEquals(1, gestionnairePions.getPions().getFirst().getNote().getTemps().getValeur());
    }

    // On teste la méthode avec un pion null
    @Test
    public void test_05_deplacerPionPionNull(){
        Pion pion = new Pion(new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière")),
                "file:img/pions_personnages/Aventurière.png");
        gestionnairePions.ajouterPion(pion);
        gestionnairePions.deplacerPion(null, new Lieu("Scène", 4, List.of()), new Temps(1), 0, 0);
        assertEquals("Salle", gestionnairePions.getPions().getFirst().getNote().getLieu().getNom());
    }

    // On teste la méthode en déplaçant un pion de manière à avoir un doublon
    @Test
    public void test_06_deplacerPionDoublon(){
        Pion pion1 = new Pion(new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière")),
                "file:img/pions_personnages/Aventurière.png");
        pion1.deplacerPion(0, 0);
        Pion pion2 = new Pion(new Note(new Lieu("Scène", 4, List.of()), new Temps(1), new Personnage("Aventurière")),
                "file:img/pions_personnages/Aventurière.png");
        gestionnairePions.ajouterPion(pion1);
        gestionnairePions.ajouterPion(pion2);
        gestionnairePions.deplacerPion(pion1, new Lieu("Scène", 4, List.of()), new Temps(1), 0, 0);
        assertEquals(1, gestionnairePions.getPions().size());
        assertEquals("Scène", gestionnairePions.getPions().getFirst().getNote().getLieu().getNom());
        assertEquals("Aventurière", gestionnairePions.getPions().getFirst().getNote().getPersonnage().getNom());
    }

    // TEST AVEC NOMBRE DE PERSONNAGE

    // On teste la méthode avec un lieu
    @Test
    public void test_07_deplacerPionLieu(){
        Pion pion = new Pion(new Note(new Lieu("Salle", 3, List.of()), new Temps(1), 2),
                "file:img/pions_personnages/Aventurière.png");
        gestionnairePions.ajouterPion(pion);
        gestionnairePions.deplacerPion(pion, new Lieu("Scène", 4, List.of()), new Temps(1), 0, 0);
        assertEquals("Scène", gestionnairePions.getPions().getFirst().getNote().getLieu().getNom());
    }

    // On teste la méthode avec un temps
    @Test
    public void test_08_deplacerPionTemps(){
        Pion pion = new Pion(new Note(new Lieu("Salle", 3, List.of()), new Temps(1), 2),
                "file:img/pions_personnages/Aventurière.png");
        gestionnairePions.ajouterPion(pion);
        gestionnairePions.deplacerPion(pion, new Lieu("Salle", 3, List.of()), new Temps(2), 0, 0);
        assertEquals(2, gestionnairePions.getPions().getFirst().getNote().getTemps().getValeur());
    }

    // On teste la méthode avec un lieu et un temps
    @Test
    public void test_09_deplacerPionLieuTemps(){
        Pion pion = new Pion(new Note(new Lieu("Salle", 3, List.of()), new Temps(1), 2),
                "file:img/pions_personnages/Aventurière.png");
        gestionnairePions.ajouterPion(pion);
        gestionnairePions.deplacerPion(pion, new Lieu("Scène", 4, List.of()), new Temps(2), 0, 0);
        assertEquals("Scène", gestionnairePions.getPions().getFirst().getNote().getLieu().getNom());
        assertEquals(2, gestionnairePions.getPions().getFirst().getNote().getTemps().getValeur());
    }

    // On teste la méthode avec un lieu null et un temps null
    @Test
    public void test_10_deplacerPionLieuTempsNull(){
        Pion pion = new Pion(new Note(new Lieu("Salle", 3, List.of()), new Temps(1), 2),
                "file:img/pions_personnages/Aventurière.png");
        gestionnairePions.ajouterPion(pion);
        gestionnairePions.deplacerPion(pion, null, null, 0, 0);
        assertEquals("Salle", gestionnairePions.getPions().getFirst().getNote().getLieu().getNom());
        assertEquals(1, gestionnairePions.getPions().getFirst().getNote().getTemps().getValeur());
    }

    // On teste la méthode avec un pion null
    @Test
    public void test_11_deplacerPionPionNull(){
        Pion pion = new Pion(new Note(new Lieu("Salle", 3, List.of()), new Temps(1), 2),
                "file:img/pions_personnages/Aventurière.png");
        gestionnairePions.ajouterPion(pion);
        gestionnairePions.deplacerPion(null, new Lieu("Scène", 4, List.of()), new Temps(1), 0, 0);
        assertEquals("Salle", gestionnairePions.getPions().getFirst().getNote().getLieu().getNom());
    }

    // On teste la méthode en déplaçant un pion de manière à avoir un doublon
    @Test
    public void test_12_deplacerPionDoublon(){
        Pion pion1 = new Pion(new Note(new Lieu("Salle", 3, List.of()), new Temps(1), 2),
                "file:img/pions_personnages/Aventurière.png");
        pion1.deplacerPion(0, 0);
        Pion pion2 = new Pion(new Note(new Lieu("Scène", 4, List.of()), new Temps(1), 2),
                "file:img/pions_personnages/Aventurière.png");
        gestionnairePions.ajouterPion(pion1);
        gestionnairePions.ajouterPion(pion2);
        gestionnairePions.deplacerPion(pion1, new Lieu("Scène", 4, List.of()), new Temps(1), 0, 0);
        assertEquals(1, gestionnairePions.getPions().size());
        assertEquals("Scène", gestionnairePions.getPions().getFirst().getNote().getLieu().getNom());
        assertEquals(2, gestionnairePions.getPions().getFirst().getNote().getNbPersonnages());
    }



}
