package Kronologic;

import Kronologic.Data.JsonReader;
import Kronologic.IA.IAAssistance.*;
import Kronologic.IA.IADeduction.IADeductionChocoSolver;
import Kronologic.IA.IADeduction.IADeductionHeuristique;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.Jeu.Partie;

import java.util.Arrays;

public class Main {
    public static void main(String[] args){
        Partie partie = JsonReader.lirePartieDepuisJson("data/enquete_base.json");
        assert partie != null;

        //IADeductionChocoSolver iaDeduction = new IADeductionChocoSolver(partie);
        IADeductionHeuristique iaDeduction = new IADeductionHeuristique(partie);

        //IAAssistanceChocoSolver iaAssistance = new IAAssistanceChocoTriche(iaDeduction, partie);
        //IAAssistanceChocoSolver iaAssistance = new IAAssistanceChocoTrichePas(iaDeduction, partie);

        //IAAssistanceHeuristique iaAssistance = new IAAssistanceHeuristiqueTriche(iaDeduction, partie);
        IAAssistanceHeuristique iaAssistance = new IAAssistanceHeuristiqueTrichePas(iaDeduction, partie);

        iaAssistance.setModeRecommandation(2);

        partie.poserQuestionTemps(partie.getElements().getLieux().getFirst(), new Temps(4));

        for (int i = 0; i < 10; i++) {
            iaAssistance.recommanderQuestionOptimale();
        }
    }
}