package Kronologic.Jeu.Elements;

import java.util.ArrayList;
import java.util.List;

public class GestionnairePions {

    private final List<Pion> pions;

    public GestionnairePions() {
        this.pions = new ArrayList<>();
    }

    public void ajouterPion(Pion pion) {
        if (this.pions.contains(pion) || pion == null) {
            return;
        }
        this.pions.add(pion);
    }

    public void deplacerPion(Pion pion, Lieu nouveauLieu, Temps nouveauTemps, int x, int y) {
        if (nouveauLieu == null || nouveauTemps == null || pion == null) {
            return;
        }
        Pion pionASupprimer = null;
        for (Pion p : this.pions) {
            if (p.equals(pion)) {
                Note note;
                if (pion.getNote().getPersonnage() == null){
                    note = new Note(nouveauLieu, nouveauTemps, pion.getNote().getNbPersonnages());
                }
                else {
                    note = new Note(nouveauLieu, nouveauTemps, pion.getNote().getPersonnage());
                }
                note.setEstAbsence(pion.getNote().estAbsence());
                note.setEstHypothese(pion.getNote().estHypothese());
                p.setNote(note);
                p.deplacerPion(x, y);

                int compteurDePionsIdentiques = 0;
                for (Pion p2 : this.pions) {
                    if (p2.equals(p)) {
                        compteurDePionsIdentiques++;
                    }
                    if (compteurDePionsIdentiques == 2) {
                        pionASupprimer = p2;
                        break;
                    }
                }
            }
        }
        if (pionASupprimer != null) {
            this.pions.remove(pionASupprimer);
        }
    }

    public void supprimerPion(Pion pion) {
        if (pion == null) {
            return;
        }
        this.pions.remove(pion);
    }

    public List<Pion> getPions() {
        return this.pions;
    }
}
