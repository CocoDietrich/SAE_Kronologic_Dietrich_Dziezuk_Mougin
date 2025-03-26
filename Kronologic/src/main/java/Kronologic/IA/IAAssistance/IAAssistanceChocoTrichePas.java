package Kronologic.IA.IAAssistance;

import Kronologic.IA.IADeduction.IADeductionChocoSolver;
import Kronologic.Jeu.Elements.*;
import Kronologic.Jeu.Partie;

import java.util.*;

public class IAAssistanceChocoTrichePas extends IAAssistanceChocoSolver {
    public IAAssistanceChocoTrichePas(IADeductionChocoSolver deduction, Partie partie) {
        super(deduction, partie);
    }

    @Override
    public String[] recommanderQuestionOptimale() {
        String meilleureQuestion = "Aucune recommandation";
        String meilleureValeur = "Vous avez déjà toutes les informations.";
        double meilleurScore = -1;

        int strategie = (int) (Math.random() * 3);

        for (Lieu lieu : partie.getElements().getLieux()) {
            for (int temps = 2; temps <= 6; temps++) {
                List<Double> reductions = new ArrayList<>();
                for (int infoPublic = 0; infoPublic <= 6; infoPublic++) {
                    for (Personnage p : partie.getElements().getPersonnages()) {
                        double r = simulerTemps(lieu, new Temps(temps), infoPublic, p.getNom());
                        if (r >= 0) reductions.add(r);
                    }
                    double r = simulerTemps(lieu, new Temps(temps), infoPublic, "Rejouer");
                    if (r >= 0) reductions.add(r);
                }
                double score = calculerScore(reductions, strategie);
                if (score > meilleurScore) {
                    meilleurScore = score;
                    meilleureQuestion = "Lieu : " + lieu.getNom();
                    meilleureValeur = "Temps : " + temps;
                }
            }

            for (Personnage perso : partie.getElements().getPersonnages()) {
                List<Double> reductions = new ArrayList<>();
                for (int infoPublic = 0; infoPublic <= 3; infoPublic++) {
                    for (int temps = 1; temps <= 6; temps++) {
                        double r = simulerPersonnage(lieu, perso, infoPublic, temps);
                        if (r >= 0) reductions.add(r);
                    }
                }
                double score = calculerScore(reductions, strategie);
                if (score > meilleurScore) {
                    meilleurScore = score;
                    meilleureQuestion = "Lieu : " + lieu.getNom();
                    meilleureValeur = "Personnage : " + perso.getNom();
                }
            }
        }

        return new String[]{meilleureQuestion, meilleureValeur};
    }

    private double calculerScore(List<Double> reductions, int strategie) {
        if (reductions.isEmpty()) return -1;

        return switch (strategie) {
            case 0 -> reductions.stream().min(Double::compareTo).orElse(-1.0);
            case 1 -> reductions.stream().max(Double::compareTo).orElse(-1.0);
            case 2 -> reductions.stream().mapToDouble(Double::doubleValue).average().orElse(-1.0);
            default -> -1;
        };
    }

    @Override
    public String corrigerDeductions() {
        return "(non implémenté)";
    }
}
