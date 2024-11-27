package org.example.kronologic.Kronologic.Pions;

public abstract class Pion {

    private final int id;
    private boolean hypothese;
    private int idLieu;
    private int temps;

    public Pion(int i, boolean h) {
        this.id = i;
        this.hypothese = h;
        this.idLieu = 0;
        this.temps = 0;
    }

    public void deplacerPion(int l, int t) {
        if (l < 1 || l > 6) {
            this.idLieu = 0;
        }
        else {
            this.idLieu = l;
        }
        if (t < 1 || t > 6) {
            this.temps = 0;
        }
        else {
            this.temps = t;
        }
    }

    public int getId() {
        return id;
    }

    public boolean getHypothese() {
        return hypothese;
    }

    public int getIdLieu() {
        return idLieu;
    }

    public int getTemps() {
        return temps;
    }

    public void setHypothese(boolean hypothese) {
        this.hypothese = hypothese;
    }

    public void setIdLieu(int idLieu) {
        this.idLieu = idLieu;
    }

    public void setTemps(int temps) {
        this.temps = temps;
    }
}
