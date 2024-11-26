package Kronologic.Indice;

import Kronologic.Elements.Lieu;
import Kronologic.Elements.Temps;

public class IndiceTemps extends Indice{

    private final Temps temps;
    private final String infoPrive;

    public IndiceTemps(Lieu l, int p, Temps t, String ip) {
        super(l, p);
        this.temps = t;
        this.infoPrive = ip;
    }

    public Temps getTemps() {
        return temps;
    }

    public String getInfoPrive() {
        return infoPrive;
    }
}
