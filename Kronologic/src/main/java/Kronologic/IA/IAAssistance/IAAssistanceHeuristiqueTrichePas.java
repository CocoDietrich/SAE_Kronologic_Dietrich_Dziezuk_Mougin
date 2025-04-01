package Kronologic.IA.IAAssistance;

import Kronologic.IA.IADeduction.IADeductionHeuristique;
import Kronologic.Jeu.Elements.*;
import Kronologic.Jeu.Partie;

import java.util.ArrayList;
import java.util.List;

public class IAAssistanceHeuristiqueTrichePas extends IAAssistanceHeuristique {

    public IAAssistanceHeuristiqueTrichePas(IADeductionHeuristique deduction, Partie partie) {
        super(deduction, partie);
    }

    /**
     * Recommande la question optimale à poser au joueur pour l'IA qui ne triche pas.
     *
     * @return Un tableau contenant le lieu et le type de question recommandée.
     */
    @Override
    public String[] recommanderQuestionOptimale() {
        String meilleureQuestion = "Aucune recommandation";
        String meilleureValeur = "Vous avez déjà toutes les informations.";
        double meilleurScore = -1;

        int strategie = (int) (Math.random() * 3); // 0 = min, 1 = max, 2 = moyenne

        for (Lieu lieu : partie.getElements().lieux()) {
            for (int temps = 2; temps <= 6; temps++) {
                List<Double> reductions = new ArrayList<>();
                for (int infoPublic = 0; infoPublic <= 6; infoPublic++) {
                    for (Personnage p : partie.getElements().personnages()) {
                        int r = simulerTemps(lieu, new Temps(temps), infoPublic, p.getNom());
                        if (r >= 0) reductions.add((double) r);
                    }
                    int r = simulerTemps(lieu, new Temps(temps), infoPublic, "Rejouer");
                    if (r >= 0) reductions.add((double) r);
                }
                double score = calculerScore(reductions, strategie);
                if (score > meilleurScore) {
                    meilleurScore = score;
                    meilleureQuestion = "Lieu : " + lieu.getNom();
                    meilleureValeur = "Temps : " + temps;
                }
            }

            for (Personnage perso : partie.getElements().personnages()) {
                List<Double> reductions = new ArrayList<>();
                for (int infoPublic = 0; infoPublic <= 3; infoPublic++) {
                    for (int infoPrive = 0; infoPrive <= 6; infoPrive++) {
                        int r = simulerPersonnage(perso, lieu, infoPublic, infoPrive);
                        if (r >= 0) reductions.add((double) r);
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

    /**
     * Calcule le score basé sur la stratégie choisie.
     *
     * @param reductions La liste des réductions.
     * @param strategie  La stratégie à utiliser (0 = min, 1 = max, 2 = moyenne).
     * @return Le score calculé.
     */
    private double calculerScore(List<Double> reductions, int strategie) {
        if (reductions.isEmpty()) return -1;

        return switch (strategie) {
            case 0 -> reductions.stream().min(Double::compareTo).orElse(-1.0);
            case 1 -> reductions.stream().max(Double::compareTo).orElse(-1.0);
            case 2 -> reductions.stream().mapToDouble(Double::doubleValue).average().orElse(-1.0);
            default -> -1;
        };
    }

}
