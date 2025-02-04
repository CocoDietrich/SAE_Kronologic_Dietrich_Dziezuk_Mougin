package Kronologic.IA.IADeduction;

import Kronologic.Jeu.Elements.*;
import Kronologic.Jeu.Partie;
import org.chocosolver.solver.variables.IntVar;

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

    public ModeleChocoSolver getModele(){
        return model;
    }
}