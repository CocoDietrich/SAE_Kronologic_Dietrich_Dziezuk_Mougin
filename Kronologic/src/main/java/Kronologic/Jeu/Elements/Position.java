package Kronologic.Jeu.Elements;

public class Position {
    private Lieu lieu;
    private Temps temps;
    private Personnage personnage;

    public Position(Lieu lieu, Temps temps, Personnage personnage) {
        this.lieu = lieu;
        this.temps = temps;
        this.personnage = personnage;
    }

    // Getter et setter pour chaque attribut
}
