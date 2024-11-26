package Kronologic.Indice;

import Kronologic.Elements.Lieu;

public class IndicePersonnage extends Indice {

    private String nomPersonnage;
    private String infoPrive;

    public IndicePersonnage(Lieu l, int p, String n, String ip) {
        super(l, p);
        this.nomPersonnage = n;
        this.infoPrive = ip;
    }
}
