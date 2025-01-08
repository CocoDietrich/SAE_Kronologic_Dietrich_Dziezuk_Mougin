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

public class TestPartieMAJHistorique {

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

    // Test de la méthode mettreAJourHistorique
    @Test
    public void test_01_majHistorique() {
        // Création d'une note
        Note n = new Note(new Lieu("Salle", 3, List.of()), new Temps(2), new Personnage("Baronne"));

        // Ajout de la note
        partie.ajouterNote(n);

        // Vérification de la mise à jour de l'historique
        assertEquals(1, partie.getHistorique().size());
        assertEquals(n, partie.getHistorique().get(0).getFirst());
    }

    // Test de la méthode mettreAJourHistorique avec une note null
    @Test
    public void test_02_majHistoriqueNoteNull() {
        // Création d'une note
        Note n = new Note(new Lieu("Salle", 3, List.of()), new Temps(2), new Personnage("Baronne"));

        // Ajout de la note
        partie.ajouterNote(n);

        // Vérification de la mise à jour de l'historique
        assertEquals(1, partie.getHistorique().size());
        assertEquals(n, partie.getHistorique().get(0).getFirst());

        // Ajout d'une note null
        partie.ajouterNote(null);

        // Vérification de la mise à jour de l'historique
        assertEquals(1, partie.getHistorique().size());
        assertEquals(n, partie.getHistorique().get(0).getFirst());
    }

    // Test de la méthode mettreAJourHistorique avec une note identique
    @Test
    public void test_03_majHistoriqueNoteIdentique() {
        // Création d'une note
        Note n = new Note(new Lieu("Salle", 3, List.of()), new Temps(2), new Personnage("Baronne"));

        // Ajout de la note
        partie.ajouterNote(n);

        // Vérification de la mise à jour de l'historique
        assertEquals(1, partie.getHistorique().size());
        assertEquals(n, partie.getHistorique().get(0).getFirst());

        // Ajout de la même note
        partie.ajouterNote(n);

        // Vérification de la mise à jour de l'historique
        assertEquals(1, partie.getHistorique().size());
        assertEquals(n, partie.getHistorique().get(0).getFirst());
    }

    // Test de la méthode mettreAJourHistorique avec une question posée
    @Test
    public void test_04_majHistoriqueQuestion() {
        // Création d'une note
        Note n = new Note(new Lieu("Salle", 3, List.of()), new Temps(2), new Personnage("Baronne"));

        // Ajout de la note
        partie.ajouterNote(n);

        // Vérification de la mise à jour de l'historique
        assertEquals(1, partie.getHistorique().size());
        assertEquals(n, partie.getHistorique().get(0).getFirst());

        partie.poserQuestionTemps(new Lieu("Salle", 3, List.of()), new Temps(2));

        // Création d'une deuxième note
        Note n2 = new Note(new Lieu("Scène", 4, List.of()), new Temps(2), new Personnage("Aventurière"));

        // Ajout de la note
        partie.ajouterNote(n2);

        System.out.println(partie.getHistorique());

        // Vérification de la mise à jour de l'historique
        assertEquals(2, partie.getHistorique().size());
        assertEquals(n2, partie.getHistorique().get(1).getLast());
    }

}
