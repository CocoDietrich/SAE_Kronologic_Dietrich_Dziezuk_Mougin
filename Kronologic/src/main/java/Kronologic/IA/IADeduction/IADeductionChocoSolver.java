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
        int[][] sallesAdjacentes = {
                {2, 3},    // Salle 1 est adjacente à 2 et 3
                {1, 3},    // Salle 2 est adjacente à 1 et 3
                {1, 2, 4}, // Salle 3 est adjacente à 1, 2 et 4
                {3, 5, 6}, // Salle 4 est adjacente à 3, 5 et 6
                {4, 6},    // Salle 5 est adjacente à 4 et 6
                {4, 5}     // Salle 6 est adjacente à 4 et 5
        };

        //On recupere les positions de tous les personnages au temps 1
        List<Realite> positionsInitiales = partie.getDeroulement().positionsAuTemps(new Temps(1));

        this.model = new ModeleChocoSolver(personnagesNoms,sallesAdjacentes,positionsInitiales);
    }

    public void poserQuestionTemps(Lieu lieu, Temps temps, int infoPublic, String infoPrive) {
        model.ajouterContrainteTemps(lieu, temps, infoPublic);
        if (!infoPrive.equals("Rejouer")) {
            model.ajouterContraintePersonnage(new Personnage(infoPrive), lieu, temps.getValeur());
        }
    }

    public void poserQuestionPersonnage(Personnage personnage, Lieu lieu, int infoPublic, int infoPrive) {
        model.ajouterContrainteNombreDePassages(personnage, lieu, infoPublic);
        model.ajouterContraintePersonnage(personnage, lieu, infoPrive);
    }


    @Override
    public void analyserIndices(List<Indice> indices) {
        for (Indice indice : indices) {
            if (indice instanceof IndiceTemps it) {
                model.ajouterContrainteTemps(it.getLieu(), it.getTemps(), it.getInfoPublic());
                if (!it.getInfoPrive().equals("Rejouer")) {
                    model.ajouterContraintePersonnage(new Personnage(it.getInfoPrive()), it.getLieu(), it.getTemps().getValeur());
                }
            } else if (indice instanceof IndicePersonnage ip) {
                model.ajouterContrainteNombreDePassages(ip.getPersonnage(), ip.getLieu(), ip.getInfoPublic());
                model.ajouterContraintePersonnage(ip.getPersonnage(), ip.getLieu(), ip.getInfoPrive());
            }
        }
    }

    @Override
    public String afficherHistoriqueDeduction() {
        return model.affichagePropagate();
    }
}