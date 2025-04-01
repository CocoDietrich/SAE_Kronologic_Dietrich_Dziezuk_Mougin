package Kronologic.Jeu.Enums;

import javafx.scene.image.Image;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public enum ImageLieux {
    LIEU1("file:img/pions_lieux/Lieu_Grand foyer_1.png", "Grand foyer"),
    LIEU2("file:img/pions_lieux/Lieu_Grand escalier_2.png", "Grand escalier"),
    LIEU3("file:img/pions_lieux/Lieu_Salle_3.png", "Salle"),
    LIEU4("file:img/pions_lieux/Lieu_Scène_4.png", "Scène"),
    LIEU5("file:img/pions_lieux/Lieu_Foyer de la danse_5.png", "Foyer de la danse"),
    LIEU6("file:img/pions_lieux/Lieu_Foyer du chant_6.png", "Foyer du chant");

    private final String url;
    private final String nom;
    private Image imageCache;

    ImageLieux(String url, String nom) {
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
        ImageLieux[] values = ImageLieux.values();
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

    public static Map<Integer, String> getLieux() {
        return IntStream.range(0, values().length)
                .boxed()
                .collect(Collectors.toMap(i -> i + 1, i -> values()[i].nom));
    }
}
