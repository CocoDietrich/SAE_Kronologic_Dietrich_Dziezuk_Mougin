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

public class TestPartiePoserQuestionTemps {

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
        IndiceTemps i1 = new IndiceTemps(scene, 2, new Temps(1), "Baronne");
        IndiceTemps i2 = new IndiceTemps(salle, 2, new Temps(2), "Aventurière");
        IndiceTemps i3 = new IndiceTemps(scene, 0, new Temps(1), "Rejouer");
        IndiceTemps i4 = new IndiceTemps(salle, 0, new Temps(2), "Rejouer");

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

    // Test de la méthode poserQuestionTemps
    @Test
    public void test_01_poserQuestionTemps() {
        Lieu scene = elements.lieux().getFirst();
        Temps temps = new Temps(1);
        partie.poserQuestionTemps(scene, temps);
        assertEquals(1, partie.getIndicesDecouverts().size());
        assertEquals(1, partie.getNbQuestion());
        assertInstanceOf(IndiceTemps.class, partie.getIndicesDecouverts().getFirst());
    }

    // Test de la méthode poserQuestionTemps avec un lieu null
    @Test
    public void test_02_poserQuestionTempsLieuNull() {
        Temps temps = new Temps(1);
        assertThrows(NullPointerException.class, () -> partie.poserQuestionTemps(null, temps));
    }

    // Test de la méthode poserQuestionTemps avec un temps null
    @Test
    public void test_03_poserQuestionTempsPersonnageNull() {
        Lieu scene = elements.lieux().getFirst();
        assertThrows(NullPointerException.class, () -> partie.poserQuestionTemps(scene, null));
    }

    // Test de la méthode poserQuestionTemps avec un lieu et un temps null
    @Test
    public void test_04_poserQuestionPersonnageLieuPersonnageNull() {
        assertThrows(NullPointerException.class, () -> partie.poserQuestionTemps(null, null));
    }

    // Test de la méthode poserQuestionTemps avec des indices qui ne sont pas des indices de temps
    @Test
    public void test_05_poserQuestionTempsIndicePersonnage() {
        IndicePersonnage i1 = new IndicePersonnage(elements.lieux().getFirst(), 1, elements.personnages().getFirst(), 1);
        IndicePersonnage i2 = new IndicePersonnage(elements.lieux().getLast(), 1, elements.personnages().getFirst(), 2);
        IndicePersonnage i3 = new IndicePersonnage(elements.lieux().getFirst(), 1, elements.personnages().getLast(), 1);
        IndicePersonnage i4 = new IndicePersonnage(elements.lieux().getLast(), 1, elements.personnages().getLast(), 2);
        partie = new Partie(enquete, deroulement, new GestionnaireIndices(List.of(i1, i2, i3, i4)), gestionnaireNotes, gestionnairePions, elements);

        assertNull(partie.poserQuestionTemps(elements.lieux().getFirst(), new Temps(1)));
    }

}
