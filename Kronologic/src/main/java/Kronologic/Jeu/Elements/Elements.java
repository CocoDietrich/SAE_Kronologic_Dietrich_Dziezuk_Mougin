package Kronologic.Jeu.Elements;


import java.util.List;

public class Elements {
    private List<Personnage> personnages;
    private List<Lieu> lieux;

    public Elements(List<Personnage> personnages, List<Lieu> lieux) {
        this.personnages = personnages;
        this.lieux = lieux;
    }

    public List<Personnage> getPersonnages() {
        return personnages;
    }

    public List<Lieu> getLieux() {
        return lieux;
    }
}
