package Jeu.Partie.Pion;

import Kronologic.Jeu.Deroulement;
import Kronologic.Jeu.Elements.*;
import Kronologic.Jeu.Enquete;
import Kronologic.Jeu.Indice.GestionnaireIndices;
import Kronologic.Jeu.Indice.IndicePersonnage;
import Kronologic.Jeu.Partie;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import kronologic.outils.InitialiseurJavaFX;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestPartieAjouterPion {

    private Partie partie;
    private Enquete enquete;
    private Deroulement deroulement;
    private GestionnaireIndices gestionnaireIndices;
    private GestionnaireNotes gestionnaireNotes;
    private GestionnairePions gestionnairePions;
    private Elements elements;

    @BeforeEach
    public void init() {
        // Création des éléments
        Personnage baronne = new Personnage("Baronne");
        Personnage aventuriere = new Personnage("Aventurière");
        Lieu scene = new Lieu("Scène", 4, List.of());
        Lieu salle = new Lieu("Salle", 3, List.of());

        // Création des éléments de déroulement
        Realite r1 = new Realite(scene, new Temps(1), baronne);
        Realite r2 = new Realite(salle, new Temps(2), baronne);
        Realite r3 = new Realite(scene, new Temps(1), aventuriere);
        Realite r4 = new Realite(salle, new Temps(2), aventuriere);
        ArrayList<Realite> listePositions = new ArrayList<>();
        listePositions.add(r1);
        listePositions.add(r2);
        listePositions.add(r3);
        listePositions.add(r4);

        // Création des indices
        IndicePersonnage i1 = new IndicePersonnage(scene, 1, baronne, 1);
        IndicePersonnage i2 = new IndicePersonnage(salle, 1, baronne, 2);
        IndicePersonnage i3 = new IndicePersonnage(scene, 1, aventuriere, 1);
        IndicePersonnage i4 = new IndicePersonnage(salle, 1, aventuriere, 2);

        // Création de l'enquête
        enquete = new Enquete(1,
                "Enquete 1",
                "Synopsis 1",
                "Enigme 1",
                5,
                10,
                baronne,
                salle,
                new Temps(2));

        // Création du déroulement
        deroulement = new Deroulement(listePositions);

        // Création du gestionnaire d'indices
        gestionnaireIndices = new GestionnaireIndices(List.of(i1, i2, i3, i4));

        // Création du gestionnaire de notes
        gestionnaireNotes = new GestionnaireNotes();

        // Création du gestionnaire de pions
        gestionnairePions = new GestionnairePions();

        // Création des éléments
        elements = new Elements(List.of(baronne, aventuriere), List.of(scene, salle));

        partie = new Partie(enquete, deroulement, gestionnaireIndices, gestionnaireNotes, gestionnairePions, elements);
    }

    @BeforeAll
    public static void setUp() {
        InitialiseurJavaFX.initToolkit();
    }

    // On teste l'ajout d'un pion
    @Test
    public void test_01_ajoutPion() {
        Note note = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière"));
        Pion pion = new Pion(
                note,
                "file:img/pions_personnages/Aventurière.png");
        partie.ajouterPion(pion);

        // Vérifier pion
        assertTrue(partie.getGestionnairePions().getPions().contains(pion), "Le pion devrait être ajouté");

        // Vérifier note
        assertEquals(note, partie.getGestionnaireNotes().getNotes().getFirst());

        // Vérifier historique
        assertEquals(1, partie.getHistorique().get(0).size());
    }

    // On teste l'ajout de plusieurs pions
    @Test
    public void test_02_ajoutPions() {
        Note note1 = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière"));
        Note note2 = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Baronne"));
        Pion pion1 = new Pion(
                note1,
                "file:img/pions_personnages/Aventurière.png");
        Pion pion2 = new Pion(
                note2,
                "file:img/pions_personnages/Baronne.png");
        partie.ajouterPion(pion1);
        partie.ajouterPion(pion2);

        // Vérifier pion 1
        assertTrue(partie.getGestionnairePions().getPions().contains(pion1), "Le pion 1 devrait être ajouté");

        // Vérifier pion 2
        assertTrue(partie.getGestionnairePions().getPions().contains(pion2), "Le pion 2 devrait être ajouté");

        // Vérifier note 1
        assertEquals(note1, partie.getGestionnaireNotes().getNotes().getFirst());

        // Vérifier note 2
        assertEquals(note2, partie.getGestionnaireNotes().getNotes().getLast());

        // Vérifier historique
        assertEquals(2, partie.getHistorique().get(0).size());
    }

    // On teste l'ajout de le même pion en double
    @Test
    public void test_03_ajoutPionDouble() {
        Note note = new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière"));
        Pion pion = new Pion(
                note,
                "file:img/pions_personnages/Aventurière.png");
        partie.ajouterPion(pion);
        partie.ajouterPion(pion);

        // Vérifier pion
        assertTrue(partie.getGestionnairePions().getPions().contains(pion), "Le pion ne devrait pas être ajouté en double");

        // Vérifier note
        assertEquals(note, partie.getGestionnaireNotes().getNotes().getFirst());

        // Vérifier historique
        assertEquals(1, partie.getHistorique().get(0).size());
    }

    // On teste l'ajout d'un pion null
    @Test
    public void test_04_ajoutPionNull() {
        assertThrows(NullPointerException.class, () -> partie.ajouterPion(null));
    }
}
