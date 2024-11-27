package Kronologic;

import Kronologic.Elements.Lieu;
import Kronologic.Elements.Personnage;
import Kronologic.Elements.Temps;
import Kronologic.Indice.Indice;

import java.util.List;

public class Enquete {

    private final List<Personnage> personnages;
    private final List<Lieu> lieux;
    private final List<Indice> indices;
    private final Personnage meurtrier;
    private final Lieu lieuDuCrime;
    private final Temps tempsDuCrime;
    private final int loupeOr;
    private final int loupeBronze;

    public Enquete(List<Personnage> lp, List<Lieu> ll, List<Indice> li, Personnage m, Lieu l, Temps t, int lo, int lb) {
        this.personnages = lp;
        this.lieux = ll;
        this.indices = li;
        this.meurtrier = m;
        this.lieuDuCrime = l;
        this.tempsDuCrime = t;
        this.loupeOr = lo;
        this.loupeBronze = lb;
    }

    public String verifierLoupe(int nbQuestion){
        if (nbQuestion <= loupeOr){
            return "Loupe Or en " + nbQuestion + "coups !";
        }
        else if (nbQuestion >= loupeBronze){
            return "Loupe Bronze en " + nbQuestion + "coups !";
        }
        else {
            return "Loupe Argent en " + nbQuestion + "coups !";
        }
    }

    // Getters

    public List<Personnage> getPersonnages() {
        return personnages;
    }

    public List<Lieu> getLieux() {
        return lieux;
    }

    public List<Indice> getIndices() {
        return indices;
    }

    public Personnage getMeurtrier() {
        return meurtrier;
    }

    public Lieu getLieuDuCrime() {
        return lieuDuCrime;
    }

    public Temps getTempsDuCrime() {
        return tempsDuCrime;
    }

    public int getLoupeOr() {
        return loupeOr;
    }

    public int getLoupeBronze() {
        return loupeBronze;
    }
}
