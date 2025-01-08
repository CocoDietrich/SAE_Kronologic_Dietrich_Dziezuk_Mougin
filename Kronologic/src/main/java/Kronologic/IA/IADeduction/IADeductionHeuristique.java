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
        super();
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

        this.model = new ModeleHeuristiqueSolver(personnagesNoms,sallesAdjacentes,positionsInitiales);
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