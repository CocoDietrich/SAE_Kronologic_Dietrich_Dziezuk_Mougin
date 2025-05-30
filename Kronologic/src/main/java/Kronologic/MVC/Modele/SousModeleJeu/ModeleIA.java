package Kronologic.MVC.Modele.SousModeleJeu;

import Kronologic.IA.IAAssistance.*;
import Kronologic.IA.IADeduction.IADeductionChocoSolver;
import Kronologic.IA.IADeduction.IADeductionHeuristique;
import Kronologic.Jeu.Indice.*;
import Kronologic.Jeu.Partie;
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

    private final Partie partie;

    public ModeleIA(Partie partie) {
        this.partie = partie;
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

    public void activerTriche(int strategie) {
        this.iaAssistanceChoco = new IAAssistanceChocoTriche(iaDeductionChocoSolver, partie);
        this.iaAssistanceChoco.setModeRecommandation(strategie);
        this.iaAssistanceHeuristique = new IAAssistanceHeuristiqueTriche(iaDeductionHeuristique, partie);
        this.iaAssistanceHeuristique.setModeRecommandation(strategie);
    }

    public void desactiverTriche(int strategie) {
        this.iaAssistanceChoco = new IAAssistanceChocoTrichePas(iaDeductionChocoSolver, partie);
        this.iaAssistanceChoco.setModeRecommandation(strategie);
        this.iaAssistanceHeuristique = new IAAssistanceHeuristiqueTrichePas(iaDeductionHeuristique, partie);
        this.iaAssistanceHeuristique.setModeRecommandation(strategie);
    }


    public void afficherPopUpDemanderIndice() {
        VuePopUpDemanderIndice vue = (VuePopUpDemanderIndice) observateurs.stream()
                .filter(o -> o instanceof VuePopUpDemanderIndice)
                .findFirst()
                .orElse(null);

        assert vue != null;
        vue.afficherPopUp();

        activerSelection(vue.getBoutonMax(), vue.getBoutonTriche());
        activerSelection(vue.getBoutonMin(), vue.getBoutonTriche());
        activerSelection(vue.getBoutonMoyenne(), vue.getBoutonTriche());

        vue.getBoutonChoco().setOnAction(_ -> {
            utiliserIAAssistanceChoco();
            activerSelection(vue.getBoutonChoco(), vue.getBoutonHeuristique());
        });

        vue.getBoutonHeuristique().setOnAction(_ -> {
            utiliserIAAssistanceHeuristique();
            activerSelection(vue.getBoutonHeuristique(), vue.getBoutonChoco());
        });

        vue.getBoutonTriche().setOnAction(_ -> {
            activerTriche(iaAssistanceChoco.getModeRecommandation());
            activerSelection(vue.getBoutonTriche(), vue.getBoutonSansTriche());
            activerSelection(vue.getBoutonMax(), vue.getBoutonSansTriche());
            activerSelection(vue.getBoutonMin(), vue.getBoutonSansTriche());
            activerSelection(vue.getBoutonMoyenne(), vue.getBoutonSansTriche());
        });

        vue.getBoutonSansTriche().setOnAction(_ -> {
            desactiverTriche(iaAssistanceChoco.getModeRecommandation());
            activerSelection(vue.getBoutonSansTriche(), vue.getBoutonTriche());
            switch (iaAssistanceChoco.getModeRecommandation()){
                case 0 :
                    activerSelection(vue.getBoutonMin(), vue.getBoutonMax());
                    activerSelection(vue.getBoutonMin(), vue.getBoutonMoyenne());
                    break;
                case 1 :
                    activerSelection(vue.getBoutonMax(), vue.getBoutonMin());
                    activerSelection(vue.getBoutonMax(), vue.getBoutonMoyenne());
                    break;
                case 2 :
                    activerSelection(vue.getBoutonMoyenne(), vue.getBoutonMax());
                    activerSelection(vue.getBoutonMoyenne(), vue.getBoutonMin());
                    break;
            }
        });

        vue.getBoutonMin().setOnAction(_ -> {
            iaAssistanceChoco.setModeRecommandation(0);
            iaAssistanceHeuristique.setModeRecommandation(0);
            activerSelection(vue.getBoutonMin(), vue.getBoutonMax());
            activerSelection(vue.getBoutonMin(), vue.getBoutonMoyenne());
        });

        vue.getBoutonMax().setOnAction(_ -> {
            iaAssistanceChoco.setModeRecommandation(1);
            iaAssistanceHeuristique.setModeRecommandation(1);
            activerSelection(vue.getBoutonMax(), vue.getBoutonMin());
            activerSelection(vue.getBoutonMax(), vue.getBoutonMoyenne());
        });

        vue.getBoutonMoyenne().setOnAction(_ -> {
            iaAssistanceChoco.setModeRecommandation(2);
            iaAssistanceHeuristique.setModeRecommandation(2);
            activerSelection(vue.getBoutonMoyenne(), vue.getBoutonMax());
            activerSelection(vue.getBoutonMoyenne(), vue.getBoutonMin());
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