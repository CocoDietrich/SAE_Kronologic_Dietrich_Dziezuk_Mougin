package org.example.kronologic.Kronologic.Pions;

import org.example.kronologic.Kronologic.Elements.Personnage;

public class PionPersonnage extends Pion {

    private boolean absences;
    private Personnage personnage;

    public PionPersonnage(int i, boolean h, boolean a, Personnage p) {
        super(i, h);
        this.absences = a;
        this.personnage = p;
    }

    public boolean getAbsences() {
        return absences;
    }

    public Personnage getPersonnage() {
        return personnage;
    }

    public void setAbsences(boolean absences) {
        this.absences = absences;
    }

    public void setPersonnage(Personnage personnage) {
        this.personnage = personnage;
    }
}
