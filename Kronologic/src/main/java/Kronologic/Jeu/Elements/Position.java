package Kronologic.Jeu.Elements;

public abstract class Position {
    private Lieu lieu;
    private Temps temps;
    private Personnage personnage;
    private int nbPersonnages;

    public Position(Lieu lieu, Temps temps, Personnage personnage) {
        this.lieu = lieu;
        this.temps = temps;
        this.personnage = personnage;
    }

    // Constructeur pour le pion de nombre
    public Position(Lieu lieu, Temps temps, int nbPersonnages) {
        this.lieu = lieu;
        this.temps = temps;
        this.nbPersonnages = nbPersonnages;
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

    public int getNbPersonnages() {
        return nbPersonnages;
    }

    // Setter pour le lieu

    public void setLieu(Lieu lieu) {
        this.lieu = lieu;
    }
}
