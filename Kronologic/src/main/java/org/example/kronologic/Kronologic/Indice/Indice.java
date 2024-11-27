package org.example.kronologic.Kronologic.Indice;

import org.example.kronologic.Kronologic.Elements.Lieu;

public abstract class Indice {

    private final Lieu lieu;
    private final int infoPublic;

    public Indice(Lieu l, int p) {
        if (l == null) {
            throw new IllegalArgumentException("Le lieu ne peut pas être nul.");
        }
        if (p < 0) {
            throw new IllegalArgumentException("L'information publique ne peut pas être négatif.");
        }
        this.lieu = l;
        this.infoPublic = p;
    }

    @Override
    public abstract String toString();

    // Getters

    public Lieu getLieu() {
        return lieu;
    }

    public int getInfoPublic() {
        return infoPublic;
    }
}

