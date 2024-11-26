package Kronologic.Indice;

import Kronologic.Elements.Lieu;

public class Indice {

    private final Lieu lieu;
    private final int infoPublic;

    public Indice(Lieu l, int p) {
        this.lieu = l;
        this.infoPublic = p;
    }

    // Getters

    public Lieu getLieu() {
        return lieu;
    }

    public int getInfoPublic() {
        return infoPublic;
    }
}

