package Kronologic;

import Kronologic.Data.JsonReader;
import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.Jeu.Enquete;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Indice.GestionnaireIndices;
import Kronologic.Jeu.Partie;

import java.util.List;

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

        // Afficher tous les lieux
        List<Lieu> lieux = partie.getElements().getLieux();
        for (Lieu lieu : lieux) {
            System.out.println(lieu.getNom() + " " + lieu.getId());
            for (Lieu lieuAdjacent : lieu.getListeLieuxAdjacents()) {
                System.out.println("  " + lieuAdjacent.getNom() + " " + lieuAdjacent.getId());
            }
        }
    }
}