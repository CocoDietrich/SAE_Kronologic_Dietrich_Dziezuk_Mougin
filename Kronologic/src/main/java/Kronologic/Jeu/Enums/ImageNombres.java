package Kronologic.Jeu.Enums;

import javafx.scene.image.Image;

import java.util.List;
import java.util.stream.IntStream;

public enum ImageNombres {
    NOMBREX("file:img/pions_nombres/Pion de Nombres.png"),
    NOMBRE0("file:img/pions_nombres/Pion de Nombres_0.png"),
    NOMBRE1("file:img/pions_nombres/Pion de Nombres_1.png"),
    NOMBRE2("file:img/pions_nombres/Pion de Nombres_2.png"),
    NOMBRE3("file:img/pions_nombres/Pion de Nombres_3.png"),
    NOMBRE4("file:img/pions_nombres/Pion de Nombres_4.png"),
    NOMBRE5("file:img/pions_nombres/Pion de Nombres_5.png");

    private final String url;
    private Image imageCache;

    ImageNombres(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public static Image get(int i) {
        ImageNombres[] values = ImageNombres.values();
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

    public static List<Integer> getNombres() {
        return IntStream.range(0, values().length).boxed().toList();
    }
}
