package org.example.kronologic.Data;

import org.example.kronologic.Kronologic.Elements.*;
import org.example.kronologic.Kronologic.Enquete;
import org.example.kronologic.Kronologic.Indice.*;
import com.google.gson.*;
import java.io.FileReader;
import java.util.*;

public class JsonReader {

    public static Enquete lireEnqueteDepuisJson(String cheminFichier) {
        try {
            // Initialiser Gson et lire le fichier JSON
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(new FileReader(cheminFichier), JsonObject.class);

            // Charger les lieux
            List<Lieu> lieux = new ArrayList<>();
            for (String key : jsonObject.keySet()) {
                if (!key.equals("idEnquete") && !key.equals("nomEnquete") && !key.equals("synopsis") &&
                        !key.equals("enigme") && !key.equals("loupeOr") && !key.equals("loupeBronze") &&
                        !key.equals("solution")) {
                    lieux.add(new Lieu(key, lieux.size() + 1, new ArrayList<>()));
                }
            }

            // Charger les personnages
            List<Personnage> personnages = new ArrayList<>();
            Set<String> nomsPersonnages = new HashSet<>();
            for (Lieu lieu : lieux) {
                JsonArray lieuData = jsonObject.getAsJsonArray(lieu.getNom());
                for (JsonElement element : lieuData) {
                    JsonObject tempsObj = element.getAsJsonObject();
                    for (String key : tempsObj.keySet()) {
                        if (key.startsWith("Temps")) {
                            String[] persos = tempsObj.getAsJsonArray(key)
                                    .get(0).getAsJsonObject()
                                    .get("personnages").getAsString().split(",");
                            for (String perso : persos) {
                                if (!perso.isEmpty() && nomsPersonnages.add(perso)) {
                                    personnages.add(new Personnage(perso, new ArrayList<>()));
                                }
                            }
                        }
                    }
                }
            }

            // Charger les indices
            List<Indice> indices = new ArrayList<>();
            for (Lieu lieu : lieux) {
                JsonArray lieuData = jsonObject.getAsJsonArray(lieu.getNom());
                for (JsonElement element : lieuData) {
                    JsonObject tempsObj = element.getAsJsonObject();
                    for (String key : tempsObj.keySet()) {
                        if (key.startsWith("Temps")) {
                            JsonObject indiceData = tempsObj.getAsJsonArray(key).get(0).getAsJsonObject();
                            int nombrePersonnages = indiceData.get("nombrePersonnages").getAsInt();
                            String personnagePrive = indiceData.get("personnagePrive").getAsString();
                            indices.add(new IndiceTemps(
                                    lieu,
                                    nombrePersonnages,
                                    new Temps(Integer.parseInt(key.split(" ")[1])),
                                    personnagePrive
                            ));
                        } else {
                            JsonObject indiceData = tempsObj.getAsJsonArray(key).get(0).getAsJsonObject();
                            int nombrePassages = indiceData.get("nombrePassages").getAsInt();
                            int tempsPrive = indiceData.get("tempsPrive").getAsInt();
                            indices.add(new IndicePersonnage(
                                    lieu,
                                    nombrePassages,
                                    key,
                                    tempsPrive
                            ));
                        }
                    }
                }
            }

            // Charger la solution
            JsonObject solution = jsonObject.getAsJsonObject("solution");
            Personnage meurtrier = personnages.stream()
                    .filter(p -> p.getNom().equals(solution.get("nomPerso").getAsString()))
                    .findFirst().orElse(null);
            Lieu lieuDuCrime = lieux.stream()
                    .filter(l -> l.getId() == solution.get("idLieu").getAsInt())
                    .findFirst().orElse(null);
            Temps tempsDuCrime = new Temps(solution.get("temps").getAsInt());

            // Créer l'enquête
            return new Enquete(
                    personnages,
                    lieux,
                    indices,
                    meurtrier,
                    lieuDuCrime,
                    tempsDuCrime,
                    jsonObject.get("loupeOr").getAsInt(),
                    jsonObject.get("loupeBronze").getAsInt()
            );

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
