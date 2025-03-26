package IA;

import Kronologic.IA.IADeduction.ModeleHeuristiqueSolver;
import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Realite;
import Kronologic.Jeu.Elements.Temps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ModeleHeuristiqueSolverTest {

    private ModeleHeuristiqueSolver solver;
    private final String[] personnages = {"A","B","C","D","J","S"};
    private final int[][] sallesAdjacentes = {
            {2, 3}, {1, 3}, {1, 2, 4}, {3, 5, 6}, {4, 6}, {4, 5}
    };
    int[][] films = {
            {6, 4, 6, 4, 6, 5},
            {1, 2, 1, 2, 3, 2},
            {1, 3, 1, 3, 4, 3},
            {2, 3, 1, 3, 1, 3},
            {4, 3, 2, 3, 4, 5},
            {5, 4, 3, 1, 2, 1}
    };

    @BeforeEach
    public void setUp() {
        ArrayList<Realite> positionsInitiales = new ArrayList<>();
        for (int i = 0; i < personnages.length; i++) {
            positionsInitiales.add(new Realite(
                    new Lieu(films[i][0]),
                    new Temps(1),
                    new Personnage(personnages[i])
            ));
        }
        solver = new ModeleHeuristiqueSolver(personnages, sallesAdjacentes, positionsInitiales);
    }

    @Test
    public void testJeuInitialDetective(){
        boolean[][][] domaines = solver.getDomainesPersonnages();
        int indexD = 3;
        assertTrue(domaines[0][indexD][1]);
        assertTrue(domaines[1][indexD][0]);
        assertTrue(domaines[1][indexD][2]);
        assertTrue(domaines[2][indexD][0]);
        assertTrue(domaines[2][indexD][1]);
        assertTrue(domaines[2][indexD][2]);
        assertTrue(domaines[2][indexD][3]);
    }

    @Test
    public void test3Passages() {
        solver.ajouterContraintePersonnage(new Personnage("C"), new Lieu(3), 1);
        solver.ajouterContrainteNombreDePassages(new Personnage("C"), new Lieu(3), 3);
    }

    @Test
    public void test1Passage(){
        solver.ajouterContraintePersonnage(new Personnage("D"), new Lieu(1), 3);
        solver.ajouterContrainteNombreDePassages(new Personnage("D"), new Lieu(6), 1);
    }

    @Test
    public void test2Passages(){
        solver.ajouterContraintePersonnage(new Personnage("D"), new Lieu(4), 5);
        solver.ajouterContrainteNombreDePassages(new Personnage("D"), new Lieu(3), 2);
    }

    @Test
    public void testContrainteNombreDePassagesAucunPassage() {
        solver.ajouterContrainteNombreDePassages(new Personnage("D"), new Lieu(6), 0);
    }

    @Test
    public void testContrainteTemps2Personnes() {
        solver.ajouterContrainteTemps(new Lieu(5), new Temps(2), 2);
    }

    @Test
    public void testContrainteTemps1Personne() {
        solver.ajouterContrainteTemps(new Lieu(6), new Temps(2), 1);
        solver.ajouterContraintePersonnage(new Personnage("J"), new Lieu(5), 2);
    }

    @Test
    public void testContrainteTemps0Personne(){
        solver.ajouterContrainteTemps(new Lieu(6), new Temps(2), 0);
    }

    @Test
    public void testContrainteTemps3Personnes(){
        solver.ajouterContrainteTemps(new Lieu(6), new Temps(3), 3);
    }

    @Test
    public void testReductionGrandEscalierTemps4Servante() {
        solver.ajouterContrainteTemps(new Lieu(4), new Temps(4), 1);
        solver.ajouterContraintePersonnage(new Personnage("S"), new Lieu(1), 4);

        boolean[] domaineServanteT2 = solver.getDomainesPersonnages()[1][5];
        boolean contient3 = domaineServanteT2[2]; // index 2 = lieu 3
        assertFalse(contient3, "La salle 3 ne devrait plus être dans le domaine à T=2 pour S");
    }
}