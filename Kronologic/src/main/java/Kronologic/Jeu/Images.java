package Kronologic.Jeu;

import javafx.scene.image.Image;

import java.util.List;
import java.util.Map;

public class Images {

    public enum Temps{
        TEMPS1("file:img/pions_temps/temps1.png"),
        TEMPS2("file:img/pions_temps/temps2.png"),
        TEMPS3("file:img/pions_temps/temps3.png"),
        TEMPS4("file:img/pions_temps/temps4.png"),
        TEMPS5("file:img/pions_temps/temps5.png"),
        TEMPS6("file:img/pions_temps/temps6.png");

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

    public enum Lieux {
        LIEU1("file:img/pions_lieux/Lieu_Grand foyer_1.png"),
        LIEU2("file:img/pions_lieux/Lieu_Grand escalier_2.png"),
        LIEU3("file:img/pions_lieux/Lieu_Salle_3.png"),
        LIEU4("file:img/pions_lieux/Lieu_Scène_4.png"),
        LIEU5("file:img/pions_lieux/Lieu_Foyer de la danse_5.png"),
        LIEU6("file:img/pions_lieux/Lieu_Foyer du chant_6.png");

        private final String url;

        // Constructeur de l'enum
        Lieux(String url) {
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

        public static String toString(int i) {
            return switch (i) {
                case 0 -> "Grand foyer";
                case 1 -> "Grand escalier";
                case 2 -> "Salle";
                case 3 -> "Scène";
                case 4 -> "Foyer de la danse";
                case 5 -> "Foyer du chant";
                default -> null;
            };
        }

        public static Map<Integer, String> getLieux() {
            return Map.of(
                    1, "Grand foyer",
                    2, "Grand escalier",
                    3, "Salle",
                    4, "Scène",
                    5, "Foyer de la danse",
                    6, "Foyer du chant"
            );
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

    public enum Personnages {
        PERSONNAGE1("file:img/pions_personnages/Aventurière.png"),
        PERSONNAGE2("file:img/pions_personnages/Baronne.png"),
        PERSONNAGE3("file:img/pions_personnages/Chauffeur.png"),
        PERSONNAGE4("file:img/pions_personnages/Détective.png"),
        PERSONNAGE5("file:img/pions_personnages/Journaliste.png"),
        PERSONNAGE6("file:img/pions_personnages/Servante.png");

        private final String url;

        // Constructeur de l'enum
        Personnages(String url) {
            this.url = url;
        }

        public static Image get(int i) {
            return switch (i) {
                case 0 -> PERSONNAGE1.creerImage();
                case 1 -> PERSONNAGE2.creerImage();
                case 2 -> PERSONNAGE3.creerImage();
                case 3 -> PERSONNAGE4.creerImage();
                case 4 -> PERSONNAGE5.creerImage();
                case 5 -> PERSONNAGE6.creerImage();
                default -> null;
            };
        }

        public static String toString(int i) {
            return switch (i) {
                case 0 -> "Aventurière";
                case 1 -> "Baronne";
                case 2 -> "Chauffeur";
                case 3 -> "Détective";
                case 4 -> "Journaliste";
                case 5 -> "Servante";
                default -> null;
            };
        }

        public static List<String> getPersonnages() {
            return List.of("Aventurière", "Baronne", "Chauffeur", "Détective", "Journaliste", "Servante");
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

    public enum Nombre {
        NOMBREX("file:img/pions_nombres/Pion de Nombres.png"),
        NOMBRE0("file:img/pions_nombres/Pion de Nombres_0.png"),
        NOMBRE1("file:img/pions_nombres/Pion de Nombres_1.png"),
        NOMBRE2("file:img/pions_nombres/Pion de Nombres_2.png"),
        NOMBRE3("file:img/pions_nombres/Pion de Nombres_3.png"),
        NOMBRE4("file:img/pions_nombres/Pion de Nombres_4.png"),
        NOMBRE5("file:img/pions_nombres/Pion de Nombres_5.png");

        private final String url;

        // Constructeur de l'enum
        Nombre(String url) {
            this.url = url;
        }

        public static Image get(int i) {
            return switch (i) {
                case 0 -> NOMBREX.creerImage();
                case 1 -> NOMBRE0.creerImage();
                case 2 -> NOMBRE1.creerImage();
                case 3 -> NOMBRE2.creerImage();
                case 4 -> NOMBRE3.creerImage();
                case 5 -> NOMBRE4.creerImage();
                case 6 -> NOMBRE5.creerImage();
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
