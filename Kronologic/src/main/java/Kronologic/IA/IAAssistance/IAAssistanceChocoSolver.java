package Kronologic.IA.IAAssistance;

import Kronologic.IA.IADeduction.IADeductionChocoSolver;
import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Note;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.Jeu.Partie;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        // Si le temps du crime n'est pas réduit, tester les questions sur le temps
//        if (!deduction.getModele().getCoupableTemps().isInstantiated()) {
//            IntVar coupableTemps = deduction.getModele().getCoupableTemps();
//            int meilleurTemps = -1;
//            int maxReduction = -1;
//
//            for (int t = coupableTemps.getLB(); t <= coupableTemps.getUB(); t = coupableTemps.nextValue(t)) {
//                int reduction = simulerQuestionTemps(new Lieu(1), new Temps(t)); // On simule avec un lieu générique
//                if (reduction > maxReduction) {
//                    maxReduction = reduction;
//                    meilleurTemps = t;
//                }
//            }
//
//            return new String[]{"Temps", meilleurTemps == -1 ? "?" : String.valueOf(meilleurTemps)};
//        }
//
//        // Si le personnage coupable n'est pas réduit, tester les questions sur un personnage
//        if (!deduction.getModele().getCoupablePersonnage().isInstantiated()) {
//            IntVar coupablePersonnage = deduction.getModele().getCoupablePersonnage();
//            int meilleurPersonnage = -1;
//            int maxReduction = -1;
//            Lieu meilleurLieu = null;
//
//            for (int i = coupablePersonnage.getLB(); i <= coupablePersonnage.getUB(); i = coupablePersonnage.nextValue(i)) {
//                Personnage personnage = new Personnage(deduction.getModele().getPersonnageNom(i));
//
//                for (int l = 1; l <= 6; l++) { // Tester chaque lieu
//                    Lieu lieu = new Lieu(l);
//                    int reduction = simulerQuestionPersonnage(lieu, personnage);
//                    if (reduction > maxReduction) {
//                        maxReduction = reduction;
//                        meilleurPersonnage = i;
//                        meilleurLieu = lieu;
//                    }
//                }
//            }
//
//            return new String[]{meilleurLieu != null ? meilleurLieu.getNom() : "?", meilleurPersonnage == -1 ? "?" : deduction.getModele().getPersonnageNom(meilleurPersonnage)};
//        }
//
//        // Si le lieu du crime n'est pas réduit, tester les questions sur le lieu
//        if (!deduction.getModele().getCoupableLieu().isInstantiated()) {
//            IntVar coupableLieu = deduction.getModele().getCoupableLieu();
//            int meilleurLieu = -1;
//            int maxReduction = -1;
//
//            for (int l = coupableLieu.getLB(); l <= coupableLieu.getUB(); l = coupableLieu.nextValue(l)) {
//                int reduction = simulerQuestionTemps(new Lieu(l), new Temps(3)); // Simuler avec un temps générique
//                if (reduction > maxReduction) {
//                    maxReduction = reduction;
//                    meilleurLieu = l;
//                }
//            }
//
//            return new String[]{"Lieu", meilleurLieu == -1 ? "?" : "Salle " + meilleurLieu};
//        }

        // Si toutes les informations sont déjà trouvées
        return new String[]{"Aucune recommandation", "Vous avez déjà toutes les informations."};
    }

    /**
     * Calcule la réduction des domaines après l'ajout d'une contrainte temporaire.
     *
     * @param action      Une action lambda qui applique la contrainte temporaire.
     * @return Nombre total de valeurs retirées des domaines.
     */
    private int simulerEtMesurerReduction(Runnable action) {
        // Sauvegarde des domaines avant propagation
        Map<IntVar, List<Integer>> domainesAvant = new HashMap<>();
        IntVar[][] positions = deduction.getModele().getPositions();
        for (IntVar[] ligne : positions) {
            for (IntVar var : ligne) {
                domainesAvant.put(var, extraireDomaine(var));
            }
        }

        // Récupérer les contraintes existantes AVANT l'ajout
        int contraintesAvant = deduction.getModele().getModel().getNbCstrs();
        action.run();

        // Calculer la réduction
        int reductionTotale = 0;
        for (IntVar[] ligne : positions) {
            for (IntVar var : ligne) {
                List<Integer> domaineAvant = domainesAvant.get(var);
                List<Integer> domaineApres = extraireDomaine(var);
                reductionTotale += compterReductionDomaine(domaineAvant, domaineApres);
            }
        }

        // Supprimer uniquement les nouvelles contraintes ajoutées
        Constraint[] contraintesApres = deduction.getModele().getModel().getCstrs();
        for (int i = contraintesAvant; i < contraintesApres.length; i++) {
            deduction.getModele().getModel().unpost(contraintesApres[i]);
        }

        return reductionTotale;
    }

    /**
     * Compte combien de valeurs ont été retirées d'un domaine.
     *
     * @param domaineAvant Domaine initial.
     * @param domaineApres Domaine après propagation.
     * @return Nombre de valeurs supprimées.
     */
    private int compterReductionDomaine(List<Integer> domaineAvant, List<Integer> domaineApres) {
        int reduction = 0;
        for (int val : domaineAvant) {
            if (!domaineApres.contains(val)) {
                reduction++;
            }
        }
        return reduction;
    }

    /**
     * Extrait les valeurs du domaine actuel d'une variable Choco-Solver.
     *
     * @param var La variable dont on veut extraire le domaine.
     * @return Liste des valeurs encore possibles.
     */
    private List<Integer> extraireDomaine(IntVar var) {
        List<Integer> valeurs = new ArrayList<>();
        for (int val = var.getLB(); val <= var.getUB(); val = var.nextValue(val)) {
            valeurs.add(val);
        }
        return valeurs;
    }


    @Override
    public int simulerQuestionPersonnage(Lieu lieu, Personnage personnage) {
        return simulerEtMesurerReduction(() ->
                deduction.getModele().ajouterContraintePersonnage(personnage, lieu, 3)
        );
    }


    @Override
    public int simulerQuestionTemps(Lieu lieu, Temps temps) {
        return simulerEtMesurerReduction(() ->
                deduction.getModele().ajouterContrainteTemps(lieu, temps, 3)
        );
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
