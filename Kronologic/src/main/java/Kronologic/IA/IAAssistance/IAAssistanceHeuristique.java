package Kronologic.IA.IAAssistance;

import Kronologic.IA.IADeduction.IADeductionHeuristique;
import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Note;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.Jeu.Indice.Indice;
import Kronologic.Jeu.Indice.IndicePersonnage;
import Kronologic.Jeu.Indice.IndiceTemps;
import Kronologic.Jeu.Partie;

import java.util.List;

public abstract class IAAssistanceHeuristique extends IAAssistance {
    protected IADeductionHeuristique iaDeductionHeuristique;
    protected Partie partie;

    public IAAssistanceHeuristique(IADeductionHeuristique iaDeductionHeuristique, Partie partie) {
        this.iaDeductionHeuristique = iaDeductionHeuristique;
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
    protected int simulerTemps(Lieu lieu, Temps temps, int infoPublic, String infoPrive) {
        IADeductionHeuristique clone = copie();
        int nbInitial = compterDomaines(clone.recupererDomainesPersonnages());
        silencieux(() -> clone.poserQuestionTemps(lieu, temps, infoPublic, infoPrive));
        int nbFinal = compterDomaines(clone.recupererDomainesPersonnages());
        return nbInitial - nbFinal;
    }

    /**
     * Simule l'ajout d'une contrainte de personnage et calcule la réduction du domaine des variables.
     *
     * @param personnage  Le personnage à simuler.
     * @param lieu        Le lieu à simuler.
     * @param infoPublic  L'information publique associée.
     * @param infoPrive   L'information privée associée.
     * @return La réduction du domaine des variables après simulation.
     */
    protected int simulerPersonnage(Personnage personnage, Lieu lieu, int infoPublic, int infoPrive) {
        IADeductionHeuristique clone = copie();
        int nbInitial = compterDomaines(clone.recupererDomainesPersonnages());
        silencieux(() -> clone.poserQuestionPersonnage(personnage, lieu, infoPublic, infoPrive));
        int nbFinal = compterDomaines(clone.recupererDomainesPersonnages());
        return nbInitial - nbFinal;
    }

    /**
     * Compte le nombre de domaines restants dans les variables.
     *
     * @param domaines Les domaines des variables.
     * @return Le nombre de domaines restants.
     */
    private int compterDomaines(boolean[][][] domaines) {
        int count = 0;
        for (boolean[][] d1 : domaines)
            for (boolean[] d2 : d1)
                for (boolean b : d2)
                    if (b) count++;
        return count;
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
                @Override
                public void write(int b) {
                }
            }));
            action.run();
        } catch (Exception ignored) {
        } finally {
            System.setErr(originalErr);
        }
    }

    /**
     * Crée une copie de l'instance actuelle de IADeductionHeuristique.
     *
     * @return Une nouvelle instance de IADeductionHeuristique.
     */
    protected IADeductionHeuristique copie() {
        IADeductionHeuristique copie = new IADeductionHeuristique(partie);
        for (Indice indice : partie.getIndicesDecouverts()) {
            if (indice instanceof IndicePersonnage ip) {
                copie.poserQuestionPersonnage(ip.getPersonnage(), ip.getLieu(), ip.getInfoPublic(), ip.getInfoPrive());
            } else if (indice instanceof IndiceTemps it) {
                copie.poserQuestionTemps(it.getLieu(), it.getTemps(), it.getInfoPublic(), it.getInfoPrive());
            }
        }
        return copie;
    }

    /**
     * Corrige les déductions du joueur en fonction des informations de l'IA de déduction.
     *
     * @return Un message indiquant les erreurs de déduction.
     */
    @Override
    public String corrigerDeductions() {
        StringBuilder correction = new StringBuilder();

        List<Note> notesJoueur = partie.getGestionnaireNotes().getNotes();
        boolean[][][] domainesIA = iaDeductionHeuristique.recupererDomainesPersonnages();

        for (Note note : notesJoueur) {
            if (note.getTemps().getValeur() != 1 && note.getPersonnage() != null) {
                int temps = note.getTemps().getValeur() - 1;
                int personnage = iaDeductionHeuristique.getModel().getIndexPersonnage(note.getPersonnage().getNom().substring(0, 1));
                int lieuJoueur = note.getLieu().getId() - 1;

                boolean[] domaine = domainesIA[temps][personnage];

                int nbPossibles = 0;
                int valeurCertaine = -1;
                for (int l = 0; l < domaine.length; l++) {
                    if (domaine[l]) {
                        nbPossibles++;
                        valeurCertaine = l;
                    }
                }

                String nomPerso = note.getPersonnage().getNom();
                String nomLieu = note.getLieu().getNom();
                int nbtemps = note.getTemps().getValeur();

                if (!note.estAbsence() && !note.estHypothese()) { // Présence
                    if (nbPossibles != 1 || valeurCertaine != lieuJoueur) {
                        correction.append(String.format("⚠️ Erreur : La note de présence de %s en %s au temps %d est fausse. ❌\n",
                                nomPerso, nomLieu, nbtemps));
                    }
                } else if (note.estAbsence() && !note.estHypothese()) { // Absence
                    if (domaine[lieuJoueur]) {
                        correction.append(String.format("⚠️ Erreur : La note d'absence de %s en %s au temps %d est fausse. ❌\n",
                                nomPerso, nomLieu, nbtemps));
                    }
                } else if (note.estHypothese() && !domaine[lieuJoueur]) { // Hypothèses (présence ou absence)
                    String type = note.estAbsence() ? "d'absence" : "de présence";
                    correction.append(String.format("⚠️ Erreur : L'hypothèse %s de %s en %s au temps %d est fausse. ❌\n",
                            type, nomPerso, nomLieu, nbtemps));
                }

            }
        }

        if (correction.isEmpty()) {
            correction.append("✅ Toutes vos hypothèses sont correctes ! 🎉\n");
        }

        return correction.toString();
    }
}