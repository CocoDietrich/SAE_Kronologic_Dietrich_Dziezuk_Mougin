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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPartieDeplacerPion {

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

    // On teste de déplacer un pion dans un lieu et un temps
    @Test
    public void test_01_deplacerPionLieuTemps() {
        Pion pion = new Pion(new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière")),
                "file:img/pions_personnages/Aventurière.png");
        partie.ajouterPion(pion);
        Lieu lieu = new Lieu("Scène", 4, List.of());
        Temps temps = new Temps(2);
        partie.deplacerPion(pion, lieu, temps, 2, 2);

        // On vérifie le pion
        assertEquals(lieu, pion.getNote().getLieu(), "Le pion devrait être déplacé");
        assertEquals(temps, pion.getNote().getTemps(), "Le pion devrait être déplacé");

        // Vérifier note
        assertEquals(new Note(new Lieu("Scène", 4, List.of()), new Temps(2), new Personnage("Aventurière")),
                partie.getGestionnaireNotes().getNotes().getFirst());

        // Vérifier historique
        assertEquals(1, partie.getHistorique().get(0).size());
    }

    // On teste de déplacer un pion dans un lieu (temps identique)
    @Test
    public void test_02_deplacerPionLieu() {
        Pion pion = new Pion(new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière")),
                "file:img/pions_personnages/Aventurière.png");
        partie.ajouterPion(pion);
        Lieu lieu = new Lieu("Scène", 4, List.of());
        Temps temps = new Temps(1);
        partie.deplacerPion(pion, lieu, temps, 2, 2);

        // On vérifie le pion
        assertEquals(lieu, pion.getNote().getLieu(), "Le pion devrait être déplacé");
        assertEquals(temps, pion.getNote().getTemps(), "Le pion devrait être déplacé");

        // Vérifier note
        assertEquals(new Note(new Lieu("Scène", 4, List.of()), new Temps(1), new Personnage("Aventurière")),
                partie.getGestionnaireNotes().getNotes().getFirst());

        // Vérifier historique
        assertEquals(1, partie.getHistorique().get(0).size());
    }

    // On teste de déplacer un pion dans un temps (lieu identique)
    @Test
    public void test_03_deplacerPionTemps() {
        Pion pion = new Pion(new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière")),
                "file:img/pions_personnages/Aventurière.png");
        partie.ajouterPion(pion);
        Lieu lieu = new Lieu("Salle", 3, List.of());
        Temps temps = new Temps(2);
        partie.deplacerPion(pion, lieu, temps, 2, 2);

        // On vérifie le pion
        assertEquals(lieu, pion.getNote().getLieu(), "Le pion devrait être déplacé");
        assertEquals(temps, pion.getNote().getTemps(), "Le pion devrait être déplacé");

        // Vérifier note
        assertEquals(new Note(new Lieu("Salle", 3, List.of()), new Temps(2), new Personnage("Aventurière")),
                partie.getGestionnaireNotes().getNotes().getFirst());

        // Vérifier historique
        assertEquals(1, partie.getHistorique().get(0).size());
    }

    // On teste de déplacer un pion dans un lieu et un temps (pion null)
    @Test
    public void test_04_deplacerPionNull() {
        Lieu lieu1 = new Lieu("Salle", 3, List.of());
        Temps temps1 = new Temps(1);
        Pion pion = new Pion(new Note(lieu1, temps1, new Personnage("Aventurière")),
                "file:img/pions_personnages/Aventurière.png");
        partie.ajouterPion(pion);
        Lieu lieu2 = new Lieu("Scène", 4, List.of());
        Temps temps2 = new Temps(2);

        partie.deplacerPion(null, lieu2, temps2, 2, 2);

        // On vérifie le pion
        assertEquals(lieu1, pion.getNote().getLieu(), "Le pion ne devrait pas être déplacé");
        assertEquals(temps1, pion.getNote().getTemps(), "Le pion ne devrait pas être déplacé");

        // Vérifier note
        assertEquals(new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière")),
                partie.getGestionnaireNotes().getNotes().getFirst());

        // Vérifier historique
        assertEquals(1, partie.getHistorique().get(0).size());
    }

    // On teste de déplacer un pion dans un lieu et un temps (pion non présent)
    @Test
    public void test_05_deplacerPionNonPresent() {
        Lieu lieu1 = new Lieu("Salle", 3, List.of());
        Temps temps1 = new Temps(1);
        Pion pion = new Pion(new Note(lieu1, temps1, new Personnage("Aventurière")),
                "file:img/pions_personnages/Aventurière.png");
        partie.ajouterPion(pion);
        Lieu lieu2 = new Lieu("Scène", 4, List.of());
        Temps temps2 = new Temps(2);
        Pion pion2 = new Pion(new Note(lieu2, temps2, new Personnage("Aventurière")),
                "file:img/pions_personnages/Aventurière.png");

        partie.deplacerPion(pion2, lieu1, temps1, 2, 2);

        // On vérifie le pion
        assertEquals(lieu1, pion.getNote().getLieu(), "Le pion ne devrait pas être déplacé");
        assertEquals(temps1, pion.getNote().getTemps(), "Le pion ne devrait pas être déplacé");

        // Vérifier note
        assertEquals(new Note(new Lieu("Salle", 3, List.of()), new Temps(1), new Personnage("Aventurière")),
                partie.getGestionnaireNotes().getNotes().getFirst());

        // Vérifier historique
        assertEquals(1, partie.getHistorique().get(0).size());
    }
}
