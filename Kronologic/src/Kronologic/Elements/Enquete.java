package Kronologic.Elements;

import Kronologic.Indice.Indice;

import java.util.List;

public class Enquete {

    private final List<Personnage> personnages;
    private final List<Lieu> lieux;
    private final List<Indice> indices;
    private final Personnage meurtrier;
    private final Lieu lieuDuCrime;
    private final Temps tempsDuCrime;

    public Enquete(List<Personnage> lp, List<Lieu> ll, List<Indice> li, Personnage m, Lieu l, Temps t) {
        this.personnages = lp;
        this.lieux = ll;
        this.indices = li;
        this.meurtrier = m;
        this.lieuDuCrime = l;
        this.tempsDuCrime = t;
    }
}
