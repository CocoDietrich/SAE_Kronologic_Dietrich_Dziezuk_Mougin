package Kronologic.IA.GenerateurScenarios;

import Kronologic.IA.IAAssistance.IAAssistanceChocoSolver;
import Kronologic.IA.IAAssistance.IAAssistanceChocoTriche;
import Kronologic.IA.IADeduction.IADeduction;
import Kronologic.IA.IADeduction.IADeductionChocoSolver;
import Kronologic.IA.IAJoueuse;
import Kronologic.Jeu.Deroulement;
import Kronologic.Jeu.Enquete;
import Kronologic.Jeu.Elements.*;
import Kronologic.Jeu.Enums.ImageLieux;
import Kronologic.Jeu.Indice.*;
import Kronologic.Jeu.Partie;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class GenerateurScenario {

    private static final int NB_LIEUX = 6;
    private static final int NB_TEMPS = 6;

    private static final Random random = new Random();

    /**
     * Génère un scénario de jeu valide.
     *
     * @return Un objet Partie contenant le scénario généré.
     */
    public static Partie genererScenario() {
        while (true) {
            List<Lieu> lieux = creerLieux();
            List<Temps> temps = new ArrayList<>();
            for (int i = 1; i <= NB_TEMPS; i++) temps.add(new Temps(i));

            List<String> noms = List.of("Aventurière", "Baronne", "Chauffeur", "Détective", "Journaliste", "Servante");
            List<Personnage> personnages = noms.stream().map(Personnage::new).collect(Collectors.toList());

            List<Realite> positions = genererTrajetsValides(personnages, lieux, temps);

            Personnage detective = personnages.stream().filter(p -> p.getNom().equals("Détective")).findFirst().orElseThrow();
            Optional<Realite> sceneMeurtre = trouverSceneMeurtre(positions, detective);
            if (sceneMeurtre.isEmpty()) continue;

            Realite crime = sceneMeurtre.get();
            Lieu lieuCrime = crime.getLieu();
            Temps tempsCrime = crime.getTemps();

            if (tempsCrime.getValeur() == 1) continue;

            Personnage meurtrier = crime.getPersonnage().getNom().equals("Détective") ?
                    trouverAutrePersonnagePresent(positions, lieuCrime, tempsCrime, detective) : crime.getPersonnage();

            long momentsSeulAvecUn = temps.stream()
                    .filter(t -> {
                        List<Realite> prs = positions.stream()
                                .filter(r -> r.getTemps().equals(t) && r.getLieu().equals(
                                        positions.stream().filter(x -> x.getPersonnage().equals(detective) && x.getTemps().equals(t))
                                                .map(Realite::getLieu).findFirst().orElse(null)))
                                .toList();
                        return prs.size() == 2 && prs.stream().anyMatch(r -> r.getPersonnage().equals(detective));
                    }).count();
            if (momentsSeulAvecUn != 1) continue;

            List<Indice> indices = genererIndices(lieux, temps, personnages, positions);

            Enquete enquete = new Enquete(2, "Poison Mondain",
                    "17 octobre 1922, au petit matin, Denis, le célèbre Détective est retrouvé mort dans son lit, manifestement empoisonné ! La veille, il enquêtait à l'Opéra de Paris où se déroulait un gala de bienfaisance. Retracez sa soirée ainsi que celle des suspects de cette enquête, pour identifier l'assassin.",
                    "L'assassin a dû se retrouver seul(e) avec le Détective pour l'empoisonner. Qui a empoisonné le Détective ? Dans quel Lieu ? A quel Temps ?",
                    12, 19, meurtrier, lieuCrime, tempsCrime);
            Deroulement deroulement = new Deroulement(new ArrayList<>(positions));

            Partie partie = new Partie(enquete, deroulement,
                    new GestionnaireIndices(indices),
                    new GestionnaireNotes(), new GestionnairePions(),
                    new Elements(personnages, lieux));

            // Vérification par l'IA joueuse
            IADeductionChocoSolver iaDeduction = new IADeductionChocoSolver(partie);
            IAAssistanceChocoSolver iaAssistance = new IAAssistanceChocoTriche(iaDeduction, partie);
            IAJoueuse iaJoueuse = new IAJoueuse(iaAssistance);
            String resultat = iaJoueuse.jouerJusquaTrouverCoupable();
            if (!resultat.contains(meurtrier.getNom()) ||
                    !resultat.contains(lieuCrime.getNom()) ||
                    !resultat.contains(String.valueOf(tempsCrime.getValeur()))) continue;

            // Vérification unicité de la solution
            Set<String> solutionsPossibles = new HashSet<>();
            for (Personnage p : personnages) {
                if (p.equals(detective)) continue;
                for (Lieu l : lieux) {
                    for (Temps t : temps) {
                        long count = positions.stream()
                                .filter(r -> r.getTemps().equals(t) && r.getLieu().equals(l))
                                .count();
                        boolean isSeulAvecDetective = count == 2 &&
                                positions.stream().anyMatch(r -> r.getTemps().equals(t) && r.getLieu().equals(l) && r.getPersonnage().equals(detective)) &&
                                positions.stream().anyMatch(r -> r.getTemps().equals(t) && r.getLieu().equals(l) && r.getPersonnage().equals(p));
                        if (isSeulAvecDetective) {
                            solutionsPossibles.add(p.getNom() + ":" + l.getNom() + ":" + t.getValeur());
                        }
                    }
                }
            }
            if (solutionsPossibles.size() != 1) continue;

            return partie;
        }
    }

    /**
     * Exporte le scénario de jeu au format JSON.
     *
     * @param partie La partie à exporter.
     * @param chemin Le chemin du fichier de sortie.
     * @throws IOException Si une erreur d'entrée/sortie se produit.
     */
    public static void exporterJson(Partie partie, String chemin) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject root = new JsonObject();

        Enquete e = partie.getEnquete();
        root.addProperty("idEnquete", e.getIdEnquete());
        root.addProperty("nomEnquete", e.getNomEnquete());
        root.addProperty("synopsis", e.getSynopsis());
        root.addProperty("enigme", e.getEnigme());
        root.addProperty("loupeOr", e.getLoupeOr());
        root.addProperty("loupeBronze", e.getLoupeBronze());

        Map<String, JsonArray> lieuxData = new LinkedHashMap<>();
        List<Lieu> lieux = partie.getElements().lieux();
        List<Temps> temps = partie.getDeroulement().getListePositions().stream().map(Realite::getTemps).distinct().toList();
        List<Personnage> persos = partie.getElements().personnages();

        for (Lieu l : lieux) {
            JsonObject bloc = new JsonObject();
            for (Temps t : temps) {
                List<Realite> prs = partie.getDeroulement().positionsDansLieu(l).stream()
                        .filter(r -> r.getTemps().getValeur() == t.getValeur()).toList();
                JsonArray arr = new JsonArray();
                JsonObject obj = new JsonObject();
                obj.addProperty("nombrePersonnages", prs.size());
                obj.addProperty("personnagePrive", prs.isEmpty() ? "Rejouer" : prs.get(random.nextInt(prs.size())).getPersonnage().getNom());
                obj.addProperty("personnages", prs.stream().map(p -> p.getPersonnage().getNom()).collect(Collectors.joining(",")));
                arr.add(obj);
                bloc.add("Temps " + t.getValeur(), arr);
            }
            for (Personnage p : persos) {
                List<Realite> passes = partie.getDeroulement().positionsDuPersonnage(p).stream()
                        .filter(r -> r.getLieu().equals(l)).toList();
                JsonArray arr = new JsonArray();
                JsonObject obj = new JsonObject();
                obj.addProperty("nombrePassages", passes.size());
                obj.addProperty("tempsPrive", passes.isEmpty() ? 0 : passes.getFirst().getTemps().getValeur());
                obj.addProperty("temps", passes.stream().map(x -> String.valueOf(x.getTemps().getValeur())).collect(Collectors.joining(",")));
                arr.add(obj);
                bloc.add(p.getNom(), arr);
            }
            JsonArray wrapper = new JsonArray();
            wrapper.add(bloc);
            lieuxData.put(l.getNom(), wrapper);
        }
        lieuxData.forEach(root::add);

        JsonObject solution = new JsonObject();
        solution.addProperty("nomPerso", e.getMeurtrier().getNom());
        solution.addProperty("idLieu", e.getLieuMeurtre().getId());
        solution.addProperty("temps", e.getTempsMeurtre().getValeur());
        root.add("solution", solution);

        try (FileWriter writer = new FileWriter(chemin)) {
            gson.toJson(root, writer);
        }
    }

    /**
     * Crée une liste de lieux avec leurs adjacences.
     *
     * @return Une liste de lieux.
     */
    private static List<Lieu> creerLieux() {
        Map<Integer, List<Integer>> adjacences = Map.of(
                1, List.of(2, 3),
                2, List.of(1, 3),
                3, List.of(1, 2, 4),
                4, List.of(3, 5, 6),
                5, List.of(4, 6),
                6, List.of(4, 5)
        );

        List<Lieu> lieux = new ArrayList<>();
        for (int i = 1; i <= NB_LIEUX; i++) {
            String nom = ImageLieux.getLieux().get(i);
            lieux.add(new Lieu(nom, i, new ArrayList<>()));
        }
        for (Lieu lieu : lieux) {
            List<Lieu> adj = adjacences.get(lieu.getId()).stream()
                    .map(id -> lieux.get(id - 1)).toList();
            lieu.getListeLieuxAdjacents().addAll(adj);
        }
        return lieux;
    }

    /**
     * Génère des trajets valides pour les personnages.
     *
     * @param persos La liste des personnages.
     * @param lieux  La liste des lieux.
     * @param temps  La liste des temps.
     * @return Une liste de réalités représentant les trajets.
     */
    private static List<Realite> genererTrajetsValides(List<Personnage> persos, List<Lieu> lieux, List<Temps> temps) {
        List<Realite> resultats = new ArrayList<>();
        for (Personnage p : persos) {
            List<Lieu> parcours = new ArrayList<>();
            Lieu actuel = lieux.get(random.nextInt(lieux.size()));
            parcours.add(actuel);
            for (int i = 1; i < temps.size(); i++) {
                Lieu finalActuel = actuel;
                List<Lieu> adjacents = actuel.getListeLieuxAdjacents().stream()
                        .filter(l -> !l.equals(finalActuel)).toList();
                actuel = adjacents.get(random.nextInt(adjacents.size()));
                parcours.add(actuel);
            }
            for (int i = 0; i < temps.size(); i++) {
                resultats.add(new Realite(parcours.get(i), temps.get(i), p));
            }
        }
        return resultats;
    }

    /**
     * Trouve la scène de meurtre dans la liste des réalités.
     *
     * @param realites  La liste des réalités.
     * @param detective Le personnage détective.
     * @return Une option contenant la réalité de la scène de meurtre, ou vide si non trouvée.
     */
    private static Optional<Realite> trouverSceneMeurtre(List<Realite> realites, Personnage detective) {
        return realites.stream()
                .filter(r -> r.getPersonnage().equals(detective))
                .filter(r -> {
                    long nbPersos = realites.stream()
                            .filter(x -> x.getTemps().equals(r.getTemps()) && x.getLieu().equals(r.getLieu()))
                            .count();
                    return nbPersos == 2;
                }).findFirst();
    }

    /**
     * Trouve un autre personnage présent dans le même lieu et temps, différent de celui donné.
     *
     * @param realites La liste des réalités.
     * @param lieu     Le lieu à vérifier.
     * @param temps    Le temps à vérifier.
     * @param except   Le personnage à exclure.
     * @return Le personnage trouvé.
     */
    private static Personnage trouverAutrePersonnagePresent(List<Realite> realites, Lieu lieu, Temps temps, Personnage except) {
        return realites.stream()
                .filter(r -> r.getLieu().equals(lieu) && r.getTemps().equals(temps))
                .map(Realite::getPersonnage)
                .filter(p -> !p.equals(except))
                .findFirst().orElseThrow();
    }

    /**
     * Génère des indices basés sur les lieux, temps, personnages et réalités.
     *
     * @param lieux    La liste des lieux.
     * @param temps    La liste des temps.
     * @param persos   La liste des personnages.
     * @param realites La liste des réalités.
     * @return Une liste d'indices générés.
     */
    private static List<Indice> genererIndices(List<Lieu> lieux, List<Temps> temps, List<Personnage> persos, List<Realite> realites) {
        List<Indice> indices = new ArrayList<>();
        for (Lieu l : lieux) {
            for (Temps t : temps) {
                List<Realite> present = realites.stream()
                        .filter(r -> r.getLieu().equals(l) && r.getTemps().equals(t))
                        .toList();
                int nb = present.size();
                String infoPriv = (nb == 0) ? "Rejouer" : present.get(random.nextInt(nb)).getPersonnage().getNom();
                indices.add(new IndiceTemps(l, nb, t, infoPriv));
            }
            for (Personnage p : persos) {
                List<Realite> passages = realites.stream()
                        .filter(r -> r.getLieu().equals(l) && r.getPersonnage().equals(p))
                        .toList();
                int nb = passages.size();
                int priv = nb == 0 ? 0 : passages.get(random.nextInt(nb)).getTemps().getValeur();
                indices.add(new IndicePersonnage(l, nb, p, priv));
            }
        }
        return indices;
    }
}
