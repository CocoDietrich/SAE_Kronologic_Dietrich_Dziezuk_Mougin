package Kronologic.IA.IAAssistance;

import Kronologic.IA.IADeduction.IADeductionHeuristique;
import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.Jeu.Indice.Indice;
import Kronologic.Jeu.Indice.IndicePersonnage;
import Kronologic.Jeu.Indice.IndiceTemps;
import Kronologic.Jeu.Partie;

public abstract class IAAssistanceHeuristique extends IAAssistance {
    protected IADeductionHeuristique iaDeductionHeuristique;
    protected Partie partie;

    public IAAssistanceHeuristique(IADeductionHeuristique iaDeductionHeuristique, Partie partie) {
        this.iaDeductionHeuristique = iaDeductionHeuristique;
        this.partie = partie;
    }

    protected int simulerTemps(Lieu lieu, Temps temps, int infoPublic, String infoPrive) {
        IADeductionHeuristique clone = copie();
        int nbInitial = compterDomaines(clone.recupererDomainesPersonnages());
        clone.poserQuestionTemps(lieu, temps, infoPublic, infoPrive);
        int nbFinal = compterDomaines(clone.recupererDomainesPersonnages());
        return nbInitial - nbFinal;
    }

    protected int simulerPersonnage(Personnage personnage, Lieu lieu, int infoPublic, int infoPrive) {
        IADeductionHeuristique clone = copie();
        int nbInitial = compterDomaines(clone.recupererDomainesPersonnages());
        clone.poserQuestionPersonnage(personnage, lieu, infoPublic, infoPrive);
        int nbFinal = compterDomaines(clone.recupererDomainesPersonnages());
        return nbInitial - nbFinal;
    }

    private int compterDomaines(boolean[][][] domaines) {
        int count = 0;
        for (boolean[][] d1 : domaines)
            for (boolean[] d2 : d1)
                for (boolean b : d2)
                    if (b) count++;
        return count;
    }

    protected IADeductionHeuristique copie() {
        IADeductionHeuristique copie = new IADeductionHeuristique(partie);
        for (Indice indice : partie.getIndicesDecouverts()) {
            if (indice instanceof IndicePersonnage ip) {
                copie.poserQuestionPersonnage(ip.getPersonnage(), ip.getLieu(), ip.getInfoPublic(), ip.getInfoPrive());
            } else if (indice instanceof IndiceTemps it) {
                copie.poserQuestionTemps(it.getLieu(), it.getTemps(), it.getInfoPublic(), it.getInfoPrive());
            }
        }
        return copie;
    }

    @Override
    public String corrigerDeductions() {
        return null;
    }
}