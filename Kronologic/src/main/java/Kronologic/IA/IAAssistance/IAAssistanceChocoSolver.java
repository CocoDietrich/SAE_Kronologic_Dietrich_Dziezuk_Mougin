package Kronologic.IA.IAAssistance;

import Kronologic.IA.IADeduction.IADeductionChocoSolver;
import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Note;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Partie;
import org.chocosolver.solver.variables.IntVar;

import java.util.List;

public class IAAssistanceChocoSolver extends IAAssistance {
    private final IADeductionChocoSolver deduction;
    private final Partie partie;

    public IAAssistanceChocoSolver(IADeductionChocoSolver deduction, Partie partie) {
        super();
        this.deduction = deduction;
        this.partie = partie;
    }

    @Override
    public String[] recommanderQuestionOptimal() {
        IntVar[][] positions = deduction.getModele().getPositions();
        int indexDetective = deduction.getModele().getIndexPersonnage("D");

        // Vérifier si le temps du crime est réduit
        if (!deduction.getModele().getCoupableTemps().isInstantiated()) {
            return new String[]{"Temps", "?"}; // On doit encore poser une question sur le temps
        }

        // Vérifier si le personnage coupable est réduit
        if (!deduction.getModele().getCoupablePersonnage().isInstantiated()) {
            return new String[]{"Lieu du crime", "Personnage"}; // On doit poser une question sur le personnage
        }

        // Vérifier si le lieu du crime est réduit
        if (!deduction.getModele().getCoupableLieu().isInstantiated()) {
            return new String[]{"Lieu", "?"}; // On doit poser une question sur le lieu
        }

        // Si toutes les informations sont déjà trouvées
        return new String[]{"Aucune recommandation", "Vous avez déjà toutes les informations."};
    }


    @Override
    public void simulerQuestion(Lieu lieu, Personnage personnage) {
        // TODO : Simuler les conséquences de poser une question sur le lieu/personnage
    }

    @Override
    public String corrigerDeductions() {
        StringBuilder correction = new StringBuilder();
        correction.append("===== Correction des Déductions du Joueur =====\n");

        // Récupération des notes du joueur
        List<Note> notesJoueur = partie.getGestionnaireNotes().getNotes();

        // Vérification rapide : si le joueur n'a fait aucune hypothèse, inutile de continuer
        if (notesJoueur.isEmpty()) {
            correction.append("Aucune hypothèse à corriger.\n");
            return correction.toString();
        }

        // Récupération des positions de l'IA Choco-Solver
        IntVar[][] positionsIA = deduction.getModele().getPositions();

        // Comparaison des hypothèses du joueur avec celles de l'IA
        for (Note note : notesJoueur) {
            if (note.estHypothese()) { // On ne vérifie que les hypothèses du joueur
                int indexPersonnage = deduction.getModele().getIndexPersonnage(note.getPersonnage().getNom());
                int temps = note.getTemps().getValeur() - 1;
                int lieuJoueur = note.getLieu().getId();

                // Vérifier si l'IA exclut ce lieu pour ce personnage à ce temps
                if (!positionsIA[indexPersonnage][temps].contains(lieuJoueur)) {
                    correction.append(String.format("⚠️ Erreur : %s ne peut pas être en %s au temps %d ! ❌\n",
                            note.getPersonnage().getNom(), note.getLieu().getNom(), note.getTemps().getValeur()));
                }
            }
        }

        // Si aucune erreur trouvée
        if (correction.toString().equals("===== Correction des Déductions du Joueur =====\n")) {
            correction.append("✅ Toutes vos hypothèses sont correctes ! 🎉\n");
        }

        return correction.toString();
    }

}
