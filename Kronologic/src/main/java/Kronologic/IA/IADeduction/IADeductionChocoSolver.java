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
        //On recupere le premier caractere du nom de tous les personnages
        List<Personnage> personnages = partie.getElements().getPersonnages();
        String[] personnagesNoms = personnages.stream()
                .map(p -> p.getNom().substring(0, 1))
                .toArray(String[]::new);

        //On recupere les salles adjacentes de chaque salle
        List<Lieu> lieux = partie.getElements().getLieux();
        int[][] sallesAdjacentes = lieux.stream()
                .map(l -> l.getListeLieuxAdjacents().stream().mapToInt(Lieu::getId).toArray())
                .toArray(int[][]::new);

        //On recupere les positions de tous les personnages au temps 1
        List<Realite> positionsInitiales = partie.getDeroulement().positionsAuTemps(new Temps(1));

        this.model = new ModeleChocoSolver(personnagesNoms, sallesAdjacentes, positionsInitiales);
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