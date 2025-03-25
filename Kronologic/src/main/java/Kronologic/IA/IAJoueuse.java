package Kronologic.IA;

import Kronologic.IA.IAAssistance.IAAssistance;
import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.Jeu.Partie;

public class IAJoueuse {
    private final IAAssistance iaAssistance;
    private final Partie partie;

    public IAJoueuse(IAAssistance iaAssistance, Partie partie) {
        this.iaAssistance = iaAssistance;
        this.partie = partie;
    }

    public void jouerJusquaTrouverCoupable() {
        int tour = 1;

        while (true) {
            System.out.println("🔁 Tour " + tour);

            String[] question = iaAssistance.recommanderQuestionOptimaleTrichePas();

            if (question[0].startsWith("Lieu :") && question[1].startsWith("Temps :")) {
                // Question temps
                String nomLieu = question[0].substring("Lieu : ".length()).trim();
                int valeurTemps = Integer.parseInt(question[1].substring("Temps : ".length()).trim());

                Lieu lieu = getLieuParNom(nomLieu);
                Temps temps = new Temps(valeurTemps);

                partie.poserQuestionTemps(lieu, temps);
                System.out.printf("📌 Question posée : Lieu=%s, Temps=%d\n", nomLieu, valeurTemps);

            } else if (question[0].startsWith("Lieu :") && question[1].startsWith("Personnage :")) {
                // Question personnage
                String nomLieu = question[0].substring("Lieu : ".length()).trim();
                String nomPerso = question[1].substring("Personnage : ".length()).trim();

                Lieu lieu = getLieuParNom(nomLieu);
                Personnage personnage = getPersonnageParNom(nomPerso);

                partie.poserQuestionPersonnage(lieu, personnage);
                System.out.printf("📌 Question posée : Lieu=%s, Personnage=%s\n", nomLieu, nomPerso);
            } else {
                System.out.println("❌ Aucune nouvelle question possible.");
                break;
            }

            // Vérification de la déduction : a-t-on trouvé le bon triplet ?
            if (verifierDeduction()) {
                System.out.println("🎯 Coupable trouvé après " + tour + " questions !");
                break;
            }

            tour++;
        }
    }

    private boolean verifierDeduction() {
        return partie.getGestionnaireNotes().getNotes().stream().anyMatch(note ->
                note.getPersonnage() != null
                        && note.getPersonnage().getNom().equals(partie.getEnquete().getMeurtrier().getNom())
                        && note.getLieu().getId() == partie.getEnquete().getLieuMeurtre().getId()
                        && note.getTemps().getValeur() == partie.getEnquete().getTempsMeurtre().getValeur()
        );
    }

    private Lieu getLieuParNom(String nom) {
        return partie.getElements().getLieux().stream()
                .filter(l -> l.getNom().equalsIgnoreCase(nom))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Lieu non trouvé : " + nom));
    }

    private Personnage getPersonnageParNom(String nom) {
        return partie.getElements().getPersonnages().stream()
                .filter(p -> p.getNom().equalsIgnoreCase(nom))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Personnage non trouvé : " + nom));
    }
}
