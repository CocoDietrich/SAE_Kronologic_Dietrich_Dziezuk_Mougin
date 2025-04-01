package Kronologic.IA.IADeduction;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Realite;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.Jeu.Partie;

import java.util.List;

public abstract class IADeduction {
    private final String[] personnagesNoms;
    private final int[][] sallesAdjacentes;
    private final List<Realite> positionsInitiales;

    public IADeduction(Partie partie) {
        //On recupere le premier caractere du nom de tous les personnages
        List<Personnage> personnages = partie.getElements().personnages();
        personnagesNoms = personnages.stream()
                .map(p -> p.getNom().substring(0, 1))
                .toArray(String[]::new);

        //On recupere les salles adjacentes de chaque salle
        List<Lieu> lieux = partie.getElements().lieux();
        sallesAdjacentes = lieux.stream()
                .map(l -> l.getListeLieuxAdjacents().stream().mapToInt(Lieu::getId).toArray())
                .toArray(int[][]::new);

        //On recupere les positions de tous les personnages au temps 1
        positionsInitiales = partie.getDeroulement().positionsAuTemps(new Temps(1));
    }
    // Méthode pour poser une question sur le temps
    public abstract void poserQuestionTemps(Lieu lieu, Temps temps, int infoPublic, String infoPrive);

    // Méthode pour poser une question sur un personnage
    public abstract void poserQuestionPersonnage(Personnage personnage, Lieu lieu, int infoPublic, int infoPrive);

    // Méthode pour afficher l'historique des déductions
    public abstract String afficherHistoriqueDeduction();

    // Méthode pour récupérer le nom des personnages
    public String[] getPersonnagesNoms() {
        return personnagesNoms;
    }

    // Méthode pour récupérer les salles adjacentes
    public int[][] getSallesAdjacentes() {
        return sallesAdjacentes;
    }

    // Méthode pour récupérer les positions initiales des personnages
    public List<Realite> getPositionsInitiales() {
        return positionsInitiales;
    }
}