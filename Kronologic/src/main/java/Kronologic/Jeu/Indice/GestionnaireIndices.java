package Kronologic.Jeu.Indice;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;

import java.util.List;
import java.util.Optional;

public class GestionnaireIndices {
    private List<Indice> listeIndices;

    public GestionnaireIndices(List<Indice> listeIndices) {
        this.listeIndices = listeIndices;
    }

    // Poser une question sur un lieu et un personnage
    public Indice poserQuestionPersonnage(Lieu l, Personnage p) {
        return listeIndices.stream()
                .filter(indice -> indice instanceof IndicePersonnage)
                .map(indice -> (IndicePersonnage) indice)
                .filter(indice -> indice.getLieu().getId() == l.getId() && indice.getPersonnage().getNom().equals(p.getNom()))
                .findFirst()
                .orElse(null);
    }

    // Poser une question sur un lieu et un temps
    public Indice poserQuestionTemps(Lieu l, Temps t) {
        return listeIndices.stream()
                .filter(indice -> indice instanceof IndiceTemps)
                .map(indice -> (IndiceTemps) indice)
                .filter(indice -> indice.getLieu().getId() == l.getId() && indice.getTemps().getValeur() == t.getValeur())
                .findFirst()
                .orElse(null);
    }

    public List<Indice> getListeIndices() {
        return listeIndices;
    }
}