package Kronologic.Jeu.Elements;

public abstract class Position {
    private Lieu lieu;
    private Temps temps;
    private Personnage personnage;
    private int nbPersonnages;
    private final static int NB_MAX_PERSONNAGES = 6;

    public Position(Lieu lieu, Temps temps, Personnage personnage) {
        if (lieu == null || temps == null || personnage == null) {
            throw new IllegalArgumentException("Les paramètres ne peuvent pas être null");
        }
        this.lieu = lieu;
        this.temps = temps;
        this.personnage = personnage;
    }

    // Constructeur pour le pion de nombre
    public Position(Lieu lieu, Temps temps, int nbPersonnages) {
        if (lieu == null || temps == null || ((nbPersonnages < 0) || (nbPersonnages > NB_MAX_PERSONNAGES))){
            throw new IllegalArgumentException("Les paramètres ne peuvent pas être null");
        }
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

    // Setter pour le temps

    public void setTemps(Temps temps) {
        this.temps = temps;
    }
}
