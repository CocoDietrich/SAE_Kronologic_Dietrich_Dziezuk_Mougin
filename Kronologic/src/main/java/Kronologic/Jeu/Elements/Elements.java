package Kronologic.Jeu.Elements;


import java.util.List;

public class Elements {
    private List<Personnage> personnages;
    private List<Lieu> lieux;

    public Elements(List<Personnage> personnages, List<Lieu> lieux) {
        if (personnages == null || lieux == null) {
            throw new IllegalArgumentException("Les listes de personnages et de lieux ne peuvent pas être nulles");
        } else if (personnages.isEmpty() || lieux.isEmpty()) {
            throw new IllegalArgumentException("Les listes de personnages et de lieux ne peuvent pas être vides");
        }
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
