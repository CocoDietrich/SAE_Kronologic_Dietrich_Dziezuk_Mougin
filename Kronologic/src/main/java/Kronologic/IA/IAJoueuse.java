package Kronologic.IA;

import Kronologic.IA.IAAssistance.IAAssistance;
import Kronologic.IA.IAAssistance.IAAssistanceChocoSolver;
import Kronologic.IA.IADeduction.IADeductionChocoSolver;
import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Note;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.Jeu.Indice.IndicePersonnage;
import Kronologic.Jeu.Indice.IndiceTemps;

import java.util.ArrayList;

public class IAJoueuse {

    private final static int NB_TEMPS = 6;

    private final IAAssistance iaAssistance;

    public IAJoueuse(IAAssistance iaAssistance) {
        this.iaAssistance = iaAssistance;
    }

    // Méthode pour jouer jusqu'à trouver le coupable
    public String jouerJusquaTrouverCoupable() {
        StringBuilder historiqueQuestions = new StringBuilder();
        historiqueQuestions.append("===== 🕵️‍♂️ Resultats de l'IA 🕵️‍♂️ =====\n");
        while (true) {
            // On crée les notes associées aux domaines de l'IA
            for (Note n : iaAssistance.getPartie().getGestionnaireNotes().getNotes()) {
                iaAssistance.getPartie().supprimerNote(n);
            }

            // Note de présence
//            for (int i = 1; i < NB_TEMPS; i++) {
//                for (int j = 0; j < iaAssistance.getPartie().getElements().getLieux().size(); j++) {
//                    for (int k = 0; k < iaAssistance.getPartie().getElements().getPersonnages().size(); k++) {
//
//                        iaAssistance.getPartie().ajouterNote();
//                    }
//                }
//            }

            // Note d'absence


            // Note de présence d'hypothèse


            // Vérifier si la solution a été trouvée
            if (iaAssistance instanceof IAAssistanceChocoSolver chocoIA) {
                IADeductionChocoSolver iaDeduction = chocoIA.getDeductionChocoSolver();
                if (iaDeduction.solutionTrouvee()) {
                    String nom = iaDeduction.getModele().getPersonnageNom(iaDeduction.getModele().getCoupablePersonnage().getValue());
                    String lieu = String.valueOf(iaDeduction.getModele().getCoupableLieu().getValue());
                    int temps = iaDeduction.getModele().getCoupableTemps().getValue();
                    return historiqueQuestions + "\n===== 🎯 Coupable Identifié ! =====\n" +
                    "Le coupable est 👤 " + nom + " dans le lieu 📍 " + lieu + " au temps ⏳ " + temps + ".";
                }
            }

            String[] question = iaAssistance.recommanderQuestionOptimale();

            if (question[0].startsWith("Lieu :") && question[1].startsWith("Temps :")) {
                // Question temps
                String nomLieu = question[0].substring("Lieu : ".length()).trim();
                int valeurTemps = Integer.parseInt(question[1].substring("Temps : ".length()).trim());

                Lieu lieu = getLieuParNom(nomLieu);
                Temps temps = new Temps(valeurTemps);

                IndiceTemps indice = (IndiceTemps) iaAssistance.getPartie().poserQuestionTemps(lieu, temps);

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

                IndicePersonnage indice = (IndicePersonnage) iaAssistance.getPartie().poserQuestionPersonnage(lieu, personnage);

                if (iaAssistance instanceof IAAssistanceChocoSolver chocoIA) {
                    IADeductionChocoSolver iaDeduction = chocoIA.getDeductionChocoSolver();
                    iaDeduction.poserQuestionPersonnage(personnage, lieu, indice.getInfoPublic(), indice.getInfoPrive());
                }

            } else {
                return "❌ L’IA n’a pas trouvé de solution.";
            }

            // On cherche faire des notes en fonction des domaines de l'IA


            historiqueQuestions.append("🔁 Tour ")
                    .append(iaAssistance.getPartie().getNbQuestion())
                    .append(" : Question posée → ")
                    .append(question[0])
                    .append(" | ")
                    .append(question[1])
                    .append("\n");

        }
    }


    // Méthode pour obtenir le lieu par son nom
    private Lieu getLieuParNom(String nom) {
        return iaAssistance.getPartie().getElements().lieux().stream()
                .filter(l -> l.getNom().equalsIgnoreCase(nom))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Lieu non trouvé : " + nom));
    }

    // Méthode pour obtenir le personnage par son nom
    private Personnage getPersonnageParNom(String nom) {
        return iaAssistance.getPartie().getElements().personnages().stream()
                .filter(p -> p.getNom().equalsIgnoreCase(nom))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Personnage non trouvé : " + nom));
    }
}
