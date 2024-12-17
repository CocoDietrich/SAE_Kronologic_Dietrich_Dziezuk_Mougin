package Kronologic.IA.IADeduction;

import Kronologic.Jeu.Indice.Indice;
import Kronologic.Jeu.Partie;

import java.util.List;

public class IADeductionHeuristique extends IADeduction {

    private Partie partie;

    public IADeductionHeuristique(Partie partie) {
        super();
        this.partie = partie;
    }

    @Override
    public void analyserIndices(List<Indice> indices) {
        // TODO : Implémenter une analyse basée sur des heuristiques simples
    }

    @Override
    public String afficherHistoriqueDeduction() {
        // TODO : Retourner l'historique des conclusions heuristiques
        return null;
    }
}
