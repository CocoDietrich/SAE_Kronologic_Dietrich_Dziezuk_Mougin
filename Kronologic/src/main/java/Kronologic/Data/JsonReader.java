package Kronologic.Data;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Position;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.Jeu.Indice.Indice;
import Kronologic.Jeu.Indice.IndiceTemps;
import org.example.kronologic.Jeu.Elements.*;
import org.example.kronologic.Jeu.Indice.*;
import com.google.gson.*;
import Kronologic.Jeu.Partie;

import java.io.FileReader;
import java.util.*;

public class JsonReader {

    public static Partie lirePartieDepuisJson(String cheminFichier) {
        try {
            // Initialiser Gson pour lire le fichier JSON
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(new FileReader(cheminFichier), JsonObject.class);

            // Charger les lieux
            List<Lieu> lieux = new ArrayList<>();
            Map<String, Lieu> lieuMap = new HashMap<>();
            int lieuId = 1;
            for (String key : jsonObject.keySet()) {
                if (!isMetaKey(key)) {
                    Lieu lieu = new Lieu(key, lieuId++, new ArrayList<>());
                    lieux.add(lieu);
                    lieuMap.put(key, lieu);
                }
            }

            // Charger les personnages
            List<Personnage> personnages = new ArrayList<>();
            Map<String, Personnage> personnageMap = new HashMap<>();
            for (Lieu lieu : lieux) {
                JsonArray lieuData = jsonObject.getAsJsonArray(lieu.getNom());
                for (JsonElement element : lieuData) {
                    JsonObject tempsObj = element.getAsJsonObject();
                    for (String key : tempsObj.keySet()) {
                        if (key.startsWith("Temps")) {
                            JsonArray tempsData = tempsObj.getAsJsonArray(key);
                            for (JsonElement tData : tempsData) {
                                String[] personnagesArray = tData.getAsJsonObject().get("personnages").getAsString().split(",");
                                for (String nomPerso : personnagesArray) {
                                    if (!personnageMap.containsKey(nomPerso)) {
                                        Personnage personnage = new Personnage(nomPerso, new ArrayList<>());
                                        personnages.add(personnage);
                                        personnageMap.put(nomPerso, personnage);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Charger les temps
            List<Temps> tempsList = new ArrayList<>();
            for (int i = 1; i <= 6; i++) {
                tempsList.add(new Temps(i));
            }

            // Charger les indices
            List<Indice> indices = new ArrayList<>();
            for (Lieu lieu : lieux) {
                JsonArray lieuData = jsonObject.getAsJsonArray(lieu.getNom());
                for (JsonElement element : lieuData) {
                    JsonObject tempsObj = element.getAsJsonObject();
                    for (String key : tempsObj.keySet()) {
                        if (key.startsWith("Temps")) {
                            int tempsId = Integer.parseInt(key.split(" ")[1]);
                            Temps temps = tempsList.stream().filter(t -> t.getTemps() == tempsId).findFirst().orElse(null);
                            JsonArray tempsData = tempsObj.getAsJsonArray(key);
                            for (JsonElement tData : tempsData) {
                                JsonObject tDataObj = tData.getAsJsonObject();
                                int nombrePersonnages = tDataObj.get("nombrePersonnages").getAsInt();
                                String personnagePrive = tDataObj.get("personnagePrive").getAsString();
                                IndiceTemps indice = new IndiceTemps(lieu, nombrePersonnages, temps, personnagePrive);
                                indices.add(indice);
                            }
                        }
                    }
                }
            }

            // Charger les positions
            List<Position> positions = new ArrayList<>();
            for (Lieu lieu : lieux) {
                JsonArray lieuData = jsonObject.getAsJsonArray(lieu.getNom());
                for (JsonElement element : lieuData) {
                    JsonObject tempsObj = element.getAsJsonObject();
                    for (String key : tempsObj.keySet()) {
                        if (key.startsWith("Temps")) {
                            int tempsId = Integer.parseInt(key.split(" ")[1]);
                            Temps temps = tempsList.stream().filter(t -> t.getTemps() == tempsId).findFirst().orElse(null);
                            JsonArray tempsData = tempsObj.getAsJsonArray(key);
                            for (JsonElement tData : tempsData) {
                                JsonObject tDataObj = tData.getAsJsonObject();
                                String personnageNom = tDataObj.get("personnagePrive").getAsString();
                                Personnage personnage = personnageMap.get(personnageNom);
                                positions.add(new Position(lieu, temps, personnage));
                            }
                        }
                    }
                }
            }

            // Charger la solution
            JsonObject solution = jsonObject.getAsJsonObject("solution");
            String nomMeurtrier = solution.get("nomPerso").getAsString();
            int idLieuCrime = solution.get("idLieu").getAsInt();
            int tempsCrimeId = solution.get("temps").getAsInt();

            Personnage meurtrier = personnageMap.get(nomMeurtrier);
            Lieu lieuMeurtre = lieux.stream().filter(l -> l.getId() == idLieuCrime).findFirst().orElse(null);
            Temps tempsMeurtre = tempsList.stream().filter(t -> t.getTemps() == tempsCrimeId).findFirst().orElse(null);

            // Créer l'enquête
            Enquete enquete = new Enquete(
                    jsonObject.get("idEnquete").getAsInt(),
                    jsonObject.get("nomEnquete").getAsString(),
                    jsonObject.get("synopsis").getAsString(),
                    jsonObject.get("enigme").getAsString(),
                    jsonObject.get("loupeOr").getAsInt(),
                    jsonObject.get("loupeBronze").getAsInt(),
                    meurtrier,
                    lieuMeurtre,
                    tempsMeurtre
            );

            // Créer le déroulement
            Deroulement deroulement = new Deroulement(positions);

            // Créer le gestionnaire d'indices
            GestionnaireIndices gestionnaireIndices = new GestionnaireIndices(indices);

            // Créer la partie
            return new Partie(enquete, deroulement, gestionnaireIndices);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean isMetaKey(String key) {
        return key.equals("idEnquete") || key.equals("nomEnquete") || key.equals("synopsis") ||
                key.equals("enigme") || key.equals("loupeOr") || key.equals("loupeBronze") ||
                key.equals("solution");
    }
}
