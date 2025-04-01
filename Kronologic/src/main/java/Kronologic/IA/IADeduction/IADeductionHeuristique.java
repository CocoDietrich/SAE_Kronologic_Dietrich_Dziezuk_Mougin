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

    // Méthode pour poser une question sur le temps
    @Override
    public void poserQuestionTemps(Lieu lieu, Temps temps, int infoPublic, String infoPrive) {
        if (!infoPrive.equals("Rejouer")) {
            model.ajouterContraintePersonnage(new Personnage(infoPrive), lieu, temps.getValeur()-1);
        }
        model.ajouterContrainteTemps(lieu, temps, infoPublic);
    }

    // Méthode pour poser une question sur un personnage
    @Override
    public void poserQuestionPersonnage(Personnage personnage, Lieu lieu, int infoPublic, int infoPrive) {
        if (infoPrive != 0) {
            model.ajouterContraintePersonnage(personnage, lieu, infoPrive-1);
        }
        model.ajouterContrainteNombreDePassages(personnage, lieu, infoPublic);
    }

    // Méthode pour afficher l'historique des déductions
    @Override
    public String afficherHistoriqueDeduction() {
        return model.affichagePropagate();
    }

    // Méthode pour récupérer les domaines des personnages
    public boolean[][][] recupererDomainesPersonnages() {
        return model.getDomainesPersonnages();
    }

    // Méthode pour récupérer le modèle
    public ModeleHeuristiqueSolver getModel() {
        return model;
    }
}