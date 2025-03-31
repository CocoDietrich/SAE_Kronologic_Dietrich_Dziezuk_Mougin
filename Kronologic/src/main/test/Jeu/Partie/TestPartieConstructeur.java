package Jeu.Partie;

import Kronologic.Jeu.Deroulement;
import Kronologic.Jeu.Elements.*;
import Kronologic.Jeu.Enquete;
import Kronologic.Jeu.Indice.GestionnaireIndices;
import Kronologic.Jeu.Indice.IndicePersonnage;
import Kronologic.Jeu.Partie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestPartieConstructeur {

    private Partie partie;
    private Enquete enquete;
    private Deroulement deroulement;
    private GestionnaireIndices gestionnaireIndices;
    private GestionnaireNotes gestionnaireNotes;
    private GestionnairePions gestionnairePions;
    private Elements elements;

    @BeforeEach
    public void setUp() {
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
    }

    // On teste le constructeur de la classe Partie
    @Test
    public void test_01_PartieConstructeur() {
        // Création de la partie
        partie = new Partie(enquete, deroulement, gestionnaireIndices, gestionnaireNotes, gestionnairePions, elements);
        assertEquals(0, partie.getNbQuestion());
        assertEquals(0, partie.getIndicesDecouverts().size());
        assertEquals("Enquete 1", partie.getEnquete().getNomEnquete());
        assertEquals("Synopsis 1", partie.getEnquete().getSynopsis());
        assertEquals("Enigme 1", partie.getEnquete().getEnigme());
        assertEquals(5, partie.getEnquete().getLoupeOr());
        assertEquals(10, partie.getEnquete().getLoupeBronze());
        assertEquals("Baronne", partie.getEnquete().getMeurtrier().getNom());
        assertEquals("Salle", partie.getEnquete().getLieuMeurtre().getNom());
        assertEquals(2, partie.getEnquete().getTempsMeurtre().getValeur());
        assertEquals(4, partie.getDeroulement().getListePositions().size());
        assertEquals(4, partie.getGestionnaireIndices().getListeIndices().size());
        assertEquals(0, partie.getGestionnaireNotes().getNotes().size());
        assertEquals(0, partie.getGestionnairePions().getPions().size());
        assertEquals(2, partie.getElements().personnages().size());
        assertEquals(2, partie.getElements().lieux().size());
    }

    // On teste le constructeur de la classe Partie avec une enquete null
    @Test
    public void test_02_PartieConstructeurEnqueteNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            partie = new Partie(null, deroulement, gestionnaireIndices, gestionnaireNotes, gestionnairePions, elements);
        });
    }

    // On teste le constructeur de la classe Partie avec un déroulement null
    @Test
    public void test_03_PartieConstructeurDeroulementNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            partie = new Partie(enquete, null, gestionnaireIndices, gestionnaireNotes, gestionnairePions, elements);
        });
    }

    // On teste le constructeur de la classe Partie avec un gestionnaire d'indices null
    @Test
    public void test_04_PartieConstructeurGestionnaireIndicesNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            partie = new Partie(enquete, deroulement, null, gestionnaireNotes, gestionnairePions, elements);
        });
    }

    // On teste le constructeur de la classe Partie avec un gestionnaire de notes null
    @Test
    public void test_05_PartieConstructeurGestionnaireNotesNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            partie = new Partie(enquete, deroulement, gestionnaireIndices, null, gestionnairePions, elements);
        });
    }

    // On teste le constructeur de la classe Partie avec un gestionnaire de pions null
    @Test
    public void test_06_PartieConstructeurGestionnairePionsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            partie = new Partie(enquete, deroulement, gestionnaireIndices, gestionnaireNotes, null, elements);
        });
    }

    // On teste le constructeur de la classe Partie avec des éléments null
    @Test
    public void test_07_PartieConstructeurElementsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            partie = new Partie(enquete, deroulement, gestionnaireIndices, gestionnaireNotes, gestionnairePions, null);
        });
    }

    // On teste le constructeur de la classe Partie avec tous les attributs null
    @Test
    public void test_08_PartieConstructeurToutNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            partie = new Partie(null, null, null, null, null, null);
        });
    }
}
