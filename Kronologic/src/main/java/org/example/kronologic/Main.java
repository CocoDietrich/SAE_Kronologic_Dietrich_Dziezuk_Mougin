package org.example.kronologic;

import org.example.kronologic.Data.JsonReader;
import org.example.kronologic.Kronologic.Elements.Lieu;
import org.example.kronologic.Kronologic.Elements.Temps;
import org.example.kronologic.Kronologic.Enquete;
import org.example.kronologic.Kronologic.Elements.Personnage;
import org.example.kronologic.Kronologic.Indice.Indice;
import org.example.kronologic.Kronologic.Partie;

public class Main {
    public static void main(String[] args){
        Enquete enquete = JsonReader.lireEnqueteDepuisJson("data/enquete_base.json");

        Partie partie = new Partie(enquete);
        Indice i = partie.poserQuestion(new Lieu("Salle", 3, null), new Personnage("Détective", null), null);
        System.out.println(i);
        Indice i2 = partie.poserQuestion(new Lieu("Salle", 3, null), null, new Temps(2));
        System.out.println(i2);
    }
}