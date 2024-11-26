package Kronologic.Pions;

import Kronologic.Elements.Personnage;

public class PionPersonnage extends Pion {

    private boolean absences;
    private Personnage personnage;

    public PionPersonnage(int i, boolean h, boolean a, Personnage p) {
        super(i, h);
        this.absences = a;
        this.personnage = p;
    }
}
