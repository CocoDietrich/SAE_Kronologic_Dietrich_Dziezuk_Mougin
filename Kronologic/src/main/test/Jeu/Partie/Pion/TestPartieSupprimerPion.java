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

public class TestPartieSupprimerPion {

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

    // On teste la suppression d'un pion
    @Test
    public void test_01_supprimerPion() {
        Pion pion = new Pion(new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière")),
                "file:img/pions_personnages/Aventurière.png");
        partie.ajouterPion(pion);
        partie.supprimerPion(pion);

        // On vérifie que le pion n'est plus dans la liste des pions
        assertFalse(partie.getGestionnairePions().getPions().contains(pion), "Le pion devrait être supprimé");

        // On vérifie que la note n'est plus dans la liste des notes
        assertFalse(partie.getGestionnaireNotes().getNotes().contains(pion.getNote()), "La note devrait être supprimée");

        // On vérifie que la note n'est plus dans l'historique
        assertFalse(partie.getHistorique().containsValue(List.of(pion.getNote())),
                "La note devrait être supprimée de l'historique");
    }

    // On teste la suppression d'un pion null
    @Test
    public void test_02_supprimerPionNull() {
        Pion pion = new Pion(new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière")),
                "file:img/pions_personnages/Aventurière.png");
        partie.ajouterPion(pion);
        partie.supprimerPion(null);
        // On vérifie que le pion n'est plus dans la liste des pions
        assertTrue(partie.getGestionnairePions().getPions().contains(pion), "Le pion devrait être conservé");
        assertFalse(partie.getGestionnairePions().getPions().contains(null), "Le pion ne devrait pas être supprimé");

        // On vérifie que la note n'est plus dans la liste des notes
        assertTrue(partie.getGestionnaireNotes().getNotes().contains(pion.getNote()), "La note devrait être conservée");
        assertFalse(partie.getGestionnaireNotes().getNotes().contains(null), "La note ne devrait pas être supprimée");

        // On vérifie que la note n'est plus dans l'historique
        assertEquals(1, partie.getHistorique().size(), "L'historique devrait contenir une seule note");
        assertTrue(partie.getHistorique().containsValue(List.of(pion.getNote())),
                "La note devrait être conservée dans l'historique");
    }

    // On teste la suppression d'un pion non présent
    @Test
    public void test_03_supprimerPionNonPresent() {
        Pion pion = new Pion(new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière")),
                "file:img/pions_personnages/Aventurière.png");
        Pion pion2 = new Pion(new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Baronne")),
                "file:img/pions_personnages/Baronne.png");
        partie.ajouterPion(pion);
        partie.supprimerPion(pion2);

        // On vérifie que le pion n'est plus dans la liste des pions
        assertTrue(partie.getGestionnairePions().getPions().contains(pion), "Le pion devrait être conservé");
        assertFalse(partie.getGestionnairePions().getPions().contains(pion2), "Le pion ne devrait pas être supprimé");

        // On vérifie que la note n'est plus dans la liste des notes
        assertTrue(partie.getGestionnaireNotes().getNotes().contains(pion.getNote()), "La note devrait être conservée");
        assertFalse(partie.getGestionnaireNotes().getNotes().contains(pion2.getNote()), "La note ne devrait pas être supprimée");

        // On vérifie que la note n'est plus dans l'historique
        assertTrue(partie.getHistorique().containsValue(List.of(pion.getNote())),
                "La note devrait être conservée dans l'historique");
        assertFalse(partie.getHistorique().containsValue(List.of(pion2.getNote())),
                "La note ne devrait pas être supprimée de l'historique");
    }

    // On teste la suppression d'un pion dans une partie sans pion
    @Test
    public void test_04_supprimerPionSansPion() {
        Pion pion = new Pion(new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière")),
                "file:img/pions_personnages/Aventurière.png");
        partie.supprimerPion(pion);

        // On vérifie que le pion n'est pas dans la liste des pions
        assertFalse(partie.getGestionnairePions().getPions().contains(pion), "Le pion ne devrait pas être ajouté");

        // On vérifie que la note n'est pas dans la liste des notes
        assertFalse(partie.getGestionnaireNotes().getNotes().contains(pion.getNote()), "La note ne devrait pas être ajoutée");

        // On vérifie que la note n'est pas dans l'historique
        assertFalse(partie.getHistorique().containsValue(List.of(pion.getNote())),
                "La note ne devrait pas être ajoutée à l'historique");
    }
}
