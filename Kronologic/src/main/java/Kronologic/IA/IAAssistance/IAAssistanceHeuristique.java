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
        return new String[]{"Aucune recommandation", "Vous avez déjà toutes les informations."};
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
