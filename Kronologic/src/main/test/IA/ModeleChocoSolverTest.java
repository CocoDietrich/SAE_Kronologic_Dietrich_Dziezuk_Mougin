package IA;

import Kronologic.IA.IADeduction.ModeleChocoSolver;
import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Realite;
import Kronologic.Jeu.Elements.Temps;
import org.chocosolver.solver.variables.IntVar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModeleChocoSolverTest {

    private ModeleChocoSolver solver;
    private final String[] personnages = {"A","B","C","D","J","S"};
    private final int[][] sallesAdjacentes = {
            {2, 3},    // Salle 1 est adjacente à 2 et 3
            {1, 3},    // Salle 2 est adjacente à 1 et 3
            {1, 2, 4}, // Salle 3 est adjacente à 1, 2 et 4
            {3, 5, 6}, // Salle 4 est adjacente à 3, 5 et 6
            {4, 6},    // Salle 5 est adjacente à 4 et 6
            {4, 5}     // Salle 6 est adjacente à 4 et 5
    };
    int[][] films = {
            {6, 4, 6, 4, 6, 5}, // Aventurière
            {1, 2, 1, 2, 3, 2}, // Baronne
            {1, 3, 1, 3, 4, 3}, // Chauffeur
            {2, 3, 1, 3, 1, 3}, // Détective
            {4, 3, 2, 3, 4, 5}, // Journaliste
            {5, 4, 3, 1, 2, 1}  // Servante
    };

    @BeforeEach
    public void setUp() {
        ArrayList<Realite> positionsInitiales = new ArrayList<>();
        // Remplissage des positions initiales
        for (int i = 0; i < personnages.length; i++) {
            positionsInitiales.add(new Realite(
                    new Lieu(films[i][0]),
                    new Temps(1),
                    new Personnage(personnages[i])
            ));
        }
        solver = new ModeleChocoSolver(personnages, sallesAdjacentes, positionsInitiales);
    }

    @Test
    public void testJeuInitialDetective(){
        /**
         * D :
         *      - Temps 1 : {2}
         *      - Temps 2 : {1, 3}
         *      - Temps 3 : {1, 2, 3, 4}
         *      - Temps 4 : {1, 2, 3, 4, 5, 6}
         *      - Temps 5 : {1, 2, 3, 4, 5, 6}
         *      - Temps 6 : {1, 2, 3, 4, 5, 6}
         */
        IntVar[] positions = solver.getPositions()[3]; //Detective
        assertEquals("D_T1 = 2", positions[0].toString());
        assertEquals("D_T2 = {1,3}", positions[1].toString());
        assertEquals("D_T3 = {1..4}", positions[2].toString());
        assertEquals("D_T4 = {1..6}", positions[3].toString());
        assertEquals("D_T5 = {1..6}", positions[4].toString());
        assertEquals("D_T6 = {1..6}", positions[5].toString());
    }

    @Test
    public void test3Passages() {
        solver.ajouterContraintePersonnage(new Personnage(personnages[2]), new Lieu(3), 4);
        solver.ajouterContrainteNombreDePassages(new Personnage(personnages[2]), new Lieu(3), 3);

        IntVar[] positions = solver.getPositions()[2]; // Récupérer les positions du Détective
        assertEquals("C_T1 = 1", positions[0].toString());
        assertEquals("C_T2 = 3", positions[1].toString());
        assertEquals("C_T3 = {1..2,4}", positions[2].toString());
        assertEquals("C_T4 = 3", positions[3].toString());
        assertEquals("C_T5 = {1..2,4}", positions[4].toString());
        assertEquals("C_T6 = 3", positions[5].toString());
    }

    @Test
    public void test1Passages(){
        solver.ajouterContraintePersonnage(new Personnage(personnages[3]), new Lieu(1), 3);
        solver.ajouterContrainteNombreDePassages(new Personnage(personnages[3]), new Lieu(6), 1);

        IntVar[] positions = solver.getPositions()[3]; // Récupérer les positions du Détective
        assertEquals("D_T1 = 2", positions[0].toString());
        assertEquals("D_T2 = 3", positions[1].toString());
        assertEquals("D_T3 = 1", positions[2].toString());
        assertEquals("D_T4 = 3", positions[3].toString());
        assertEquals("D_T5 = 4", positions[4].toString());
        assertEquals("D_T6 = 6", positions[5].toString());
    }

    @Test
    public void test2Passages(){
        //Est censé ne rien changer
        solver.ajouterContraintePersonnage(new Personnage(personnages[3]), new Lieu(4), 5);
        solver.ajouterContrainteNombreDePassages(new Personnage(personnages[3]), new Lieu(3), 2);

        IntVar[] positions = solver.getPositions()[3];
        assertEquals("D_T1 = 2", positions[0].toString());
        assertEquals("D_T2 = {1,3}", positions[1].toString());
        assertEquals("D_T3 = {1..2,4}", positions[2].toString());
        assertEquals("D_T4 = {3,5..6}", positions[3].toString());
        assertEquals("D_T5 = 4", positions[4].toString());
        assertEquals("D_T6 = {3,5..6}", positions[5].toString());
    }

    @Test
    public void testContrainteNombreDePassagesAucunPassage() {
        // Ajouter contrainte : Aucun passage dans la salle 3
        solver.ajouterContrainteNombreDePassages(new Personnage(personnages[3]), new Lieu(6), 0);

        IntVar[] positions = solver.getPositions()[3];
        assertEquals("D_T1 = 2", positions[0].toString());
        assertEquals("D_T2 = {1,3}", positions[1].toString());
        assertEquals("D_T3 = {1..4}", positions[2].toString());
        assertEquals("D_T4 = {1..5}", positions[3].toString());
        assertEquals("D_T5 = {1..5}", positions[4].toString());
        assertEquals("D_T6 = {1..5}", positions[5].toString());
    }


    @Test
    public void testContrainteTemps2Personnes() {
        /**
         * J :
         *      - Temps 1 : {4}
         *      - Temps 2 : {3, 5, 6}
         * A :
         *      - Temps 1 : {6}
         *      - Temps 2 : {4, 5}
         */
        solver.ajouterContrainteTemps(new Lieu(5), new Temps(2), 2);

        IntVar[] positions = solver.getPositions()[0];
        IntVar[] positions2 = solver.getPositions()[4];
        assertEquals("A_T2 = 5", positions[1].toString());
        assertEquals("J_T2 = 5", positions2[1].toString());
    }

    @Test
    public void testContrainteTemps1Personne() {
        /**
         * J :
         *      - Temps 1 : {4}
         *      - Temps 2 : {3, 5, 6}
         * S :
         *      - Temps 1 : {5}
         *      - Temps 2 : {4, 6}
         */
        solver.ajouterContrainteTemps(new Lieu(6), new Temps(2), 1);
        IntVar[] positions = solver.getPositions()[4];
        IntVar[] positions2 = solver.getPositions()[5];
        assertEquals("J_T2 = {3,5..6}", positions[1].toString());
        assertEquals("S_T2 = {4,6}", positions2[1].toString());

        solver.ajouterContraintePersonnage(new Personnage(personnages[4]), new Lieu(5), 2);
        positions = solver.getPositions()[4];
        positions2 = solver.getPositions()[5];
        assertEquals("J_T2 = 5", positions[1].toString());
        assertEquals("S_T2 = 6", positions2[1].toString());
    }

    @Test
    public void testContrainteTemps0Personne(){
        /**
         * J :
         *      - Temps 1 : {4}
         *      - Temps 2 : {3, 5, 6}
         * S :
         *      - Temps 1 : {5}
         *      - Temps 2 : {4, 6}
         */
        solver.ajouterContrainteTemps(new Lieu(6), new Temps(2), 0);
        IntVar[] positions = solver.getPositions()[4];
        IntVar[] positions2 = solver.getPositions()[5];
        assertEquals("J_T2 = {3,5}", positions[1].toString());
        assertEquals("S_T2 = 4", positions2[1].toString());
    }

    @Test
    public void testContrainteTemps3Personnes(){
        /**
         * J :
         *      - Temps 1 : {4}
         *      - Temps 2 : {3, 5, 6}
         *      - Temps 3 : {1, 2, 4, 5, 6}
         * A :
         *      - Temps 1 : {6}
         *      - Temps 2 : {4, 5}
         *      - Temps 3 : {3, 4, 5, 6}
         * S :
         *      - Temps 1 : {5}
         *      - Temps 2 : {4, 6}
         *      - Temps 3 : {3, 4, 5, 6}
         */
        solver.ajouterContrainteTemps(new Lieu(6), new Temps(3), 3);
        IntVar[] positions = solver.getPositions()[0];
        IntVar[] positions2 = solver.getPositions()[4];
        IntVar[] positions3 = solver.getPositions()[5];
        assertEquals("A_T3 = 6", positions[2].toString());
        assertEquals("J_T3 = 6", positions2[2].toString());
        assertEquals("S_T3 = 6", positions3[2].toString());
    }
}
