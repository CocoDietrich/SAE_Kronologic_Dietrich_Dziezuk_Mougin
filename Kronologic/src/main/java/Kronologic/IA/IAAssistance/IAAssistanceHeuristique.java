package Kronologic.IA.IAAssistance;

import Kronologic.IA.IADeduction.IADeductionHeuristique;
import Kronologic.IA.IADeduction.ModeleChocoSolver;
import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.Jeu.Indice.Indice;
import Kronologic.Jeu.Indice.IndicePersonnage;
import Kronologic.Jeu.Indice.IndiceTemps;
import Kronologic.Jeu.Partie;

import java.util.List;

public class IAAssistanceHeuristique extends IAAssistance {
    private IADeductionHeuristique iaDeductionHeuristique;
    private Partie partie;

    public IAAssistanceHeuristique(IADeductionHeuristique iaDeductionHeuristique,Partie partie) {
        super();
        this.iaDeductionHeuristique = iaDeductionHeuristique;
        this.partie = partie;
    }

    @Override
    public String[] recommanderQuestionOptimaleTriche() {
        List<Indice> indices = partie.getGestionnaireIndices().getListeIndices();

        int maxReduction = -1;
        String meilleurType = "Aucune";
        String meilleurValeur = "?";
        Lieu meilleurLieu = null;

        for (Indice indice : indices) {
            if (indice instanceof IndicePersonnage indicePerso) {
                int reduction = simulerPersonnage(
                        indicePerso.getPersonnage(),
                        indicePerso.getLieu(),
                        indicePerso.getInfoPublic(),
                        indicePerso.getInfoPrive()
                );

                if (reduction > maxReduction) {
                    maxReduction = reduction;
                    meilleurType = "Personnage";
                    meilleurValeur = indicePerso.getPersonnage().getNom();
                    meilleurLieu = indicePerso.getLieu();
                }
            } else if (indice instanceof IndiceTemps indiceTemps) {
                if (indiceTemps.getTemps().getValeur() == 1) {
                    continue;
                }
                int reduction = simulerTemps(
                        indiceTemps.getLieu(),
                        indiceTemps.getTemps(),
                        indiceTemps.getInfoPublic(),
                        indiceTemps.getInfoPrive()
                );

                if (reduction > maxReduction) {
                    maxReduction = reduction;
                    meilleurType = "Temps";
                    meilleurValeur = String.valueOf(indiceTemps.getTemps().getValeur());
                    meilleurLieu = indiceTemps.getLieu();
                }
            }
//            System.out.println("Reduction : " + maxReduction);
//            System.out.println("Meilleur type : " + meilleurType);
//            System.out.println("Meilleur valeur : " + meilleurValeur);
//            if (meilleurLieu != null) {
//                System.out.println("Meilleur lieu : " + meilleurLieu.getNom());
//            }
        }

        //System.out.println("------------------------------------------------------");

        return meilleurLieu != null ?
                new String[]{"Lieu : " + meilleurLieu.getNom(), meilleurType + " : " + meilleurValeur} :
                new String[]{"Aucune recommandation", "Vous avez déjà toutes les informations."};
    }

    @Override
    public String[] recommanderQuestionOptimaleTrichePas() {
        int meilleurScore = -1;

        int strategie = (int) (Math.random() * 3); // 0 = min, 1 = max, 2 = moyenne

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
            int ScoreTemps = Integer.MIN_VALUE;
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
                        int score = simulerTemps(lieu, new Temps(temps), indicePublic, indicePrive);
                        if (score > ScoreTemps) {
                            ScoreTemps = score;
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

            int ScorePersonnage = Integer.MIN_VALUE;
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
                        int score = simulerPersonnage(personnage, lieu, indicePublic, indicePrive);
                        if (score > ScorePersonnage) {
                            ScorePersonnage = score;
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
                        int score = simulerTemps(lieu, new Temps(temps), indicePublic, indicePrive);
                        if (score < ScoreTemps) {
                            ScoreTemps = score;
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
                        int score = simulerPersonnage(personnage, lieu, indicePublic, indicePrive);
                        if (score < ScorePersonnage) {
                            ScorePersonnage = score;
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
                        int score = simulerTemps(lieu, new Temps(temps), indicePublic, indicePrive);
                        somme += score;
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
                        int score = simulerPersonnage(personnage, lieu, indicePublic, indicePrive);
                        somme += score;
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

    public int simulerTemps(Lieu lieu, Temps temps, int infoPublic, String infoPrive) {
        // Cloner l'instance de IADeductionHeuristique
        IADeductionHeuristique clone = copie();

        // On récupère le nombre de coupables avant de poser la question
        int nbDomainesInitial = 0;

        // On récupère les domaines des personnages
        boolean[][][] domainesPersonnages = clone.recupererDomainesPersonnages();
        for (boolean[][] personnage : domainesPersonnages) {
            for (boolean[] booleans : personnage) {
                for (boolean aBoolean : booleans) {
                    if (aBoolean) {
                        nbDomainesInitial++;
                    }
                }
            }
        }


        clone.poserQuestionTemps(lieu, temps, infoPublic, infoPrive);

        // On récupère le nombre de coupables après avoir posé la question
        int nbDomainesFinal = 0;

        // On récupère les domaines des personnages
        domainesPersonnages = clone.recupererDomainesPersonnages();
        for (boolean[][] domainesPersonnage : domainesPersonnages) {
            for (boolean[] booleans : domainesPersonnage) {
                for (boolean aBoolean : booleans) {
                    if (aBoolean) {
                        nbDomainesFinal++;
                    }
                }
            }
        }

        // On retourne le gain
        return nbDomainesInitial - nbDomainesFinal;
    }

    public int simulerPersonnage(Personnage personnage, Lieu lieu, int infoPublic, int infoPrive) {
        // Cloner l'instance de IADeductionHeuristique
        IADeductionHeuristique clone = copie();

        // On récupère le nombre de coupables avant de poser la question
        int nbDomainesInitial = 0;

        // On récupère les domaines des personnages
        boolean[][][] domainesPersonnages = clone.recupererDomainesPersonnages();
        for (boolean[][] value : domainesPersonnages) {
            for (boolean[] booleans : value) {
                for (boolean aBoolean : booleans) {
                    if (aBoolean) {
                        nbDomainesInitial++;
                    }
                }
            }
        }

        clone.poserQuestionPersonnage(personnage, lieu, infoPublic, infoPrive);

        // On récupère le nombre de coupables après avoir posé la question
        int nbDomainesFinal = 0;

        // On récupère les domaines des personnages
        domainesPersonnages = clone.recupererDomainesPersonnages();
        for (boolean[][] domainesPersonnage : domainesPersonnages) {
            for (boolean[] booleans : domainesPersonnage) {
                for (boolean aBoolean : booleans) {
                    if (aBoolean) {
                        nbDomainesFinal++;
                    }
                }
            }
        }

        // On retourne le gain
        return nbDomainesInitial - nbDomainesFinal;
    }

    @Override
    public String corrigerDeductions() {
        // TODO : Comparer les déductions du joueur avec une logique heuristique
        return null;
    }

    private IADeductionHeuristique copie(){
        IADeductionHeuristique copie = new IADeductionHeuristique(
                partie
        );

        for (Indice indice : partie.getIndicesDecouverts()) {
            if (indice instanceof IndicePersonnage indicePerso) {
                copie.poserQuestionPersonnage(indicePerso.getPersonnage(), indicePerso.getLieu(), indicePerso.getInfoPublic(), indicePerso.getInfoPrive());
            } else if (indice instanceof IndiceTemps indiceTemps) {
                copie.poserQuestionTemps(indiceTemps.getLieu(), indiceTemps.getTemps(), indiceTemps.getInfoPublic(), indiceTemps.getInfoPrive());
            }
        }
        return copie;
    }
}
