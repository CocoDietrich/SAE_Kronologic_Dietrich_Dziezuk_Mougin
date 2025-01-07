package Kronologic.Jeu.Elements;

public class Realite extends Position {
    public Realite(Lieu lieu, Temps temps, Personnage personnage) {
        super(lieu, temps, personnage);
    }

    public String toString() {
        return getPersonnage().getNom() + " - "
                + getLieu().getNom() + " - "
                + getTemps().getValeur();
    }
}