package Kronologic.Jeu;

public class Partie {
    private int nbQuestion;
    private Enquete enquete;
    private Deroulement deroulement;
    private GestionnaireIndices gestionnaireIndices;

    public Partie(Enquete enquete, Deroulement deroulement, GestionnaireIndices gestionnaireIndices) {
        this.enquete = enquete;
        this.deroulement = deroulement;
        this.gestionnaireIndices = gestionnaireIndices;
        this.nbQuestion = 0;
    }

    // Getter et setter pour chaque attribut
}
