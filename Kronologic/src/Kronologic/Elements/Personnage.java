package Kronologic.Elements;

import java.util.List;

public class Personnage {

    private final String nom;
    private final List<Lieu> listeDeplacements;

    public Personnage(String n, List<Lieu> l) {
        this.nom = n;
        this.listeDeplacements = l;
    }

    public String getNom() {
        return nom;
    }

    public List<Lieu> getListeDeplacements() {
        return listeDeplacements;
    }
}
