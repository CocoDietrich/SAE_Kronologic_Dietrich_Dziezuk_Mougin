package Kronologic.IA;

import Kronologic.IA.IAAssistance.IAAssistance;
import Kronologic.IA.IAAssistance.IAAssistanceChocoSolver;
import Kronologic.IA.IADeduction.IADeductionChocoSolver;
import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.Jeu.Indice.IndicePersonnage;
import Kronologic.Jeu.Indice.IndiceTemps;
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

            if (iaAssistance instanceof IAAssistanceChocoSolver chocoIA) {
                System.out.println("🔍 Vérification de la déduction...");
                IADeductionChocoSolver iaDeduction = chocoIA.getDeductionChocoSolver();
                if (iaDeduction.solutionTrouvee()) {
                    String nom = partie.getEnquete().getMeurtrier().getNom();
                    String lieu = partie.getEnquete().getLieuMeurtre().getNom();
                    int temps = partie.getEnquete().getTempsMeurtre().getValeur();
                    System.out.printf("🎯 Choco a trouvé ! Coupable : %s, Lieu : %s, Temps : %d\n", nom, lieu, temps);
                    System.out.println(iaDeduction.afficherHistoriqueDeduction());
                    break;
                }
            }

            String[] question = iaAssistance.recommanderQuestionOptimale();

            if (question[0].startsWith("Lieu :") && question[1].startsWith("Temps :")) {
                // Question temps
                String nomLieu = question[0].substring("Lieu : ".length()).trim();
                int valeurTemps = Integer.parseInt(question[1].substring("Temps : ".length()).trim());

                Lieu lieu = getLieuParNom(nomLieu);
                Temps temps = new Temps(valeurTemps);

                IndiceTemps indice = (IndiceTemps) partie.poserQuestionTemps(lieu, temps);
                System.out.printf("📌 Question posée : Lieu=%s, Temps=%d\n", nomLieu, valeurTemps);

                if (iaAssistance instanceof IAAssistanceChocoSolver chocoIA) {
                    IADeductionChocoSolver iaDeduction = chocoIA.getDeductionChocoSolver();
                    iaDeduction.poserQuestionTemps(lieu, temps, indice.getInfoPublic(), indice.getInfoPrive());
                }

            } else if (question[0].startsWith("Lieu :") && question[1].startsWith("Personnage :")) {
                // Question personnage
                String nomLieu = question[0].substring("Lieu : ".length()).trim();
                String nomPerso = question[1].substring("Personnage : ".length()).trim();

                Lieu lieu = getLieuParNom(nomLieu);
                Personnage personnage = getPersonnageParNom(nomPerso);

                IndicePersonnage indice = (IndicePersonnage) partie.poserQuestionPersonnage(lieu, personnage);
                System.out.printf("📌 Question posée : Lieu=%s, Personnage=%s\n", nomLieu, nomPerso);

                if (iaAssistance instanceof IAAssistanceChocoSolver chocoIA) {
                    IADeductionChocoSolver iaDeduction = chocoIA.getDeductionChocoSolver();
                    iaDeduction.poserQuestionPersonnage(personnage, lieu, indice.getInfoPublic(), indice.getInfoPrive());
                }

            } else {
                System.out.println("Aucune nouvelle question possible.");
                break;
            }

            tour++;
        }
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
