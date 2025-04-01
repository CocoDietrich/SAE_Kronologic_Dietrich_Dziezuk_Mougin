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

    /**
     * Simule l'ajout d'une contrainte de temps et calcule la réduction du domaine des variables.
     *
     * @param lieu        Le lieu à simuler.
     * @param temps       Le temps à simuler.
     * @param infoPublic  L'information publique associée.
     * @param infoPrive   L'information privée associée.
     * @return La réduction du domaine des variables après simulation.
     */
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

    /**
     * Simule l'ajout d'une contrainte de personnage et calcule la réduction du domaine des variables.
     *
     * @param lieu        Le lieu à simuler.
     * @param personnage  Le personnage à simuler.
     * @param infoPublic  L'information publique associée.
     * @param infoPrive   L'information privée associée.
     * @return La réduction du domaine des variables après simulation.
     */
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

    /**
     * Crée une copie du modèle ChocoSolver.
     * @return Une nouvelle instance de ModeleChocoSolver avec les mêmes contraintes.
     */
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

    /**
     * Calcule la réduction du domaine des variables après simulation.
     *
     * @param copie          La copie du modèle ChocoSolver.
     * @param domainesAvant  Les domaines avant la simulation.
     * @return La réduction du domaine des variables.
     */
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

    /**
     * Capture les domaines des variables du modèle ChocoSolver.
     *
     * @param modele Le modèle ChocoSolver.
     * @return Un dictionnaire contenant les variables et leurs domaines respectifs.
     */
    protected Map<IntVar, List<Integer>> capturerDomaines(ModeleChocoSolver modele) {
        Map<IntVar, List<Integer>> domaines = new HashMap<>();
        for (IntVar[] ligne : modele.getPositions()) {
            for (IntVar var : ligne) {
                domaines.put(var, extraireDomaine(var));
            }
        }
        return domaines;
    }

    /**
     * Extrait le domaine d'une variable.
     *
     * @param var La variable dont on veut extraire le domaine.
     * @return Une liste contenant les valeurs du domaine de la variable.
     */
    protected List<Integer> extraireDomaine(IntVar var) {
        List<Integer> valeurs = new ArrayList<>();
        for (int val = var.getLB(); val <= var.getUB(); val = var.nextValue(val)) {
            valeurs.add(val);
        }
        return valeurs;
    }

    /**
     * Exécute une action en silence, sans afficher les erreurs.
     *
     * @param action L'action à exécuter.
     */
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

    /**
     * Getter pour le modèle ChocoSolver de l'IA.
     *
     * @return Le modèle ChocoSolver de l'IA.
     */
    public IADeductionChocoSolver getDeductionChocoSolver() {
        return deduction;
    }

    /**
     * Corrige les déductions du joueur en comparant ses notes avec celles de l'IA.
     *
     * @return Un message indiquant les erreurs trouvées dans les déductions du joueur.
     */
    @Override
    public String corrigerDeductions() {
        StringBuilder correction = new StringBuilder();

        List<Note> notesJoueur = partie.getGestionnaireNotes().getNotes();
        IntVar[][] positionsIA = deduction.getModele().getPositions();

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