package Kronologic.IA.IAAssistance;

import Kronologic.IA.IADeduction.IADeductionHeuristique;
import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Indice.*;
import Kronologic.Jeu.Partie;

import java.util.List;

public class IAAssistanceHeuristiqueTriche extends IAAssistanceHeuristique {

    public IAAssistanceHeuristiqueTriche(IADeductionHeuristique deduction, Partie partie) {
        super(deduction, partie);
    }

    /**
     * Recommande la question optimale à poser au joueur pour l'IA qui triche.
     *
     * @return Un tableau contenant le lieu et le type de question recommandée.
     */
    @Override
    public String[] recommanderQuestionOptimale() {
        List<Indice> indices = partie.getGestionnaireIndices().getListeIndices();
        int maxReduction = -1;
        String meilleurType = "Aucune";
        String meilleurValeur = "?";
        Lieu meilleurLieu = null;

        for (Indice indice : indices) {
            if (indice instanceof IndicePersonnage ip) {
                int reduction = simulerPersonnage(ip.getPersonnage(), ip.getLieu(), ip.getInfoPublic(), ip.getInfoPrive());
                if (reduction > maxReduction) {
                    maxReduction = reduction;
                    meilleurType = "Personnage";
                    meilleurValeur = ip.getPersonnage().getNom();
                    meilleurLieu = ip.getLieu();
                }
            } else if (indice instanceof IndiceTemps it) {
                if (it.getTemps().getValeur() == 1) continue;
                int reduction = simulerTemps(it.getLieu(), it.getTemps(), it.getInfoPublic(), it.getInfoPrive());
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