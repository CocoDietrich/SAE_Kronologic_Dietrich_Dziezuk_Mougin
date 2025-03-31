package Kronologic.MVC.Modele.SousModeleJeu;

import Kronologic.IA.IAAssistance.*;
import Kronologic.IA.IADeduction.IADeductionChocoSolver;
import Kronologic.IA.IADeduction.IADeductionHeuristique;
import Kronologic.Jeu.Indice.*;
import Kronologic.Jeu.Partie;
import Kronologic.MVC.Modele.ModeleJeu;
import Kronologic.MVC.Modele.Sujet;
import Kronologic.MVC.Vue.Observateur;
import Kronologic.MVC.Vue.PopUps.VuePopUpDemanderIndice;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

public class ModeleIA implements Sujet {

    private final IADeductionChocoSolver iaDeductionChocoSolver;
    private final IADeductionHeuristique iaDeductionHeuristique;

    private IAAssistance iaAssistanceChoco;
    private IAAssistance iaAssistanceHeuristique;

    private final List<Observateur> observateurs;
    private boolean iaAssistanceChocoActive = true;

    public ModeleIA(Partie partie) {
        this.observateurs = new ArrayList<>();
        this.iaDeductionChocoSolver = new IADeductionChocoSolver(partie);
        this.iaDeductionHeuristique = new IADeductionHeuristique(partie);

        this.iaAssistanceChoco = new IAAssistanceChocoTriche(iaDeductionChocoSolver, partie);
        this.iaAssistanceHeuristique = new IAAssistanceHeuristiqueTriche(iaDeductionHeuristique, partie);
    }

    public String voirDeductionIAChocoSolver() {
        return iaDeductionChocoSolver.afficherHistoriqueDeduction();
    }

    public String voirDeductionIAHeuristique() {
        return iaDeductionHeuristique.afficherHistoriqueDeduction();
    }

    public String afficherMauvaisesDeductions() {
        return iaAssistanceChoco.corrigerDeductions();
    }

    public void ajoutContraintesQuestion(Indice i) {
        if (i instanceof IndicePersonnage ip) {
            iaDeductionChocoSolver.poserQuestionPersonnage(ip.getPersonnage(), ip.getLieu(), ip.getInfoPublic(), ip.getInfoPrive());
            iaDeductionHeuristique.poserQuestionPersonnage(ip.getPersonnage(), ip.getLieu(), ip.getInfoPublic(), ip.getInfoPrive());
        } else if (i instanceof IndiceTemps it) {
            iaDeductionChocoSolver.poserQuestionTemps(it.getLieu(), it.getTemps(), it.getInfoPublic(), it.getInfoPrive());
            iaDeductionHeuristique.poserQuestionTemps(it.getLieu(), it.getTemps(), it.getInfoPublic(), it.getInfoPrive());
        }
    }

    public String demanderIndice() {
        String[] question = getIaAssistanceActive().recommanderQuestionOptimale();

        if (!question[0].equals("Aucune recommandation")) {
            return "Demandez une question avec " + question[0] + " et " + question[1] + ".";
        } else {
            return "Vous avez déjà trouvé la réponse, relancez une partie pour recommencer.";
        }
    }

    @Override
    public void enregistrerObservateur(Observateur o) {
        this.observateurs.add(o);
    }

    @Override
    public void supprimerObservateur(Observateur o) {
        this.observateurs.remove(o);
    }

    @Override
    public void notifierObservateurs() {
        for (Observateur o : observateurs) {
            o.actualiser();
        }
    }

    public List<Observateur> getObservateurs() {
        return observateurs;
    }

    public void utiliserIAAssistanceChoco() {
        this.iaAssistanceChocoActive = true;
    }

    public void utiliserIAAssistanceHeuristique() {
        this.iaAssistanceChocoActive = false;
    }

    public void activerTriche() {
        this.iaAssistanceChoco = new IAAssistanceChocoTriche(iaDeductionChocoSolver, ModeleJeu.getPartie());
        this.iaAssistanceHeuristique = new IAAssistanceHeuristiqueTriche(iaDeductionHeuristique, ModeleJeu.getPartie());
    }

    public void desactiverTriche() {
        this.iaAssistanceChoco = new IAAssistanceChocoTrichePas(iaDeductionChocoSolver, ModeleJeu.getPartie());
        this.iaAssistanceHeuristique = new IAAssistanceHeuristiqueTrichePas(iaDeductionHeuristique, ModeleJeu.getPartie());
    }


    public void afficherPopUpDemanderIndice() {
        VuePopUpDemanderIndice vue = (VuePopUpDemanderIndice) observateurs.stream()
                .filter(o -> o instanceof VuePopUpDemanderIndice)
                .findFirst()
                .orElse(null);

        assert vue != null;
        vue.afficherPopUp();

        vue.boutonChoco.setOnAction(_ -> {
            utiliserIAAssistanceChoco();
            activerSelection(vue.boutonChoco, vue.boutonHeuristique);
        });

        vue.boutonHeuristique.setOnAction(_ -> {
            utiliserIAAssistanceHeuristique();
            activerSelection(vue.boutonHeuristique, vue.boutonChoco);
        });

        vue.boutonTriche.setOnAction(_ -> {
            activerTriche();
            activerSelection(vue.boutonTriche, vue.boutonSansTriche);
        });

        vue.boutonSansTriche.setOnAction(_ -> {
            desactiverTriche();
            activerSelection(vue.boutonSansTriche, vue.boutonTriche);
        });
    }

    private void activerSelection(Button actif, Button inactif) {
        actif.setDisable(true);
        actif.setOpacity(0.5);
        inactif.setDisable(false);
        inactif.setOpacity(1.0);
    }

    public IAAssistance getIaAssistanceActive() {
        return iaAssistanceChocoActive ? iaAssistanceChoco : iaAssistanceHeuristique;
    }
}
