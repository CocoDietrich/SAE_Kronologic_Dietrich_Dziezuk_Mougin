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

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Note : ");
        sb.append(this.getLieu().getNom());
        sb.append(" - ");
        sb.append(this.getTemps().getValeur());
        sb.append(" - ");
        if (this.getPersonnage() != null) {
            sb.append(this.getPersonnage().getNom());
        } else {
            sb.append(this.getNbPersonnages());
        }
        sb.append(" - ");
        sb.append(this.estAbsence);
        sb.append(" - ");
        sb.append(this.estHypothese);
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (o instanceof Note n) {
            return this.getLieu().equals(n.getLieu())
                    && this.getTemps().equals(n.getTemps())
                    && this.getPersonnage().equals(n.getPersonnage())
                    && this.getNbPersonnages() == n.getNbPersonnages();
        }
        return false;
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
