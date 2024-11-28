package Kronologic.Jeu.Elements;

public abstract class Position {
    private Lieu lieu;
    private Temps temps;
    private Personnage personnage;

    public Position(Lieu lieu, Temps temps, Personnage personnage) {
        this.lieu = lieu;
        this.temps = temps;
        this.personnage = personnage;
    }

    // Getter pour chaque attribut

    public Lieu getLieu() {
        return lieu;
    }

    public Temps getTemps() {
        return temps;
    }

    public Personnage getPersonnage() {
        return personnage;
    }
}
