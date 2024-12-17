package Kronologic.IA.IADeduction;

import Kronologic.Jeu.Elements.Hypothese;
import Kronologic.Jeu.Indice.Indice;
import java.util.List;

public abstract class IADeduction {
    // Méthode pour analyser les indices
    public abstract void analyserIndices(List<Indice> indices);

    // Méthode pour afficher l'historique des déductions
    public abstract String afficherHistoriqueDeduction();
}