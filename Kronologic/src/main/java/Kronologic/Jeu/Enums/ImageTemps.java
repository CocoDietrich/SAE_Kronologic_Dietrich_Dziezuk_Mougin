package Kronologic.Jeu.Enums;

import javafx.scene.image.Image;

public enum ImageTemps {
    TEMPS1("file:img/pions_temps/temps1.png"),
    TEMPS2("file:img/pions_temps/temps2.png"),
    TEMPS3("file:img/pions_temps/temps3.png"),
    TEMPS4("file:img/pions_temps/temps4.png"),
    TEMPS5("file:img/pions_temps/temps5.png"),
    TEMPS6("file:img/pions_temps/temps6.png");

    private final String url;
    private Image imageCache;

    ImageTemps(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public Image getImage() {
        if (imageCache == null) {
            imageCache = new Image(url);
        }
        return imageCache;
    }

    public static Image get(int i) {
        ImageTemps[] values = ImageTemps.values();
        if (i < 0 || i >= values.length) {
            throw new IllegalArgumentException("Index hors limite: " + i);
        }
        return values[i].getImage();
    }
}
