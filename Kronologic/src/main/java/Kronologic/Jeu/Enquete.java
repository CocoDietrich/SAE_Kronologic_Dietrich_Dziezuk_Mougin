package Kronologic.Jeu;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;

public class Enquete {
    private int idEnquete;
    private String nomEnquete;
    private String synopsis;
    private String enigme;
    private int loupeOr;
    private int loupeBronze;
    private Personnage meurtrier;
    private Lieu lieuMeurtre;
    private Temps tempsMeurtre;

    public Enquete(int id, String nom, String synopsis, String enigme, int loupeOr, int loupeBronze, Personnage meurtrier, Lieu lieuMeurtre, Temps tempsMeurtre) {
        this.idEnquete = id;
        this.nomEnquete = nom;
        this.synopsis = synopsis;
        this.enigme = enigme;
        this.loupeOr = loupeOr;
        this.loupeBronze = loupeBronze;
        this.meurtrier = meurtrier;
        this.lieuMeurtre = lieuMeurtre;
        this.tempsMeurtre = tempsMeurtre;
    }

    public String verifierLoupe(int nbQuestion){
        if (nbQuestion <= loupeOr){
            return "Loupe Or en " + nbQuestion + "coups !";
        }
        else if (nbQuestion >= loupeBronze){
            return "Loupe Bronze en " + nbQuestion + "coups !";
        }
        else {
            return "Loupe Argent en " + nbQuestion + "coups !";
        }
    }

    // Getter et setter pour chaque attribut
}
