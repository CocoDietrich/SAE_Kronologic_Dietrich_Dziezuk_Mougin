package Kronologic.Jeu.Elements;

public class Note extends Position {

    private boolean estAbsence;
    private boolean estHypothese;

    // Constructeur pour les pions de personnage
    public Note(Lieu lieu, Temps temps, Personnage personnage) {
        super(lieu, temps, personnage);
        this.estAbsence = false;
        this.estHypothese = false;
    }

    // Constructeur pour le pion de nombre
    public Note(Lieu lieu, Temps temps, int nbPersonnages) {
        super(lieu, temps, nbPersonnages);
        this.estAbsence = false;
        this.estHypothese = false;
    }

    // Setter pour chaque attribut
    public void setEstAbsence(boolean estAbsence) {
        this.estAbsence = estAbsence;
    }

    public void setEstHypothese(boolean estHypothese) {
        this.estHypothese = estHypothese;
    }

    // Getter pour chaque attribut
    public boolean estAbsence() {
        return estAbsence;
    }

    public boolean estHypothese() {
        return estHypothese;
    }
}
