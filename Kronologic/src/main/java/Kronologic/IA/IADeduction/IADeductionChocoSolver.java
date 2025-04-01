package Kronologic.IA.IADeduction;

import Kronologic.Jeu.Elements.*;
import Kronologic.Jeu.Partie;

public class IADeductionChocoSolver extends IADeduction {

    private final ModeleChocoSolver model;

    public IADeductionChocoSolver(Partie partie) {
        super(partie);
        this.model = new ModeleChocoSolver(getPersonnagesNoms(), getSallesAdjacentes(), getPositionsInitiales());
    }

    // Méthode pour poser une question sur le temps
    @Override
    public void poserQuestionTemps(Lieu lieu, Temps temps, int infoPublic, String infoPrive) {
        if (!infoPrive.equals("Rejouer")) {
            model.ajouterContraintePersonnage(new Personnage(infoPrive), lieu, temps.getValeur());
        }
        model.ajouterContrainteTemps(lieu, temps, infoPublic);
    }

    // Méthode pour poser une question sur un personnage
    @Override
    public void poserQuestionPersonnage(Personnage personnage, Lieu lieu, int infoPublic, int infoPrive) {
        model.ajouterContraintePersonnage(personnage, lieu, infoPrive);
        model.ajouterContrainteNombreDePassages(personnage, lieu, infoPublic);
    }

    // Méthode pour afficher l'historique des déductions
    @Override
    public String afficherHistoriqueDeduction() {
        return model.affichagePropagate();
    }

    // Méthode pour récupérer le modèle
    public ModeleChocoSolver getModele(){
        return model;
    }

    // Méthode pour vérifier si la solution a été trouvée
    public boolean solutionTrouvee() {
        return model.getCoupablePersonnage().isInstantiated()
                && model.getCoupableLieu().isInstantiated()
                && model.getCoupableTemps().isInstantiated();
    }
}