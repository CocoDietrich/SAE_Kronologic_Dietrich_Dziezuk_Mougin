package Kronologic.IA.IAAssistance;

import Kronologic.IA.IADeduction.IADeductionHeuristique;
import Kronologic.Jeu.Elements.*;
import Kronologic.Jeu.Partie;

public class IAAssistanceHeuristiqueTrichePas extends IAAssistanceHeuristique {
    public IAAssistanceHeuristiqueTrichePas(IADeductionHeuristique deduction, Partie partie) {
        super(deduction, partie);
    }

    @Override
    public String[] recommanderQuestionOptimale() {
        int strategie = (int) (Math.random() * 3);
        return switch (strategie) {
            case 0 -> recommanderMin();
            case 1 -> recommanderMax();
            case 2 -> recommanderMoyenne();
            default -> new String[]{"Aucune recommandation", "Vous avez déjà toutes les informations."};
        };
    }

    private String[] recommanderMin() {
        return recommanderParStrategie(Integer::min, Integer.MAX_VALUE);
    }

    private String[] recommanderMax() {
        return recommanderParStrategie(Integer::max, Integer.MIN_VALUE);
    }

    private String[] recommanderMoyenne() {
        // stratégie moyenne : à implémenter si tu veux
        return new String[]{"Non implémenté", "Stratégie moyenne"};
    }

    private String[] recommanderParStrategie(java.util.function.IntBinaryOperator op, int initial) {
        String meilleureQuestion = "Aucune recommandation";
        String meilleureValeur = "Vous avez déjà toutes les informations.";
        int bestScore = initial;

        for (Lieu lieu : partie.getElements().getLieux()) {
            for (int temps = 2; temps <= 6; temps++) {
                for (int infoPublic = 0; infoPublic <= 6; infoPublic++) {
                    for (String infoPrive : new String[]{"A", "B", "C", "D", "J", "S", "Rejouer"}) {
                        int score = simulerTemps(lieu, new Temps(temps), infoPublic, infoPrive);
                        if ((op.applyAsInt(score, bestScore) == score)) {
                            bestScore = score;
                            meilleureQuestion = "Lieu : " + lieu.getNom();
                            meilleureValeur = "Temps : " + temps;
                        }
                    }
                }
            }

            for (Personnage personnage : partie.getElements().getPersonnages()) {
                for (int infoPublic = 0; infoPublic <= 3; infoPublic++) {
                    for (int infoPrive = 0; infoPrive <= 6; infoPrive++) {
                        int score = simulerPersonnage(personnage, lieu, infoPublic, infoPrive);
                        if ((op.applyAsInt(score, bestScore) == score)) {
                            bestScore = score;
                            meilleureQuestion = "Lieu : " + lieu.getNom();
                            meilleureValeur = "Personnage : " + personnage.getNom();
                        }
                    }
                }
            }
        }
        return new String[]{meilleureQuestion, meilleureValeur};
    }
}
