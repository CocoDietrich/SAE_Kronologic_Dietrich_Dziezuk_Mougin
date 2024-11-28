package Kronologic.Jeu.Elements;

public class Realite {
    private Lieu lieu;
    private Temps temps;
    private Personnage personnage;

    public Realite(Lieu lieu, Temps temps, Personnage personnage) {
        this.lieu = lieu;
        this.temps = temps;
        this.personnage = personnage;
    }

    // Getter et setter pour chaque attribut
}
