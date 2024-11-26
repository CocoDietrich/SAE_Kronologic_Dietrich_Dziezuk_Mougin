package Kronologic.Elements;

import java.util.List;

public class Personnage {

    private String name;
    private List<Lieu> listeDeplacements;

    public Personnage(String n, List<Lieu> l) {
        this.name = n;
        this.listeDeplacements = l;
    }
}
