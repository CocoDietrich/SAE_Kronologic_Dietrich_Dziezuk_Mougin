package Kronologic.IA.IAAssistance;

import Kronologic.IA.IADeduction.IADeductionChocoSolver;
import Kronologic.IA.IADeduction.ModeleChocoSolver;
import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Note;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.Jeu.Indice.Indice;
import Kronologic.Jeu.Indice.IndicePersonnage;
import Kronologic.Jeu.Indice.IndiceTemps;
import Kronologic.Jeu.Partie;
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
    public String[] recommanderQuestionOptimaleTriche() {
        List<Indice> indices = partie.getGestionnaireIndices().getListeIndices();

        int maxReduction = -1;
        String meilleurType = "Aucune";
        String meilleurValeur = "?";
        Lieu meilleurLieu = null;

        for (Indice indice : indices) {
            if (indice instanceof IndicePersonnage indicePerso) {
                int reduction = simulerPersonnage(
                        indicePerso.getLieu(),
                        indicePerso.getPersonnage(),
                        indicePerso.getInfoPublic(),
                        indicePerso.getInfoPrive()
                );

                if (reduction > maxReduction) {
                    maxReduction = reduction;
                    meilleurType = "Personnage";
                    meilleurValeur = indicePerso.getPersonnage().getNom();
                    meilleurLieu = indicePerso.getLieu();
                }
            } else if (indice instanceof IndiceTemps indiceTemps) {
                int reduction = simulerTemps(
                        indiceTemps.getLieu(),
                        indiceTemps.getTemps(),
                        indiceTemps.getInfoPublic(),
                        indiceTemps.getInfoPrive()
                );

                if (reduction > maxReduction) {
                    maxReduction = reduction;
                    meilleurType = "Temps";
                    meilleurValeur = String.valueOf(indiceTemps.getTemps().getValeur());
                    meilleurLieu = indiceTemps.getLieu();
                }
            }
        }

        return meilleurLieu != null ?
                new String[]{"Lieu : " + meilleurLieu.getNom(), meilleurType + " : " + meilleurValeur} :
                new String[]{"Aucune recommandation", "Vous avez déjà toutes les informations."};
    }


    public int simulerTemps(Lieu lieu, Temps temps, int infoPublic, String infoPrive) {
        ModeleChocoSolver copie = copie();

        IntVar[][] positions = copie.getPositions();
        Map<IntVar, List<Integer>> domainesAvant = new HashMap<>();
        for (IntVar[] ligne : positions) {
            for (IntVar var : ligne) {
                domainesAvant.put(var, extraireDomaine(var));
            }
        }

        if (!infoPrive.equals("Rejouer")) {
            copie.ajouterContraintePersonnage(new Personnage(infoPrive), lieu, temps.getValeur());
        }
        copie.ajouterContrainteTemps(lieu, temps, infoPublic);

        return calculerReduction(copie, domainesAvant);
    }


    public int simulerPersonnage(Lieu lieu, Personnage personnage, int infoPublic, int infoPrive) {
        ModeleChocoSolver copie = copie();

        IntVar[][] positions = copie.getPositions();
        Map<IntVar, List<Integer>> domainesAvant = new HashMap<>();
        for (IntVar[] ligne : positions) {
            for (IntVar var : ligne) {
                domainesAvant.put(var, extraireDomaine(var));
            }
        }

        copie.ajouterContraintePersonnage(personnage, lieu, infoPrive);
        copie.ajouterContrainteNombreDePassages(personnage, lieu, infoPublic);

        return calculerReduction(copie, domainesAvant);
    }

    private ModeleChocoSolver copie() {
        ModeleChocoSolver copie = new ModeleChocoSolver(
                deduction.getPersonnagesNoms(),
                deduction.getSallesAdjacentes(),
                deduction.getPositionsInitiales()
        );
        for (Indice indice : partie.getIndicesDecouverts()) {
            if (indice instanceof IndicePersonnage indicePerso) {
                copie.ajouterContraintePersonnage(indicePerso.getPersonnage(), indicePerso.getLieu(), indicePerso.getInfoPrive());
                copie.ajouterContrainteNombreDePassages(indicePerso.getPersonnage(), indicePerso.getLieu(), indicePerso.getInfoPublic());
            } else if (indice instanceof IndiceTemps indiceTemps) {
                copie.ajouterContrainteTemps(indiceTemps.getLieu(), indiceTemps.getTemps(), indiceTemps.getInfoPublic());
                if (!indiceTemps.getInfoPrive().equals("Rejouer")) {
                    copie.ajouterContraintePersonnage(new Personnage(indiceTemps.getInfoPrive()), indiceTemps.getLieu(), indiceTemps.getTemps().getValeur());
                }
            }
        }
        return copie;
    }

    private int calculerReduction(ModeleChocoSolver copie, Map<IntVar, List<Integer>> domainesAvant) {
        int reduction = 0;
        IntVar[][] positions = copie.getPositions();
        for (IntVar[] ligne : positions) {
            for (IntVar var : ligne) {
                List<Integer> avant = domainesAvant.get(var);
                List<Integer> apres = extraireDomaine(var);
                reduction += (int) avant.stream().filter(val -> !apres.contains(val)).count();
            }
        }
        return reduction;
    }

    private List<Integer> extraireDomaine(IntVar var) {
        List<Integer> valeurs = new ArrayList<>();
        for (int val = var.getLB(); val <= var.getUB(); val = var.nextValue(val)) {
            valeurs.add(val);
        }
        return valeurs;
    }

    @Override
    public String[] recommanderQuestionOptimaleTrichePas() {
        String meilleureQuestion = "Aucune recommandation";
        String meilleureValeur = "Vous avez déjà toutes les informations.";
        double meilleurScore = -1;

        int strategie = (int) (Math.random() * 3); // 0 = min, 1 = max, 2 = moyenne
        System.out.println("📊 Stratégie IA choisie (0=min, 1=max, 2=moyenne) : " + strategie);

        for (Lieu lieu : partie.getElements().getLieux()) {
            for (int temps = 1; temps <= 6; temps++) {
                double score = simulerToutesReponsesTemps(lieu, temps, strategie);
                if (score > meilleurScore) {
                    meilleurScore = score;
                    meilleureQuestion = "Lieu : " + lieu.getNom();
                    meilleureValeur = "Temps : " + temps;
                    System.out.printf("✅ Nouvelle meilleure question trouvée : [%s, %s] avec un score de %.2f\n", meilleureQuestion, meilleureValeur, meilleurScore);
                }
            }

            for (Personnage perso : partie.getElements().getPersonnages()) {
                double score = simulerToutesReponsesPersonnage(perso, lieu, strategie);
                if (score > meilleurScore) {
                    meilleurScore = score;
                    meilleureQuestion = "Lieu : " + lieu.getNom();
                    meilleureValeur = "Personnage : " + perso.getNom();
                    System.out.printf("✅ Nouvelle meilleure question trouvée : [%s, %s] avec un score de %.2f\n", meilleureQuestion, meilleureValeur, meilleurScore);
                }
            }
        }

        return new String[]{meilleureQuestion, meilleureValeur};
    }

    private double simulerToutesReponsesTemps(Lieu lieu, int temps, int strategie) {
        List<Double> reductions = new ArrayList<>();


        for (int infoPublic = 0; infoPublic <= 6; infoPublic++) {
            for (Personnage p : partie.getElements().getPersonnages()) {
                int finalInfoPublic = infoPublic;
                silencieux(() -> {
                    try {
                        ModeleChocoSolver copie = copie();
                        Map<IntVar, List<Integer>> domainesAvant = capturerDomaines(copie);
                        copie.ajouterContrainteTemps(lieu, new Temps(temps), finalInfoPublic);
                        if (!p.getNom().equals("Rejouer")) {
                            copie.ajouterContraintePersonnage(p, lieu, temps);
                        }
                        copie.getModel().getSolver().propagate();
                        double reduction = calculerReduction(copie, domainesAvant);
                        reductions.add(reduction);
                    } catch (Exception ignored) {
                    }
                });
            }

        }
        return calculerScore(reductions, strategie);
    }

    private double simulerToutesReponsesPersonnage(Personnage perso, Lieu lieu, int strategie) {
        List<Double> reductions = new ArrayList<>();

        for (int infoPublic = 0; infoPublic <= 6; infoPublic++) {
            int finalInfoPublic = infoPublic;
            silencieux(() -> {
                try {
                    ModeleChocoSolver copie = copie();
                    Map<IntVar, List<Integer>> domainesAvant = capturerDomaines(copie);
                    copie.ajouterContrainteNombreDePassages(perso, lieu, finalInfoPublic);
                    for (int temps = 1; temps <= 6; temps++) {
                        copie.ajouterContraintePersonnage(perso, lieu, temps);
                        copie.getModel().getSolver().propagate();
                        double reduction = calculerReduction(copie, domainesAvant);
                        reductions.add(reduction);
                    }
                } catch (Exception ignored) {
                }
            });
        }

        return calculerScore(reductions, strategie);
    }

    private double calculerScore(List<Double> reductions, int strategie) {
        if (reductions.isEmpty()) return -1;

        return switch (strategie) {
            case 0 -> reductions.stream().min(Double::compareTo).orElse(-1.0); // Pessimiste
            case 1 -> reductions.stream().max(Double::compareTo).orElse(-1.0); // Optimiste
            case 2 -> reductions.stream().mapToDouble(Double::doubleValue).average().orElse(-1.0); // Moyenne
            default -> -1;
        };
    }

    private Map<IntVar, List<Integer>> capturerDomaines(ModeleChocoSolver modele) {
        Map<IntVar, List<Integer>> domaines = new HashMap<>();
        for (IntVar[] ligne : modele.getPositions()) {
            for (IntVar var : ligne) {
                domaines.put(var, extraireDomaine(var));
            }
        }
        return domaines;
    }

    private void silencieux(Runnable action) {
        java.io.PrintStream originalErr = System.err;
        try {
            System.setErr(new java.io.PrintStream(new java.io.OutputStream() {
                public void write(int b) {
                } // Ne fait rien
            }));
            action.run();
        } finally {
            System.setErr(originalErr);
        }
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

    public IADeductionChocoSolver getDeductionChocoSolver() {
        return this.deduction;
    }
}
