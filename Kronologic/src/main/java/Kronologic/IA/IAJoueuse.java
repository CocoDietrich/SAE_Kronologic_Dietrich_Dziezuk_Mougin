package Kronologic.IA;

import Kronologic.IA.IAAssistance.IAAssistance;
import Kronologic.IA.IAAssistance.IAAssistanceChocoSolver;
import Kronologic.IA.IADeduction.IADeductionChocoSolver;
import Kronologic.IA.IADeduction.ModeleChocoSolver;
import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Note;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.Jeu.Enums.ImagePersonnages;
import Kronologic.Jeu.Indice.IndicePersonnage;
import Kronologic.Jeu.Indice.IndiceTemps;
import Kronologic.Jeu.Partie;
import org.chocosolver.solver.variables.IntVar;

import java.util.ArrayList;
import java.util.List;

public class IAJoueuse {

    private final IAAssistance iaAssistance;
    private final Partie partie;

    public IAJoueuse(IAAssistance iaAssistance, Partie partie) {
        this.iaAssistance = iaAssistance;
        this.partie = partie;
    }

    // Méthode pour jouer jusqu'à trouver le coupable
    public String jouerJusquaTrouverCoupable(boolean noteNecessaire) {
        StringBuilder historiqueQuestions = new StringBuilder();
        historiqueQuestions.append("===== 🕵️‍♂️ Resultats de l'IA 🕵️‍♂️ =====\n");
        while (true) {
            if (noteNecessaire) {
                List<Note> notesASupprimer = new ArrayList<>(partie.getGestionnaireNotes().getNotes());
                for (Note n : notesASupprimer) {
                    partie.supprimerNote(n);
                }

                if (iaAssistance instanceof IAAssistanceChocoSolver chocoIA) {
                    IADeductionChocoSolver iaDeduction = chocoIA.getDeductionChocoSolver();
                    ModeleChocoSolver modele = iaDeduction.getModele();
                    IntVar[][] positions = modele.getPositions();
                    String[] noms = modele.getPersonnages();

                    for (int i = 0; i < noms.length; i++) {
                        Personnage personnage = new Personnage(ImagePersonnages.getPersonnages().get(i));

                        for (int t = 0; t < 6; t++) {
                            Temps temps = new Temps(t + 1);
                            IntVar position = positions[i][t];

                            for (Lieu lieu : partie.getElements().lieux()) {
                                Note note = new Note(lieu, temps, personnage);

                                if (position.isInstantiated() && position.getValue() == lieu.getId()) {
                                    // Présence confirmée
                                    note.setEstAbsence(false);
                                    note.setEstHypothese(false);
                                } else if (t == 0) {
                                    // Temps 1, aucune hypothèse, ignorer le reste
                                    continue;
                                } else if (!position.contains(lieu.getId())) {
                                    // Absence certaine
                                    note.setEstAbsence(true);
                                    note.setEstHypothese(false);
                                } else {
                                    // Hypothèse
                                    note.setEstAbsence(false);
                                    note.setEstHypothese(true);
                                }

                                partie.ajouterNote(note);
                            }
                        }
                    }
                }
            }

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

                IndiceTemps indice = (IndiceTemps) partie.poserQuestionTemps(lieu, temps);

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

                if (iaAssistance instanceof IAAssistanceChocoSolver chocoIA) {
                    IADeductionChocoSolver iaDeduction = chocoIA.getDeductionChocoSolver();
                    iaDeduction.poserQuestionPersonnage(personnage, lieu, indice.getInfoPublic(), indice.getInfoPrive());
                }

            } else {
                return "❌ L’IA n’a pas trouvé de solution.";
            }
            historiqueQuestions.append("🔁 Tour ")
                    .append(partie.getNbQuestion())
                    .append(" : Question posée → ")
                    .append(question[0])
                    .append(" | ")
                    .append(question[1])
                    .append("\n");

        }
    }


    // Méthode pour obtenir le lieu par son nom
    private Lieu getLieuParNom(String nom) {
        return partie.getElements().lieux().stream()
                .filter(l -> l.getNom().equalsIgnoreCase(nom))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Lieu non trouvé : " + nom));
    }

    // Méthode pour obtenir le personnage par son nom
    private Personnage getPersonnageParNom(String nom) {
        return partie.getElements().personnages().stream()
                .filter(p -> p.getNom().equalsIgnoreCase(nom))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Personnage non trouvé : " + nom));
    }
}