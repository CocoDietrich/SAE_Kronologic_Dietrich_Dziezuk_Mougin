package Kronologic.IA.IADeduction;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.Jeu.Partie;


public class IADeductionHeuristique extends IADeduction {

    private final ModeleHeuristiqueSolver model;

    public IADeductionHeuristique(Partie partie) {
        super(partie);
        this.model = new ModeleHeuristiqueSolver(getPersonnagesNoms(), getSallesAdjacentes(), getPositionsInitiales());
    }

    @Override
    public void poserQuestionTemps(Lieu lieu, Temps temps, int infoPublic, String infoPrive) {
        if (!infoPrive.equals("Rejouer")) {
            model.ajouterContraintePersonnage(new Personnage(infoPrive), lieu, temps.getValeur()-1);
        }
        model.ajouterContrainteTemps(lieu, temps, infoPublic);
    }

    @Override
    public void poserQuestionPersonnage(Personnage personnage, Lieu lieu, int infoPublic, int infoPrive) {
        if (infoPrive != 0) {
            model.ajouterContraintePersonnage(personnage, lieu, infoPrive-1);
        }
        model.ajouterContrainteNombreDePassages(personnage, lieu, infoPublic);
    }

    @Override
    public String afficherHistoriqueDeduction() {
        return model.affichagePropagate();
    }

    public boolean[][][] recupererDomainesPersonnages() {
        return model.getDomainesPersonnages();
    }

    public ModeleHeuristiqueSolver getModel() {
        return model;
    }
}