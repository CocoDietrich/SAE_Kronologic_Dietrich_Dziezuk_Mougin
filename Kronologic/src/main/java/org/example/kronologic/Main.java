import Data.JsonReader;
import Kronologic.Elements.Lieu;
import Kronologic.Elements.Temps;
import Kronologic.Enquete;
import Kronologic.Elements.Personnage;
import Kronologic.Indice.Indice;
import Kronologic.Partie;

import java.util.List;

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