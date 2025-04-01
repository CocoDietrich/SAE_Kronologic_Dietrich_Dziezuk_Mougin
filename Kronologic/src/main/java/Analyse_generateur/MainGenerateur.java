package Analyse_generateur;

import Kronologic.IA.GenerateurScenarios.GenerateurScenario;
import Kronologic.Jeu.Partie;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainGenerateur {

    public static void main(String[] args) {
        int nombreScenarios = 100;
        List<Long> durees = new ArrayList<>();
        List<Integer> nonConformes = new ArrayList<>();

        StringBuilder csv = new StringBuilder();
        csv.append("Scenario n°,Temps (ms),Nb scenarios non conformes avant\n");

        for (int i = 1; i <= nombreScenarios; i++) {
            System.out.println("Génération du scénario " + i + "...");
            long debut = System.currentTimeMillis();

            GenerateurScenario.genererScenario();
            int nbInvalides = GenerateurScenario.getNbScenariosNonConformes();

            long fin = System.currentTimeMillis();
            long duree = fin - debut;

            durees.add(duree);
            nonConformes.add(nbInvalides);

            csv.append(i).append(";")
                    .append(duree).append(";")
                    .append(nbInvalides).append("\n");

            System.out.println("✅ Scénario " + i + " généré en " + duree + " ms | Scénarios non conformes avant : " + nbInvalides + "\n");
        }

        try (FileWriter writer = new FileWriter("src/main/java/Analyse_generateur/resultats_generation.csv")) {
            writer.write(csv.toString());
            System.out.println("📁 Fichier CSV 'resultats_generation.csv' généré avec succès.");
        } catch (IOException e) {
            System.err.println("❌ Erreur lors de l'écriture du fichier CSV : " + e.getMessage());
        }
    }
}
