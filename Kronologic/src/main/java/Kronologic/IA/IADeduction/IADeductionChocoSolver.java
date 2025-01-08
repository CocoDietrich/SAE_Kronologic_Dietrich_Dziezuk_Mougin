package Kronologic.IA.IADeduction;

import Kronologic.Jeu.Elements.*;
import Kronologic.Jeu.Indice.Indice;
import Kronologic.Jeu.Indice.IndicePersonnage;
import Kronologic.Jeu.Indice.IndiceTemps;
import Kronologic.Jeu.Partie;

import java.util.List;

public class IADeductionChocoSolver extends IADeduction {

    private final ModeleChocoSolver model;

    public IADeductionChocoSolver(Partie partie) {
        super(partie);
        this.model = new ModeleChocoSolver(getPersonnagesNoms(), getSallesAdjacentes(), getPositionsInitiales());
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
        model.ajouterContraintePersonnage(personnage, lieu, infoPrive);
        model.ajouterContrainteNombreDePassages(personnage, lieu, infoPublic);
    }

    @Override
    public String afficherHistoriqueDeduction() {
        return model.affichagePropagate();
    }
}