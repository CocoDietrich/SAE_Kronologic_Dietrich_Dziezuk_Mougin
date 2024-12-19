package Kronologic.MVC.Vue;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Note;

import java.util.ArrayList;
import java.util.List;

public class GestionnairePions {

    private List<Pion> pions;

    public GestionnairePions() {
        this.pions = new ArrayList<>();
    }

    public void ajouterPion(Pion pion) {
        this.pions.add(pion);
    }

    public void deplacerPion(Pion pion, Lieu nouveauLieu, int x, int y) {
        int index = this.pions.indexOf(pion);
        Note note = new Note(nouveauLieu, pion.getNote().getTemps(), pion.getNote().getPersonnage());
        note.setEstAbsence(pion.getNote().estAbsence());
        note.setEstHypothese(pion.getNote().estHypothese());
        pion.setNote(note);
        pion.deplacerPion(x, y);
        this.pions.set(index, pion);
    }

    public void supprimerPion(Pion pion) {
        this.pions.remove(pion);
    }

    public List<Pion> getPions() {
        return this.pions;
    }



}
