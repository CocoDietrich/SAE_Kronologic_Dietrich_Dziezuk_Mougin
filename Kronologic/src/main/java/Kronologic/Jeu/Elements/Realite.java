package Kronologic.Jeu.Elements;

public class Realite extends Position {
    public Realite(Lieu lieu, Temps temps, Personnage personnage) {
        if (lieu == null || temps == null || personnage == null) {
            throw new IllegalArgumentException("Les paramètres ne peuvent pas être null");
        }
        super(lieu, temps, personnage);
    }
}