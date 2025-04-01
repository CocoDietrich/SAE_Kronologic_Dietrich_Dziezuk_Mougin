package Kronologic.MVC.Modele.SousModeleJeu;

import Kronologic.MVC.Modele.ModeleJeu;
import Kronologic.MVC.Modele.Sujet;
import Kronologic.MVC.Vue.Observateur;
import Kronologic.MVC.Vue.VueFilmJoueur;
import Kronologic.MVC.Vue.VueFilmRealite;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

import static javafx.scene.layout.GridPane.setColumnSpan;

public class ModeleFilms implements Sujet {

    private final List<Observateur> observateurs;

    public ModeleFilms() {
        this.observateurs = new ArrayList<>();
    }

    public void actualiserFilmRealite(ModeleJeu modeleJeu) {
        VueFilmRealite vue = trouverObservateur(VueFilmRealite.class);
        if (vue == null) return;

        vue.getChildren().removeIf(node ->
                node instanceof HBox && "carte".equals(node.getId())
        );

        HBox nouvelleHbox = vue.afficherCarte(modeleJeu);
        vue.add(nouvelleHbox, 1, 1);
        vue.actualiser();
    }

    public void actualiserFilmJoueur(ModeleJeu modeleJeu) {
        VueFilmJoueur vue = trouverObservateur(VueFilmJoueur.class);
        if (vue == null) return;

        vue.getChildren().removeIf(node ->
                node instanceof HBox && "carte".equals(node.getId())
        );

        List<HBox> nouvelles = vue.afficherCarte(modeleJeu);
        if (nouvelles.size() >= 2) {
            vue.add(nouvelles.getFirst(), 0, 1);
            vue.add(nouvelles.getLast(), 0, 2);
            setColumnSpan(nouvelles.getFirst(), 2);
            setColumnSpan(nouvelles.getLast(), 2);
        }

        notifierObservateurs();
    }

    private <T extends Observateur> T trouverObservateur(Class<T> type) {
        for (Observateur o : observateurs) {
            if (type.isInstance(o)) return type.cast(o);
        }
        return null;
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