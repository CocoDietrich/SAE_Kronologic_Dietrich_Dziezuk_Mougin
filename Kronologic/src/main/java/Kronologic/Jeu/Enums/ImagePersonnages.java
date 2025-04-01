package Kronologic.Jeu.Enums;

import javafx.scene.image.Image;

import java.util.List;
import java.util.stream.Stream;

public enum ImagePersonnages {
    PERSONNAGE1("file:img/pions_personnages/Aventurière.png", "Aventurière"),
    PERSONNAGE2("file:img/pions_personnages/Baronne.png", "Baronne"),
    PERSONNAGE3("file:img/pions_personnages/Chauffeur.png", "Chauffeur"),
    PERSONNAGE4("file:img/pions_personnages/Détective.png", "Détective"),
    PERSONNAGE5("file:img/pions_personnages/Journaliste.png", "Journaliste"),
    PERSONNAGE6("file:img/pions_personnages/Servante.png", "Servante");

    private final String url;
    private final String nom;
    private Image imageCache;

    ImagePersonnages(String url, String nom) {
        this.url = url;
        this.nom = nom;
    }

    public String getUrl() {
        return url;
    }

    public String getNom() {
        return nom;
    }

    public static Image get(int i) {
        ImagePersonnages[] values = ImagePersonnages.values();
        if (i < 0 || i >= values.length) {
            throw new IllegalArgumentException("Index hors limite: " + i);
        }
        return values[i].creerImage();
    }

    public Image creerImage() {
        if (imageCache == null) {
            imageCache = new Image(url);
        }
        return imageCache;
    }

    public static List<String> getPersonnages() {
        return Stream.of(values()).map(ImagePersonnages::getNom).toList();
    }
}
