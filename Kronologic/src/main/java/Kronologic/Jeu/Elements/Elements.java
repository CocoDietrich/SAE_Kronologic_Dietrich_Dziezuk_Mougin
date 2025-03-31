package Kronologic.Jeu.Elements;


import java.util.List;

public record Elements(List<Personnage> personnages, List<Lieu> lieux) {
    public Elements {
        if (personnages == null || lieux == null) {
            throw new IllegalArgumentException("Les listes de personnages et de lieux ne peuvent pas être nulles");
        } else if (personnages.isEmpty() || lieux.isEmpty()) {
            throw new IllegalArgumentException("Les listes de personnages et de lieux ne peuvent pas être vides");
        }
    }
}
