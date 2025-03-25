package IA;

import Kronologic.IA.GenerateurScenarios.GenerateurScenario;
import Kronologic.Jeu.Elements.*;
import Kronologic.Jeu.Partie;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class GenerateurScenarioTest {

    @RepeatedTest(20)
    public void testScenarioValide() {
        Partie partie = GenerateurScenario.genererScenario();
        List<Realite> realites = partie.getDeroulement().getListePositions();
        List<Personnage> persos = partie.getElements().getPersonnages();
        List<Lieu> lieux = partie.getElements().getLieux();
        List<Temps> temps = new ArrayList<>();
        for (int i = 1; i <= 6; i++) temps.add(new Temps(i));

        // 1. Chaque personnage doit avoir exactement une position par temps
        for (Personnage p : persos) {
            long count = realites.stream().filter(r -> r.getPersonnage().equals(p)).count();
            assertEquals(6, count, "Chaque personnage doit avoir 6 positions : " + p.getNom());

            Map<Integer, Lieu> positions = new HashMap<>();
            for (Realite r : realites) {
                if (r.getPersonnage().equals(p)) {
                    int t = r.getTemps().getValeur();
                    assertFalse(positions.containsKey(t), "Doublon au temps " + t);
                    positions.put(t, r.getLieu());
                }
            }
        }

        // 2. Aucun personnage ne reste dans le même lieu deux temps consécutifs
        for (Personnage p : persos) {
            List<Realite> trajets = realites.stream()
                    .filter(r -> r.getPersonnage().equals(p))
                    .sorted(Comparator.comparingInt(r -> r.getTemps().getValeur()))
                    .toList();
            for (int i = 0; i < trajets.size() - 1; i++) {
                Lieu actuel = trajets.get(i).getLieu();
                Lieu suivant = trajets.get(i + 1).getLieu();
                assertNotEquals(actuel, suivant, "Un personnage reste sur place : " + p.getNom() + " au temps " + (i+1));
            }
        }

        // 3. Chaque déplacement est vers un lieu adjacent
        for (Personnage p : persos) {
            List<Realite> trajets = realites.stream()
                    .filter(r -> r.getPersonnage().equals(p))
                    .sorted(Comparator.comparingInt(r -> r.getTemps().getValeur()))
                    .toList();
            for (int i = 0; i < trajets.size() - 1; i++) {
                Lieu actuel = trajets.get(i).getLieu();
                Lieu suivant = trajets.get(i + 1).getLieu();
                assertTrue(actuel.getListeLieuxAdjacents().contains(suivant),
                        "Déplacement non valide de " + p.getNom() + " de " + actuel.getNom() + " vers " + suivant.getNom());
            }
        }

        // 4. Scène du meurtre : le meurtrier et le détective sont seuls
        Personnage detective = persos.stream().filter(p -> p.getNom().equals("Détective")).findFirst().orElseThrow();
        Lieu lieuCrime = partie.getEnquete().getLieuMeurtre();
        Temps tempsCrime = partie.getEnquete().getTempsMeurtre();

        List<Realite> presents = realites.stream()
                .filter(r -> r.getLieu().equals(lieuCrime) && r.getTemps().equals(tempsCrime))
                .toList();

        assertEquals(2, presents.size(), "Il doit y avoir exactement 2 personnes au moment du meurtre");
        assertTrue(presents.stream().anyMatch(r -> r.getPersonnage().equals(detective)), "Le détective doit être présent");
        assertTrue(presents.stream().anyMatch(r -> r.getPersonnage().equals(partie.getEnquete().getMeurtrier())), "Le meurtrier doit être présent");
    }

    @Test
    public void testCreer20ScenariosSansErreur() {
        for (int i = 0; i < 20; i++) {
            assertDoesNotThrow(GenerateurScenario::genererScenario, "Erreur lors de la génération du scénario " + (i + 1));
        }
    }
}
