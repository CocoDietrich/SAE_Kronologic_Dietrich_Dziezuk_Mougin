package Kronologic.IA.IAAssistance;

import Kronologic.IA.IADeduction.IADeductionChocoSolver;
import Kronologic.IA.IADeduction.ModeleChocoSolver;
import Kronologic.Jeu.Elements.*;
import Kronologic.Jeu.Indice.*;
import Kronologic.Jeu.Partie;
import org.chocosolver.solver.variables.IntVar;

import java.util.*;

public abstract class IAAssistanceChocoSolver extends IAAssistance {
    protected final IADeductionChocoSolver deduction;
    protected final Partie partie;

    public IAAssistanceChocoSolver(IADeductionChocoSolver deduction, Partie partie) {
        this.deduction = deduction;
        this.partie = partie;
    }

    protected double simulerTemps(Lieu lieu, Temps temps, int infoPublic, String infoPrive) {
        final double[] reduction = {-1};
        silencieux(() -> {
            try {
                ModeleChocoSolver copie = copie();
                Map<IntVar, List<Integer>> domainesAvant = capturerDomaines(copie);
                if (!infoPrive.equals("Rejouer")) {
                    copie.ajouterContraintePersonnage(new Personnage(infoPrive), lieu, temps.getValeur());
                }
                copie.ajouterContrainteTemps(lieu, temps, infoPublic);
                copie.getModel().getSolver().propagate();
                reduction[0] = calculerReduction(copie, domainesAvant);
            } catch (Exception ignored) {
            }
        });
        return reduction[0];
    }

    protected double simulerPersonnage(Lieu lieu, Personnage personnage, int infoPublic, int infoPrive) {
        final double[] reduction = {-1};
        silencieux(() -> {
            try {
                ModeleChocoSolver copie = copie();
                Map<IntVar, List<Integer>> domainesAvant = capturerDomaines(copie);
                copie.ajouterContrainteNombreDePassages(personnage, lieu, infoPublic);
                copie.ajouterContraintePersonnage(personnage, lieu, infoPrive);
                copie.getModel().getSolver().propagate();
                reduction[0] = calculerReduction(copie, domainesAvant);
            } catch (Exception ignored) {
            }
        });
        return reduction[0];
    }

    protected ModeleChocoSolver copie() {
        ModeleChocoSolver copie = new ModeleChocoSolver(
                deduction.getPersonnagesNoms(),
                deduction.getSallesAdjacentes(),
                deduction.getPositionsInitiales()
        );
        for (Indice indice : partie.getIndicesDecouverts()) {
            if (indice instanceof IndicePersonnage ip) {
                copie.ajouterContraintePersonnage(ip.getPersonnage(), ip.getLieu(), ip.getInfoPrive());
                copie.ajouterContrainteNombreDePassages(ip.getPersonnage(), ip.getLieu(), ip.getInfoPublic());
            } else if (indice instanceof IndiceTemps it) {
                copie.ajouterContrainteTemps(it.getLieu(), it.getTemps(), it.getInfoPublic());
                if (!it.getInfoPrive().equals("Rejouer")) {
                    copie.ajouterContraintePersonnage(new Personnage(it.getInfoPrive()), it.getLieu(), it.getTemps().getValeur());
                }
            }
        }
        return copie;
    }

    protected int calculerReduction(ModeleChocoSolver copie, Map<IntVar, List<Integer>> domainesAvant) {
        int reduction = 0;
        for (IntVar[] ligne : copie.getPositions()) {
            for (IntVar var : ligne) {
                List<Integer> avant = domainesAvant.get(var);
                List<Integer> apres = extraireDomaine(var);
                reduction += (int) avant.stream().filter(val -> !apres.contains(val)).count();
            }
        }
        return reduction;
    }

    protected Map<IntVar, List<Integer>> capturerDomaines(ModeleChocoSolver modele) {
        Map<IntVar, List<Integer>> domaines = new HashMap<>();
        for (IntVar[] ligne : modele.getPositions()) {
            for (IntVar var : ligne) {
                domaines.put(var, extraireDomaine(var));
            }
        }
        return domaines;
    }

    protected List<Integer> extraireDomaine(IntVar var) {
        List<Integer> valeurs = new ArrayList<>();
        for (int val = var.getLB(); val <= var.getUB(); val = var.nextValue(val)) {
            valeurs.add(val);
        }
        return valeurs;
    }

    protected void silencieux(Runnable action) {
        java.io.PrintStream originalErr = System.err;
        try {
            System.setErr(new java.io.PrintStream(new java.io.OutputStream() {
                public void write(int b) {
                }
            }));
            action.run();
        } finally {
            System.setErr(originalErr);
        }
    }

    public IADeductionChocoSolver getDeductionChocoSolver() {
        return deduction;
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
            if (note.getTemps().getValeur() != 1 && note.getPersonnage() != null) {
                int indexPersonnage = deduction.getModele().getIndexPersonnage(note.getPersonnage().getNom().substring(0, 1));
                int temps = note.getTemps().getValeur() - 1;
                int lieuJoueur = note.getLieu().getId();

                IntVar domaineIA = positionsIA[indexPersonnage][temps];

                if (!note.estAbsence() && !note.estHypothese()) { // Présence
                    if (!domaineIA.isInstantiated() || domaineIA.getValue() != lieuJoueur) {
                        correction.append(String.format("⚠️ Erreur : La note de présence de %s en %s au temps %d est fausse. ❌\n",
                                note.getPersonnage().getNom(), note.getLieu().getNom(), note.getTemps().getValeur()));
                    }
                } else if (note.estAbsence() && !note.estHypothese()) { // Absence
                    if (!domaineIA.isInstantiated() || domaineIA.getValue() == lieuJoueur) {
                        correction.append(String.format("⚠️ Erreur : La note d'absence de %s en %s au temps %d est fausse. ❌\n",
                                note.getPersonnage().getNom(), note.getLieu().getNom(), note.getTemps().getValeur()));
                    }
                } else if (note.estHypothese()) { // Hypothèses (présence ou absence)
                    if (!domaineIA.contains(lieuJoueur) || domaineIA.isInstantiated()) {
                        correction.append(String.format("⚠️ Erreur : L'hypothèse %s de %s en %s au temps %d est fausse. ❌\n",
                                note.estAbsence() ? "d'absence" : "de présence", note.getPersonnage().getNom(), note.getLieu().getNom(), note.getTemps().getValeur()));
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