package Kronologic.Jeu.Elements;

import Kronologic.Jeu.Images;

import java.util.List;

public class Personnage {

    private final String nom;

    public Personnage(String n) {
        List<String> personnages = new java.util.ArrayList<>(Images.Personnages.getPersonnages());
        for (String personnage : personnages) {
            personnages.set(personnages.indexOf(personnage), String.valueOf(personnage.charAt(0)));
        }
        if (n == null) {
            throw new IllegalArgumentException("Le nom ne peut pas être null");
        } else if (n.isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide");
        } else if (!Images.Personnages.getPersonnages().contains(n)
                && !personnages.contains(String.valueOf(n.charAt(0)))) {
            throw new IllegalArgumentException("Le nom doit être un nom de personnage");
        }
        this.nom = n;
    }

    public String getNom() {
        return nom;
    }
}
