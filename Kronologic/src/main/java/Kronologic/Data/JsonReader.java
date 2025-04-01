package Kronologic.Data;

import Kronologic.Jeu.Deroulement;
import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.Jeu.Enquete;
import Kronologic.Jeu.Indice.Indice;
import Kronologic.Jeu.Indice.IndiceTemps;
import Kronologic.Jeu.Elements.*;
import Kronologic.Jeu.Indice.*;
import Kronologic.Jeu.Elements.GestionnairePions;
import com.google.gson.*;
import Kronologic.Jeu.Partie;

import java.io.FileReader;
import java.util.*;

public class JsonReader {

    private static final int TEMPS_MAX = 6;

    /**
     * Lit une partie à partir d'un fichier JSON.
     *
     * @param cheminFichier Le chemin du fichier JSON.
     * @return La partie lue à partir du fichier JSON.
     */
    public static Partie lirePartieDepuisJson(String cheminFichier) {
        try {
            // Initialiser Gson pour lire le fichier JSON
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(new FileReader(cheminFichier), JsonObject.class);

            // Charger les lieux
            List<Lieu> lieux = new ArrayList<>();
            Map<Integer, List<Integer>> lieuxAdjacents = Map.of(
                    1, List.of(2, 3),
                    2, List.of(1, 3),
                    3, List.of(1, 2, 4),
                    4, List.of(3, 5, 6),
                    5, List.of(4, 6),
                    6, List.of(4, 5)
            );

            int lieuId = 1;
            Map<String, Lieu> lieuxMap = new HashMap<>();  // Stocke les lieux par nom

            // Création des lieux
            for (String key : jsonObject.keySet()) {
                if (isMetaKey(key)) {
                    Lieu lieu = new Lieu(key, lieuId++, new ArrayList<>());
                    lieux.add(lieu);
                    lieuxMap.put(key, lieu);  // Associe le nom au lieu
                }
            }

            // Ajout des lieux adjacents
            for (Lieu lieu : lieux) {
                List<Integer> adjacentsIds = lieuxAdjacents.get(lieu.getId());
                if (adjacentsIds != null) {
                    for (int adjId : adjacentsIds) {
                        // Récupérer le lieu adjacent par son ID
                        lieux.stream()
                                .filter(l -> l.getId() == adjId)
                                .findFirst().ifPresent(lieuAdjacent -> lieu.getListeLieuxAdjacents().add(lieuAdjacent));
                    }
                }
            }

            // Charger les temps
            List<Temps> tempsList = new ArrayList<>();
            for (int i = 1; i <= TEMPS_MAX; i++) {
                tempsList.add(new Temps(i));
            }

            // Charger les personnages
            Map<String, Personnage> personnageMap = new HashMap<>();
            List<Personnage> personnages = new ArrayList<>();
            for (String personnageNom : getAllPersonnagesFromJson(jsonObject)) {
                Personnage personnage = new Personnage(personnageNom);
                personnages.add(personnage);
                personnageMap.put(personnageNom, personnage);
            }

            // Charger les positions
            ArrayList<Realite> positions = new ArrayList<>();
            for (Lieu lieu : lieux) {
                JsonArray lieuData = jsonObject.getAsJsonArray(lieu.getNom());
                if (lieuData != null) {
                    for (JsonElement element : lieuData) {
                        JsonObject tempsObj = element.getAsJsonObject();
                        for (String key : tempsObj.keySet()) {
                            if (key.startsWith("Temps")) {
                                int tempsId = Integer.parseInt(key.split(" ")[1]);
                                Temps temps = tempsList.stream().filter(t -> t.getValeur() == tempsId).findFirst().orElse(null);
                                if (temps != null) {
                                    JsonArray tempsData = tempsObj.getAsJsonArray(key);
                                    if (tempsData != null) {
                                        for (JsonElement tData : tempsData) {
                                            JsonObject tDataObj = tData.getAsJsonObject();
                                            String[] personnagesArray = tDataObj.get("personnages").getAsString().split(",");
                                            for (String nomPerso : personnagesArray) {
                                                nomPerso = nomPerso.trim();
                                                if (!nomPerso.isEmpty() && personnageMap.containsKey(nomPerso)) {
                                                    // Ajouter uniquement les positions valides
                                                    positions.add(new Realite(lieu, temps, personnageMap.get(nomPerso)));
                                                }
                                            }
                                        }
                                    }
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
                if (lieuData != null) {
                    for (JsonElement element : lieuData) {
                        JsonObject tempsObj = element.getAsJsonObject();
                        for (String key : tempsObj.keySet()) {
                            if (key.startsWith("Temps")) {
                                int tempsId = Integer.parseInt(key.split(" ")[1]);
                                Temps temps = tempsList.stream().filter(t -> t.getValeur() == tempsId).findFirst().orElse(null);
                                if (temps != null) {
                                    JsonArray tempsData = tempsObj.getAsJsonArray(key);
                                    if (tempsData != null) {
                                        for (JsonElement tData : tempsData) {
                                            JsonObject tDataObj = tData.getAsJsonObject();
                                            int nombrePersonnages = tDataObj.get("nombrePersonnages").getAsInt();
                                            String infoPrive = tDataObj.has("personnagePrive") ? tDataObj.get("personnagePrive").getAsString() : "Rejouer";
                                            indices.add(new IndiceTemps(lieu, nombrePersonnages, temps, infoPrive));
                                        }
                                    }
                                }
                            }
                            if (personnageMap.containsKey(key)) {
                                JsonArray persoData = tempsObj.getAsJsonArray(key);
                                if (persoData != null) {
                                    for (JsonElement pData : persoData) {
                                        JsonObject pDataObj = pData.getAsJsonObject();
                                        int nombrePassages = pDataObj.get("nombrePassages").getAsInt();
                                        int tempsPrive = pDataObj.get("tempsPrive").getAsInt();
                                        indices.add(new IndicePersonnage(lieu, nombrePassages, personnageMap.get(key), tempsPrive));
                                    }
                                }
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
            Temps tempsMeurtre = tempsList.stream().filter(t -> t.getValeur() == tempsCrimeId).findFirst().orElse(null);

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

            GestionnaireIndices gestionnaireIndices = new GestionnaireIndices(indices);
            GestionnaireNotes gestionnaireNotes = new GestionnaireNotes();
            GestionnairePions gestionnairePions = new GestionnairePions();

            // Créer la partie
            return new Partie(enquete, deroulement, gestionnaireIndices, gestionnaireNotes, gestionnairePions, new  Elements(personnages, lieux));

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Récupère tous les personnages à partir de l'objet JSON.
     *
     * @param jsonObject L'objet JSON à partir duquel récupérer les personnages.
     * @return Un ensemble de noms de personnages.
     */
    private static Set<String> getAllPersonnagesFromJson(JsonObject jsonObject) {
        Set<String> personnages = new HashSet<>();
        for (String key : jsonObject.keySet()) {
            if (isMetaKey(key)) {
                JsonArray lieuData = jsonObject.getAsJsonArray(key);
                if (lieuData != null) {
                    for (JsonElement element : lieuData) {
                        JsonObject tempsObj = element.getAsJsonObject();
                        for (String tempsKey : tempsObj.keySet()) {
                            if (tempsKey.startsWith("Temps")) {
                                JsonArray tempsData = tempsObj.getAsJsonArray(tempsKey);
                                if (tempsData != null) {
                                    for (JsonElement tData : tempsData) {
                                        JsonObject tDataObj = tData.getAsJsonObject();
                                        String[] personnagesArray = tDataObj.get("personnages").getAsString().split(",");
                                        for (String personnage : personnagesArray) {
                                            personnage = personnage.trim();
                                            if (!personnage.isEmpty()) {
                                                personnages.add(personnage);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return personnages;
    }

    /**
     * Vérifie si la clé est une clé de métadonnées (non liée à un personnage ou à un lieu).
     *
     * @param key La clé à vérifier.
     * @return true si la clé est une clé de métadonnées, false sinon.
     */
    private static boolean isMetaKey(String key) {
        return !key.equals("idEnquete") && !key.equals("nomEnquete") && !key.equals("synopsis") &&
                !key.equals("enigme") && !key.equals("loupeOr") && !key.equals("loupeBronze") &&
                !key.equals("solution");
    }

}
