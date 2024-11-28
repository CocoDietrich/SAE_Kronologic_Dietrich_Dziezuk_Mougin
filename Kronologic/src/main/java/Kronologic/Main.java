package Kronologic;

import Kronologic.Data.JsonReader;
import Kronologic.Jeu.Deroulement;
import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Position;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.Jeu.Enquete;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Indice.GestionnaireIndices;
import Kronologic.Jeu.Indice.Indice;
import Kronologic.Jeu.Partie;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args){
        Partie partie = JsonReader.lirePartieDepuisJson("data/enquete_base.json");
        assert partie != null;
        Enquete enquete = partie.getEnquete();
        Personnage meurtrier = enquete.getMeurtrier();
        Lieu lieuMeurtre = enquete.getLieuMeurtre();
        Temps tempsMeurtre = enquete.getTempsMeurtre();

        Deroulement deroulement = partie.getDeroulement();
        ArrayList<Position> listePositions = deroulement.positionsAuTemps(tempsMeurtre);
        for (Position position : listePositions){
            System.out.println("Le personnage " + position.getPersonnage().getNom() + " était à " + position.getLieu().getNom() + " au temps " + position.getTemps().getValeur() + ".");
        }

        GestionnaireIndices gestionnaireIndices = partie.getGestionnaireIndices();
        Indice indice = partie.poserQuestion(new Lieu("Salle", 3, null), new Personnage("Détective"), null);
        System.out.println(indice.toString());
    }
}