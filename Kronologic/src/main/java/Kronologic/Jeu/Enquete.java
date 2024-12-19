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
            return "loupe Or en " + nbQuestion + " coups !";
        }
        else if (nbQuestion >= loupeBronze){
            return "loupe Bronze en " + nbQuestion + " coups !";
        }
        else {
            return "loupe Argent en " + nbQuestion + " coups !";
        }
    }

    // Méthode permettant de faire une déduction
    public boolean faireDeduction(Lieu lieu, Personnage personnage, Temps temps) {
        if (personnage.getNom().equals(meurtrier.getNom()) && lieu.getId() == lieuMeurtre.getId() && temps.getValeur() == tempsMeurtre.getValeur()) {
            System.out.println("Bravo, votre déduction est correcte !");
            return true;
        } else {
            System.out.println("Désolé, votre déduction est incorrecte.");
            return false;
        }
    }

    // Getter pour chaque attribut
    public int getIdEnquete() {
        return idEnquete;
    }

    public String getNomEnquete() {
        return nomEnquete;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getEnigme() {
        return enigme;
    }

    public int getLoupeOr() {
        return loupeOr;
    }

    public int getLoupeBronze() {
        return loupeBronze;
    }

    public Personnage getMeurtrier() {
        return meurtrier;
    }

    public Lieu getLieuMeurtre() {
        return lieuMeurtre;
    }

    public Temps getTempsMeurtre() {
        return tempsMeurtre;
    }
}
