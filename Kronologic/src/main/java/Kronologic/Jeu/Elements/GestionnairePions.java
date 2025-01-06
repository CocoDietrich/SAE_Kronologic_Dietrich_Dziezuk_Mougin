package Kronologic.Jeu.Elements;

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

    public void deplacerPion(Pion pion, Lieu nouveauLieu, Temps nouveauTemps, int x, int y) {
        for (Pion p : this.pions) {
            if (p.getNote().toString().equals(pion.getNote().toString())){
                Note note = new Note(nouveauLieu, nouveauTemps, pion.getNote().getPersonnage());
                note.setEstAbsence(pion.getNote().estAbsence());
                note.setEstHypothese(pion.getNote().estHypothese());
                pion.setNote(note);
                pion.deplacerPion(x, y);
                this.pions.set(this.pions.indexOf(p), pion);
                break;
            }
        }
    }

    public void supprimerPion(Pion pion) {
        this.pions.remove(pion);
    }

    public List<Pion> getPions() {
        return this.pions;
    }



}
