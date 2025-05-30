package Kronologic.Jeu;

import Kronologic.Jeu.Elements.*;

import java.util.ArrayList;

public class Deroulement {
    private final ArrayList<Realite> listePositions;

    public Deroulement(ArrayList<Realite> listePositions) {
        if (listePositions == null) {
            throw new IllegalArgumentException("La liste de positions ne peut pas être nulle");
        }
        if (listePositions.isEmpty()) {
            throw new IllegalArgumentException("La liste de positions ne peut pas être vide");
        }
        this.listePositions = listePositions;
    }

    // Getter pour listePositions

    public ArrayList<Realite> getListePositions() {
        return listePositions;
    }

    public ArrayList<Realite> positionsAuTemps(Temps t)  {
        ArrayList<Realite> res = new ArrayList<>();
        for (Realite p : listePositions) {
            if (p.getTemps().getValeur() == t.getValeur()) {
                res.add(p);
            }
        }
        return res;
    }

    public ArrayList<Realite> positionsDansLieu(Lieu l) {
        ArrayList<Realite> res = new ArrayList<>();
        for (Realite p : listePositions) {
            if (p.getLieu().getNom().equals(l.getNom())) {
                res.add(p);
            }
        }
        return res;
    }

    public ArrayList<Realite> positionsDuPersonnage(Personnage p) {
        ArrayList<Realite> res = new ArrayList<>();
        for (Realite pos : listePositions) {
            if (pos.getPersonnage().getNom().equals(p.getNom())) {
                res.add(pos);
            }
        }
        return res;
    }
}
