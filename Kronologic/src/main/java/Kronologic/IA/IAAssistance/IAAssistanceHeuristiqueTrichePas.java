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
        int meilleurScore;

        int strategie = 1; // 0 = min, 1 = max, 2 = moyenne

        String[] resultats = new String[3];

        switch (strategie) {
            case 0 -> {
                meilleurScore = Integer.MAX_VALUE;
                resultats = recommanderQuestionMin(meilleurScore);
            }
            case 1 -> {
                meilleurScore = Integer.MIN_VALUE;
                resultats = recommanderQuestionMax(meilleurScore);
            }
            case 2 -> {
                meilleurScore = Integer.MIN_VALUE;
                resultats = recommanderQuestionMoyenne(meilleurScore);
            }
        }

        return resultats;
    }

    // Max :
    // Prend le max des possibilités d'indices pour chaque question
    // Prend le max des max des possibilités d'indices récupérées
    public String[] recommanderQuestionMax(int meilleurScore) {
        String meilleureQuestion = "Aucune recommandation";
        String meilleureValeur = "Vous avez déjà toutes les informations.";
        String[] resultats = new String[3];

        // On récupère tous les lieux
        for (Lieu lieu : partie.getElements().getLieux()) {
            double ScoreTemps = Double.MIN_VALUE;
            // On teste les questions par rapport aux temps
            // Questions Temps Lieu à poser = 30 (5*6) :
            // Temps : 2/3/4/5/6
            // Lieu : 1/2/3/4/5/6
            for (int temps = 2; temps <= 6; temps++) {
                // On teste avec tous les indices publics
                // -----------------------------
                // Questions Temps Lieu = 49 possibilités d'indices (7*7) :
                // Indice public (Nombre de personnages) : 0/1/2/3/4/5/6
                // Indice privé (Temps auquel il est passé) : A/B/C/D/J/S/Rejouer
                for (int indicePublic = 0; indicePublic <= 6; indicePublic++) {
                    // On teste avec tous les indices privés
                    for (String indicePrive : new String[]{"A", "B", "C", "D", "J", "S", "Rejouer"}) {
                        double score = simulerTemps(lieu, new Temps(temps), indicePublic, indicePrive);
                        if (score > ScoreTemps) {
                            ScoreTemps = score;
                        }
                        System.out.println("Score pour " + temps + " à " + lieu.getNom() + " : " + score);
                    }
                }
                // On a fini de tester tous les indices
                if (ScoreTemps > meilleurScore) {
                    meilleureQuestion = "Temps : " + temps;
                    meilleureValeur = "Lieu : " + lieu.getNom();
                    meilleurScore = (int) ScoreTemps;
                }
            }

            double ScorePersonnage = Double.MIN_VALUE;
            // On teste les questions par rapport aux personnages
            // Questions Personnages Lieu à poser = 36 (6*6) :
            // Personnages : A/B/C/D/J/S
            // Lieu : 1/2/3/4/5/6
            for (Personnage personnage : partie.getElements().getPersonnages()) {
                // On teste avec tous les indices publics
                // -----------------------------
                // Questions Personnages Lieu = 28 possibilités d'indices (4*7) :
                // Indice public (Nombre de passages) : 0/1/2/3
                // Indice privé (Temps auquel il est passé) : 0/1/2/3/4/5/6
                for (int indicePublic = 0; indicePublic <= 3; indicePublic++) {
                    // On teste avec tous les indices privés
                    for (int indicePrive = 0; indicePrive <= 6; indicePrive++) {
                        double score = simulerPersonnage(personnage, lieu, indicePublic, indicePrive);
                        if (score > ScorePersonnage) {
                            ScorePersonnage = score;
                        }
                    }
                    System.out.println("Score pour " + personnage.getNom() + " à " + lieu.getNom() + " : " + ScorePersonnage);
                }
                // On a fini de tester tous les indices
                if (ScorePersonnage > meilleurScore) {
                    meilleureQuestion = "Personnage : " + personnage.getNom();
                    meilleureValeur = "Lieu : " + lieu.getNom();
                    meilleurScore = (int) ScorePersonnage;
                }
            }

        }

        resultats[0] = meilleureQuestion;
        resultats[1] = meilleureValeur;
        resultats[2] = String.valueOf(meilleurScore);

        return resultats;
    }

    // Min :
    // Prend le min des possibilités d'indices pour chaque question
    // Prend le max des min des possibilités d'indices récupérées
    public String[] recommanderQuestionMin(int meilleurScore) {
        String[] resultats = new String[3];
        String meilleureQuestion = "Aucune recommandation";
        String meilleureValeur = "Vous avez déjà toutes les informations.";

        // On récupère tous les lieux
        for (Lieu lieu : partie.getElements().getLieux()) {
            int ScoreTemps = Integer.MAX_VALUE;
            // On teste les questions par rapport aux temps
            // Questions Temps Lieu à poser = 30 (5*6) :
            // Temps : 2/3/4/5/6
            // Lieu : 1/2/3/4/5/6
            for (int temps = 2; temps <= 6; temps++) {
                // On teste avec tous les indices publics
                // -----------------------------
                // Questions Temps Lieu = 49 possibilités d'indices (7*7) :
                // Indice public (Nombre de personnages) : 0/1/2/3/4/5/6
                // Indice privé (Temps auquel il est passé) : A/B/C/D/J/S/Rejouer
                for (int indicePublic = 0; indicePublic <= 6; indicePublic++) {
                    // On teste avec tous les indices privés
                    for (String indicePrive : new String[]{"A", "B", "C", "D", "J", "S", "Rejouer"}) {
                        double score = simulerTemps(lieu, new Temps(temps), indicePublic, indicePrive);
                        if (score < ScoreTemps) {
                            ScoreTemps = (int) score;
                        }
                    }
                }
                // On a fini de tester tous les indices
                if (ScoreTemps > meilleurScore) {
                    meilleureQuestion = "Temps : " + temps;
                    meilleureValeur = "Lieu : " + lieu.getNom();
                    meilleurScore = ScoreTemps;
                }
            }

            int ScorePersonnage = Integer.MAX_VALUE;
            // On teste les questions par rapport aux personnages
            // Questions Personnages Lieu à poser = 36 (6*6) :
            // Personnages : A/B/C/D/J/S
            // Lieu : 1/2/3/4/5/6
            for (Personnage personnage : partie.getElements().getPersonnages()) {
                // On teste avec tous les indices publics
                // -----------------------------
                // Questions Personnages Lieu = 28 possibilités d'indices (4*7) :
                // Indice public (Nombre de passages) : 0/1/2/3
                // Indice privé (Temps auquel il est passé) : 0/1/2/3/4/5/6
                for (int indicePublic = 0; indicePublic <= 3; indicePublic++) {
                    // On teste avec tous les indices privés
                    for (int indicePrive = 0; indicePrive <= 6; indicePrive++) {
                        double score = simulerPersonnage(personnage, lieu, indicePublic, indicePrive);
                        if (score < ScorePersonnage) {
                            ScorePersonnage = (int) score;
                        }
                    }
                }
                // On a fini de tester tous les indices
                if (ScorePersonnage > meilleurScore) {
                    meilleureQuestion = "Personnage : " + personnage.getNom();
                    meilleureValeur = "Lieu : " + lieu.getNom();
                    meilleurScore = ScorePersonnage;
                }
            }
        }

        resultats[0] = meilleureQuestion;
        resultats[1] = meilleureValeur;
        resultats[2] = String.valueOf(meilleurScore);
        return resultats;
    }
    // Moyenne :
    // Prend la moyenne des possibilités d'indices pour chaque question
    // Prend le max des moyennes des possibilités d'indices récupérées
    public String[] recommanderQuestionMoyenne(int meilleurScore){
        String[] resultats = new String[3];
        String meilleureQuestion = "Aucune recommandation";
        String meilleureValeur = "Vous avez déjà toutes les informations.";

        // On récupère tous les lieux
        for (Lieu lieu : partie.getElements().getLieux()) {
            int somme = 0;
            int diviseur = 0;
            // On teste les questions par rapport aux temps
            // Questions Temps Lieu à poser = 30 (5*6) :
            // Temps : 2/3/4/5/6
            // Lieu : 1/2/3/4/5/6
            for (int temps = 2; temps <= 6; temps++) {
                // On teste avec tous les indices publics
                // -----------------------------
                // Questions Temps Lieu = 49 possibilités d'indices (7*7) :
                // Indice public (Nombre de personnages) : 0/1/2/3/4/5/6
                // Indice privé (Temps auquel il est passé) : A/B/C/D/J/S/Rejouer
                for (int indicePublic = 0; indicePublic <= 6; indicePublic++) {
                    // On teste avec tous les indices privés
                    for (String indicePrive : new String[]{"A", "B", "C", "D", "J", "S", "Rejouer"}) {
                        diviseur++;
                        double score = simulerTemps(lieu, new Temps(temps), indicePublic, indicePrive);
                        somme += (int) score;
                    }
                }
                somme = somme / diviseur;
                if (somme > meilleurScore) {
                    meilleureQuestion = "Temps : " + temps;
                    meilleureValeur = "Lieu : " + lieu.getNom();
                    meilleurScore = somme;
                }
            }
            somme = 0;
            diviseur = 0;
            // On teste les questions par rapport aux personnages
            // Questions Personnages Lieu à poser = 36 (6*6) :
            // Personnages : A/B/C/D/J/S
            // Lieu : 1/2/3/4/5/6
            for (Personnage personnage : partie.getElements().getPersonnages()) {
                // On teste avec tous les indices publics
                // -----------------------------
                // Questions Personnages Lieu = 28 possibilités d'indices (4*7) :
                // Indice public (Nombre de passages) : 0/1/2/3
                // Indice privé (Temps auquel il est passé) : 0/1/2/3/4/5/6
                for (int indicePublic = 0; indicePublic <= 3; indicePublic++) {
                    // On teste avec tous les indices privés
                    for (int indicePrive = 0; indicePrive <= 6; indicePrive++) {
                        diviseur++;
                        double score = simulerPersonnage(personnage, lieu, indicePublic, indicePrive);
                        somme += (int) score;
                    }
                }
                somme = somme/diviseur;
                if (somme > meilleurScore) {
                    meilleureQuestion = "Personnage : " + personnage.getNom();
                    meilleureValeur = "Lieu : " + lieu.getNom();
                    meilleurScore = somme;
                }
            }

        }

        resultats[0] = meilleureQuestion;
        resultats[1] = meilleureValeur;
        resultats[2] = String.valueOf(meilleurScore);
        return resultats;
    }

}
