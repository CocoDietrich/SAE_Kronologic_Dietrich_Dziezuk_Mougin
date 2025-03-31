package Kronologic.IA.IAAssistance;

import Kronologic.IA.IADeduction.IADeductionChocoSolver;
import Kronologic.Jeu.Indice.*;
import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Partie;

public class IAAssistanceChocoTriche extends IAAssistanceChocoSolver {
    public IAAssistanceChocoTriche(IADeductionChocoSolver deduction, Partie partie) {
        super(deduction, partie);
    }

    @Override
    public String[] recommanderQuestionOptimale() {
        double maxReduction = -1;
        String meilleurType = "Aucune";
        String meilleurValeur = "?";
        Lieu meilleurLieu = null;

        for (Indice indice : partie.getGestionnaireIndices().getListeIndices()) {
            if (indice instanceof IndicePersonnage ip) {
                double reduction = simulerPersonnage(ip.getLieu(), ip.getPersonnage(), ip.getInfoPublic(), ip.getInfoPrive());
                if (reduction > maxReduction) {
                    maxReduction = reduction;
                    meilleurType = "Personnage";
                    meilleurValeur = ip.getPersonnage().getNom();
                    meilleurLieu = ip.getLieu();
                }
            } else if (indice instanceof IndiceTemps it) {
                double reduction = simulerTemps(it.getLieu(), it.getTemps(), it.getInfoPublic(), it.getInfoPrive());
                if (reduction > maxReduction) {
                    maxReduction = reduction;
                    meilleurType = "Temps";
                    meilleurValeur = String.valueOf(it.getTemps().getValeur());
                    meilleurLieu = it.getLieu();
                }
            }
        }


        return meilleurLieu != null ?
                new String[]{"Lieu : " + meilleurLieu.getNom(), meilleurType + " : " + meilleurValeur} :
                new String[]{"Aucune recommandation", "Vous avez déjà toutes les informations."};
    }
}