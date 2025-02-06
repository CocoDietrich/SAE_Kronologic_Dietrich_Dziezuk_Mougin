package Kronologic.IA.IAAssistance;

import Kronologic.IA.IADeduction.IADeductionChocoSolver;
import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Note;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;
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
        //mettre que c'est work in progress
        return new String[]{"Work in progress"};
    }


    @Override
    public int predireTemps(Lieu lieu, Temps temps){
        //TODO
        return 0;
    }

    @Override
    public int predirePersonnage(Lieu lieu, Personnage personnage){
        //TODO
        return 0;
    }


    @Override
    public String corrigerDeductions() {
        StringBuilder correction = new StringBuilder();

        // Récupération des notes du joueur
        List<Note> notesJoueur = partie.getGestionnaireNotes().getNotes();

        // Récupération des positions de l'IA Choco-Solver
        IntVar[][] positionsIA = deduction.getModele().getPositions();

        // Comparaison des hypothèses du joueur avec celles de l'IA
        for (Note note : notesJoueur) {
            // on ne s'occupe pas des pions du temps 1 et des notes de nombre de perso dans une salle
            if (note.getTemps().getValeur() != 1 || note.getPersonnage() != null) {
                int indexPersonnage = deduction.getModele().getIndexPersonnage(note.getPersonnage().getNom().substring(0, 1));
                int temps = note.getTemps().getValeur() - 1;
                int lieuJoueur = note.getLieu().getId();

                IntVar domaineIA = positionsIA[indexPersonnage][temps];

                if (!note.estAbsence() && !note.estHypothese()) { // Présence
                    if (!domaineIA.contains(lieuJoueur)) {
                        correction.append(String.format("⚠️ Erreur : La note de présence sur %s en %s au temps %d est fausse. ❌\n",
                                note.getPersonnage().getNom(), note.getLieu().getNom(), note.getTemps().getValeur()));
                    }
                } else if (note.estAbsence() && !note.estHypothese()) { // Absence
                    if (domaineIA.contains(lieuJoueur)) {
                        correction.append(String.format("⚠️ Erreur : La note d'absence de %s en %s au temps %d est fausse. ❌\n",
                                note.getPersonnage().getNom(), note.getLieu().getNom(), note.getTemps().getValeur()));
                    }
                } else if (note.estAbsence() && note.estHypothese()) { // Hypothèse d'absence
                    if (domaineIA.contains(lieuJoueur)) {
                        correction.append(String.format("⚠️ Erreur : L'hypothèse d'absence de %s en %s au temps %d est fausse. ❌\n",
                                note.getPersonnage().getNom(), note.getLieu().getNom(), note.getTemps().getValeur()));
                    }
                }
            }
        }

        // Si aucune erreur trouvée
        if (correction.isEmpty()) {
            correction.append("✅ Toutes vos hypothèses sont correctes ! 🎉\n");
        }

        return correction.toString();
    }
}
