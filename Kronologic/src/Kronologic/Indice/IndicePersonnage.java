package Kronologic.Indice;

import Kronologic.Elements.Lieu;

public class IndicePersonnage extends Indice {

    private final String nomPersonnage;
    private final String infoPrive;

    public IndicePersonnage(Lieu l, int p, String n, String ip) {
        super(l, p);
        this.nomPersonnage = n;
        this.infoPrive = ip;
    }

    public String getNomPersonnage() {
        return nomPersonnage;
    }

    public String getInfoPrive() {
        return infoPrive;
    }
}
