package Kronologic.MVC.Vue;

import javafx.scene.image.Image;

public class Images {

    public enum Temps{
        TEMPS1("file:img/temps1.png"),
        TEMPS2("file:img/temps2.png"),
        TEMPS3("file:img/temps3.png"),
        TEMPS4("file:img/temps4.png"),
        TEMPS5("file:img/temps5.png"),
        TEMPS6("file:img/temps6.png");

        private final String url;

        // Constructeur de l'enum
        Temps(String url) {
            this.url = url;
        }

        // Méthode pour récupérer l'URL
        public String getUrl() {
            return url;
        }

        // Méthode pour obtenir le ième élément de l'enum
        public static Image get(int i) {
            return switch (i) {
                case 0 -> TEMPS1.creerImage();
                case 1 -> TEMPS2.creerImage();
                case 2 -> TEMPS3.creerImage();
                case 3 -> TEMPS4.creerImage();
                case 4 -> TEMPS5.creerImage();
                case 5 -> TEMPS6.creerImage();
                default -> null;
            };
        }

        // Méthode pour créer l'image
        public Image creerImage(){
            return new Image(url);
        }
    }

    public enum Lieu{
        LIEU1("file:img/Lieu_Grand foyer_1.png"),
        LIEU2("file:img/Lieu_Grand escalier_2.png"),
        LIEU3("file:img/Lieu_Salle_3.png"),
        LIEU4("file:img/Lieu_Scène_4.png"),
        LIEU5("file:img/Lieu_Foyer de la danse_5.png"),
        LIEU6("file:img/Lieu_Foyer du chant_6.png");

        private final String url;

        // Constructeur de l'enum
        Lieu(String url) {
            this.url = url;
        }

        public static Image get(int i) {
            return switch (i) {
                case 0 -> LIEU1.creerImage();
                case 1 -> LIEU2.creerImage();
                case 2 -> LIEU3.creerImage();
                case 3 -> LIEU4.creerImage();
                case 4 -> LIEU5.creerImage();
                case 5 -> LIEU6.creerImage();
                default -> null;
            };
        }

        // Méthode pour récupérer l'URL
        public String getUrl() {
            return url;
        }

        // Méthode pour créer l'image
        public Image creerImage(){
            return new Image(url);
        }
    }
}