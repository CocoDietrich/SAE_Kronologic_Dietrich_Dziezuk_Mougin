package Kronologic.MVC.Modele.SousModeleJeu;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.Jeu.Indice.Indice;
import Kronologic.Jeu.Partie;
import Kronologic.MVC.Modele.ModeleJeu;
import Kronologic.MVC.Modele.Sujet;
import Kronologic.MVC.Vue.Observateur;
import Kronologic.MVC.Vue.PopUps.VuePopUpDeduction;
import Kronologic.MVC.Vue.PopUps.VuePopUpPoseQuestion;
import Kronologic.MVC.Vue.VueDeduction;
import Kronologic.MVC.Vue.VuePoseQuestion;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ModeleQuestionDeduction implements Sujet {

    private final Partie partie;
    private final List<Observateur> observateurs;

    public ModeleQuestionDeduction(Partie partie) {
        this.observateurs = new ArrayList<>();
        this.partie = partie;
    }

    private <T extends Observateur> T getVue(Class<T> type) {
        for (Observateur o : observateurs) {
            if (type.isInstance(o)) {
                return type.cast(o);
            }
        }
        return null;
    }

    // Méthode permettant de stocker le lieu choisi pour la question posée ou la déduction faite par le joueur
    public void setLieuChoisi(Lieu lieu, Observateur vue) {
        if (vue instanceof VuePoseQuestion vuePoseQuestion) {
            vuePoseQuestion.lieuChoisi = lieu;
        } else if (vue instanceof VueDeduction vueDeduction) {
            vueDeduction.lieuMeurtre = lieu;
        }
    }

    // Méthode permettant de stocker le temps choisi pour la question posée ou la déduction faite par le joueur
    public void setTempsChoisi(Temps temps, Observateur vue) {
        if (vue instanceof VuePoseQuestion vuePoseQuestion) {
            vuePoseQuestion.tempsChoisi = temps;
            vuePoseQuestion.personnageChoisi = null;
        } else if (vue instanceof VueDeduction vueDeduction) {
            vueDeduction.tempsMeurtre = temps;
        }
    }

    // Méthode permettant de stocker le personnage choisi pour la question posée ou la déduction faite par le joueur
    public void setPersonnageChoisi(Personnage personnage, Observateur vue) {
        if (vue instanceof VuePoseQuestion vuePoseQuestion) {
            vuePoseQuestion.personnageChoisi = personnage;
            vuePoseQuestion.tempsChoisi = null;
        } else if (vue instanceof VueDeduction vueDeduction) {
            vueDeduction.meurtrier = personnage;
        }
    }

    public Indice poserQuestion(Stage stage, ModeleJeu modeleJeu) {
        VuePoseQuestion vuePoseQuestion = getVue(VuePoseQuestion.class);
        if (vuePoseQuestion == null) return null;


        Indice indice;
        if (vuePoseQuestion.personnageChoisi != null) {
            indice = partie.poserQuestionPersonnage(vuePoseQuestion.lieuChoisi, vuePoseQuestion.personnageChoisi);
        } else {
            indice = partie.poserQuestionTemps(vuePoseQuestion.lieuChoisi, vuePoseQuestion.tempsChoisi);
        }

        partie.ajouterIndice(indice);
        notifierObservateurs();
        System.out.println("Réponse à la question posée : " + indice);

        VuePopUpPoseQuestion vuePopUpPoseQuestion = getVue(VuePopUpPoseQuestion.class);
        if (vuePopUpPoseQuestion != null) {
            vuePopUpPoseQuestion.afficherPopUp(indice);
        }

        if (modeleJeu.estVueCarte()) {
            modeleJeu.retourVueCarte(stage);
        } else {
            modeleJeu.retourVueTableau(stage);
        }
        return indice;
    }

    public void faireDeduction() {
        VueDeduction vueDeduction = getVue(VueDeduction.class);
        if (vueDeduction == null) return;

        boolean resultat = partie.faireDeduction(vueDeduction.lieuMeurtre, vueDeduction.meurtrier, vueDeduction.tempsMeurtre);
        VuePopUpDeduction vuePopUpDeduction = getVue(VuePopUpDeduction.class);

        if (vuePopUpDeduction != null) {
            vuePopUpDeduction.afficherPopUp(resultat, partie.verifierLoupe());
        }

        notifierObservateurs();
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
}
