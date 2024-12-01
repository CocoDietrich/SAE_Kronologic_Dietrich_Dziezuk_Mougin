package Kronologic;

import Kronologic.Data.JsonReader;
import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.Jeu.Enquete;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Indice.GestionnaireIndices;
import Kronologic.Jeu.Partie;

public class Main {
    public static void main(String[] args){
        Partie partie = JsonReader.lirePartieDepuisJson("data/enquete_base.json");
        assert partie != null;
        Enquete enquete = partie.getEnquete();
        Personnage meurtrier = enquete.getMeurtrier();
        Lieu lieuMeurtre = enquete.getLieuMeurtre();
        Temps tempsMeurtre = enquete.getTempsMeurtre();

        GestionnaireIndices gestionnaireIndices = partie.getGestionnaireIndices();
        // Afficher tous les indices
        System.out.println(gestionnaireIndices.getListeIndices());
    }
}