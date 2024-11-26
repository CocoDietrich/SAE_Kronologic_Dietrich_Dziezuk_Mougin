package Kronologic.Elements;

import java.util.List;

public class Personnage {

    private final String name;
    private final List<Lieu> listeDeplacements;

    public Personnage(String n, List<Lieu> l) {
        this.name = n;
        this.listeDeplacements = l;
    }

    public String getName() {
        return name;
    }

    public List<Lieu> getListeDeplacements() {
        return listeDeplacements;
    }
}
