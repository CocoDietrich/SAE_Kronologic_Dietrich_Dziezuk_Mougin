package Jeu.Partie;

import Kronologic.Jeu.Deroulement;
import Kronologic.Jeu.Elements.*;
import Kronologic.Jeu.Enquete;
import Kronologic.Jeu.Indice.GestionnaireIndices;
import Kronologic.Jeu.Indice.IndicePersonnage;
import Kronologic.Jeu.Indice.IndiceTemps;
import Kronologic.Jeu.Partie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestPartiePoserQuestionPersonnage {

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

        partie = new Partie(enquete, deroulement, gestionnaireIndices, gestionnaireNotes, gestionnairePions, elements);
    }

    // Test de la méthode poserQuestionPersonnage
    @Test
    public void test_01_poserQuestionPersonnage() {
        Lieu scene = elements.lieux().getFirst();
        Personnage baronne = elements.personnages().getFirst();
        partie.poserQuestionPersonnage(scene, baronne);
        assertEquals(1, partie.getIndicesDecouverts().size());
        assertEquals(1, partie.getNbQuestion());
        assertInstanceOf(IndicePersonnage.class, partie.getIndicesDecouverts().getFirst());
    }

    // Test de la méthode poserQuestionPersonnage avec un lieu null
    @Test
    public void test_02_poserQuestionPersonnageLieuNull() {
        Personnage baronne = elements.personnages().getFirst();
        assertThrows(NullPointerException.class, () -> partie.poserQuestionPersonnage(null, baronne));
    }

    // Test de la méthode poserQuestionPersonnage avec un personnage null
    @Test
    public void test_03_poserQuestionPersonnagePersonnageNull() {
        Lieu scene = elements.lieux().getFirst();
        assertThrows(NullPointerException.class, () -> partie.poserQuestionPersonnage(scene, null));
    }

    // Test de la méthode poserQuestionPersonnage avec un lieu et un personnage null
    @Test
    public void test_04_poserQuestionPersonnageLieuPersonnageNull() {
        assertThrows(NullPointerException.class, () -> partie.poserQuestionPersonnage(null, null));
    }

    // Test de la méthode poserQuestionPersonnage avec des indices qui ne sont pas des indices de personnage
    @Test
    public void test_05_poserQuestionPersonnageIndiceTemps() {
        IndiceTemps i1 = new IndiceTemps(elements.lieux().getFirst(), 2, new Temps(1), "Baronne");
        IndiceTemps i2 = new IndiceTemps(elements.lieux().getLast(), 2, new Temps(2), "Aventurière");
        IndiceTemps i3 = new IndiceTemps(elements.lieux().getFirst(), 0, new Temps(1), "Rejouer");
        IndiceTemps i4 = new IndiceTemps(elements.lieux().getLast(), 0, new Temps(2), "Rejouer");
        partie = new Partie(enquete, deroulement, new GestionnaireIndices(List.of(i1, i2, i3, i4)), gestionnaireNotes, gestionnairePions, elements);

        assertNull(partie.poserQuestionPersonnage(elements.lieux().getFirst(), elements.personnages().getFirst()));
    }

}
