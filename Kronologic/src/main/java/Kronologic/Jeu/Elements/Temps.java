package Kronologic.Jeu.Elements;

public class Temps {

    private final int valeur;

    public Temps(int t) {
        if (t < 0) {
            throw new IllegalArgumentException("Le temps ne peut pas être négatif");
        }
        else if (t > 6) {
            throw new IllegalArgumentException("Le temps ne peut pas être supérieur à 6");
        } else if (t == 0) {
            throw new NullPointerException("Le temps ne peut pas être nul");
        }
        this.valeur = t;
    }

    public int getValeur() {
        return valeur;
    }

}
