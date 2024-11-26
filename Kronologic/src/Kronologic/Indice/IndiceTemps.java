package Kronologic.Indice;

import Kronologic.Elements.Lieu;
import Kronologic.Elements.Temps;

public class IndiceTemps extends Indice{

    private Temps temps;
    private String infoPrive;

    public IndiceTemps(Lieu l, int p, Temps t, String ip) {
        super(l, p);
        this.temps = t;
        this.infoPrive = ip;
    }
}
