import Data.JsonReader;
import Kronologic.Elements.Lieu;
import Kronologic.Elements.Temps;
import Kronologic.Enquete;
import Kronologic.Elements.Personnage;
import Kronologic.Indice.Indice;

import java.util.List;

public class Main {
    public static void main(String[] args){
        Enquete enquete = JsonReader.lireEnqueteDepuisJson("data/enquete_base.json");

        List<Personnage> personnages = enquete.getPersonnages();
        for (Personnage personnage : personnages) {
            System.out.println(personnage.getNom());
        }
        List<Lieu> lieux = enquete.getLieux();
        for (Lieu lieu : lieux) {
            System.out.println(lieu.getNom());
        }
        List<Indice> indices = enquete.getIndices();
        for (Indice indice : indices) {
            System.out.println(indice.toString());
        }
        Personnage meurtrier = enquete.getMeurtrier();
        System.out.println(meurtrier.getNom());
        Lieu lieuDuCrime = enquete.getLieuDuCrime();
        System.out.println(lieuDuCrime.getNom());
        Temps tempsDuCrime = enquete.getTempsDuCrime();
        System.out.println(tempsDuCrime.getTemps());
        int loupeOr = enquete.getLoupeOr();
        System.out.println(loupeOr);
        int loupeBronze = enquete.getLoupeBronze();
        System.out.println(loupeBronze);
    }
}