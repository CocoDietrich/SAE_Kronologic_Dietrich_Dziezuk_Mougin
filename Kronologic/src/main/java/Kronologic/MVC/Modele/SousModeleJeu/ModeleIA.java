package Kronologic.MVC.Modele.SousModeleJeu;

import Kronologic.IA.IAAssistance.IAAssistanceChocoSolver;
import Kronologic.IA.IAAssistance.IAAssistanceHeuristique;
import Kronologic.IA.IADeduction.IADeductionChocoSolver;
import Kronologic.IA.IADeduction.IADeductionHeuristique;
import Kronologic.Jeu.Indice.Indice;
import Kronologic.Jeu.Indice.IndicePersonnage;
import Kronologic.Jeu.Indice.IndiceTemps;
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
    private final IAAssistanceChocoSolver iaAssistanceChocoSolver;
    private final IAAssistanceHeuristique iaAssistanceHeuristique;
    private final Partie partie;
    private final List<Observateur> observateurs;
    private boolean iaAssistanceChocoActive = true;
    private boolean iaTricheActive = true;

    public ModeleIA(Partie partie) {
        this.observateurs = new ArrayList<>();
        this.partie = partie;
        this.iaDeductionChocoSolver = new IADeductionChocoSolver(partie);
        this.iaDeductionHeuristique = new IADeductionHeuristique(partie);
        this.iaAssistanceChocoSolver = new IAAssistanceChocoSolver(this.iaDeductionChocoSolver, partie);
        this.iaAssistanceHeuristique = new IAAssistanceHeuristique(this.iaDeductionHeuristique, partie);
    }

    // Méthode pour afficher les déductions de l'IA ChocoSolver
    public String voirDeductionIAChocoSolver() {
        return iaDeductionChocoSolver.afficherHistoriqueDeduction();
    }

    // Méthode pour afficher les déductions de l'IA Heuristique
    public String voirDeductionIAHeuristique() {
        return iaDeductionHeuristique.afficherHistoriqueDeduction();
    }

    public String afficherMauvaisesDeductions() {
        return iaAssistanceChocoSolver.corrigerDeductions();
    }

    public void ajoutContraintesQuestion(Indice i){
        if (i instanceof IndicePersonnage ip){
            // Ajouter contraintes publiques et privées
            iaDeductionChocoSolver.poserQuestionPersonnage(
                    ip.getPersonnage(), ip.getLieu(), ip.getInfoPublic(), ip.getInfoPrive()
            );
            iaDeductionHeuristique.poserQuestionPersonnage(
                    ip.getPersonnage(), ip.getLieu(), ip.getInfoPublic(), ip.getInfoPrive()
            );
        }
        else {
            // Ajouter contraintes publiques et privées
            IndiceTemps it = (IndiceTemps) i;
            iaDeductionChocoSolver.poserQuestionTemps(
                    it.getLieu(), it.getTemps(), it.getInfoPublic(), it.getInfoPrive()
            );
            iaDeductionHeuristique.poserQuestionTemps(
                    it.getLieu(), it.getTemps(), it.getInfoPublic(), it.getInfoPrive()
            );
        }
    }

    public String demanderIndice() {
        String[] question;

        if (iaAssistanceChocoActive) {
            question = iaTricheActive
                    ? iaAssistanceChocoSolver.recommanderQuestionOptimaleTriche()
                    : iaAssistanceChocoSolver.recommanderQuestionOptimaleTrichePas();
        } else {
            question = iaTricheActive
                    ? iaAssistanceHeuristique.recommanderQuestionOptimaleTriche()
                    : iaAssistanceHeuristique.recommanderQuestionOptimaleTrichePas();
        }

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
        this.iaTricheActive = true;
    }

    public void desactiverTriche() {
        this.iaTricheActive = false;
    }

    // Méthode affichant le pop-up de demande d'indice à l'IA
    public void afficherPopUpDemanderIndice() {
        VuePopUpDemanderIndice vuePopUpDemanderIndice = (VuePopUpDemanderIndice) observateurs.stream().filter(o -> o instanceof VuePopUpDemanderIndice).findFirst().orElse(null);

        assert vuePopUpDemanderIndice != null;
        vuePopUpDemanderIndice.afficherPopUp();

        // IA
        vuePopUpDemanderIndice.boutonChoco.setOnAction(e -> {
            utiliserIAAssistanceChoco();
            activerSelection(vuePopUpDemanderIndice.boutonChoco, vuePopUpDemanderIndice.boutonHeuristique);
        });

        vuePopUpDemanderIndice.boutonHeuristique.setOnAction(e -> {
            utiliserIAAssistanceHeuristique();
            activerSelection(vuePopUpDemanderIndice.boutonHeuristique, vuePopUpDemanderIndice.boutonChoco);
        });

// Triche
        vuePopUpDemanderIndice.boutonTriche.setOnAction(e -> {
            activerTriche();
            activerSelection(vuePopUpDemanderIndice.boutonTriche, vuePopUpDemanderIndice.boutonSansTriche);
        });

        vuePopUpDemanderIndice.boutonSansTriche.setOnAction(e -> {
            desactiverTriche();
            activerSelection(vuePopUpDemanderIndice.boutonSansTriche, vuePopUpDemanderIndice.boutonTriche);
        });


    }

    private void activerSelection(Button actif, Button inactif) {
        actif.setDisable(true);
        actif.setOpacity(0.5); // effet transparent
        inactif.setDisable(false);
        inactif.setOpacity(1.0); // effet normal
    }


}
