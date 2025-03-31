package Kronologic.Jeu;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;

public class Enquete {
    private final int idEnquete;
    private final String nomEnquete;
    private final String synopsis;
    private final String enigme;
    private final int loupeOr;
    private final int loupeBronze;
    private final Personnage meurtrier;
    private final Lieu lieuMeurtre;
    private final Temps tempsMeurtre;

    public Enquete(int id, String nom, String synopsis, String enigme, int loupeOr, int loupeBronze, Personnage meurtrier, Lieu lieuMeurtre, Temps tempsMeurtre) {
        if (id < 0) {
            throw new IllegalArgumentException("L'id de l'enquête ne peut pas être négatif");
        }
        if (nom == null || nom.isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'enquête invalide");
        }
        if (synopsis == null || synopsis.isEmpty()) {
            throw new IllegalArgumentException("Le synopsis de l'enquête invalide");
        }
        if (enigme == null || enigme.isEmpty()) {
            throw new IllegalArgumentException("L'enigme de l'enquête invalide");
        }
        if (loupeOr < 0) {
            throw new IllegalArgumentException("Le nombre de coups pour la loupe Or ne peut pas être négatif");
        }
        if (loupeBronze < 0) {
            throw new IllegalArgumentException("Le nombre de coups pour la loupe Bronze ne peut pas être négatif");
        }
        if (meurtrier == null) {
            throw new IllegalArgumentException("Le meurtrier ne peut pas être nul");
        }
        if (lieuMeurtre == null) {
            throw new IllegalArgumentException("Le lieu du meurtre ne peut pas être nul");
        }
        if (tempsMeurtre == null) {
            throw new IllegalArgumentException("Le temps du meurtre ne peut pas être nul");
        }
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
        if (nbQuestion < 0){
            throw new IllegalArgumentException("Le nombre de coups ne peut pas être négatif");
        }
        if (nbQuestion <= loupeOr){
            return "loupe Or en " + nbQuestion + " coups !";
        }
        else if (nbQuestion >= loupeBronze){
            return "loupe Bronze en " + nbQuestion + " coups !";
        }
        else{
            return "loupe Argent en " + nbQuestion + " coups !";
        }
    }

    // Méthode permettant de faire une déduction
    public boolean faireDeduction(Lieu lieu, Personnage personnage, Temps temps) {
        if (lieu == null || personnage == null || temps == null) {
            throw new NullPointerException("Le lieu ne peut pas être nul");
        }
        if (personnage.getNom().equals(meurtrier.getNom())
                && lieu.getId() == lieuMeurtre.getId()
                && temps.getValeur() == tempsMeurtre.getValeur()) {
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
