package Kronologic.IA.IADeduction;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Realite;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.Jeu.Indice.Indice;
import Kronologic.Jeu.Indice.IndicePersonnage;
import Kronologic.Jeu.Indice.IndiceTemps;
import Kronologic.Jeu.Partie;

import java.util.List;

public class IADeductionHeuristique extends IADeduction {

    private final ModeleHeuristiqueSolver model;

    public IADeductionHeuristique(Partie partie) {
        super(partie);
        this.model = new ModeleHeuristiqueSolver(getPersonnagesNoms(), getSallesAdjacentes(), getPositionsInitiales());
    }

    @Override
    public void poserQuestionTemps(Lieu lieu, Temps temps, int infoPublic, String infoPrive) {
        if (!infoPrive.equals("Rejouer")) {
            model.ajouterContraintePersonnage(new Personnage(infoPrive), lieu, temps.getValeur());
        }
        model.ajouterContrainteTemps(lieu, temps, infoPublic);
    }

    @Override
    public void poserQuestionPersonnage(Personnage personnage, Lieu lieu, int infoPublic, int infoPrive) {
        model.ajouterContraintePersonnage(personnage, lieu, infoPrive-1);
        model.ajouterContrainteNombreDePassages(personnage, lieu, infoPublic);
    }

    @Override
    public String afficherHistoriqueDeduction() {
        return model.affichagePropagate();
    }
}