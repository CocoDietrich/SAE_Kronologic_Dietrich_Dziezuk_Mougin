package Kronologic.Jeu.Elements;

public class Realite extends Position {
    private Lieu lieu;
    private Temps temps;
    private Personnage personnage;

    public Realite(Lieu lieu, Temps temps, Personnage personnage) {
        super(lieu, temps, personnage);
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
