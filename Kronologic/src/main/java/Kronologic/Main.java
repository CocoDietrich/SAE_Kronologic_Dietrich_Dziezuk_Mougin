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
        System.out.println(partie.poserQuestionPersonnage(new Lieu("Salle", 3, null), new Personnage("Détective")));
        System.out.println(partie.poserQuestionTemps(new Lieu("Salle", 3, null), new Temps(3)));
        partie.faireDeduction(new Lieu("Salle", 3, null), new Personnage("Chauffeur"), new Temps(6));
    }
}