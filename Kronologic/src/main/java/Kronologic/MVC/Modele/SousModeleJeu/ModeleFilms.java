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

    // TODO : à revoir (comment modifier la vue avec les données appropriées)
    public void actualiserFilmRealite(ModeleJeu modeleJeu) {
        VueFilmRealite vueFilmRealite = null;
        for (Observateur o : observateurs) {
            if (o instanceof VueFilmRealite) {
                vueFilmRealite = (VueFilmRealite) o;
                break;
            }
        }
        assert vueFilmRealite != null;

        // On supprime et recrée la carte pour éviter les doublons
        vueFilmRealite.getChildren().removeIf(node ->
                node instanceof HBox && "carte".equals(node.getId())
        );

        HBox nouvelleHbox = vueFilmRealite.afficherCarte(modeleJeu);
        vueFilmRealite.add(nouvelleHbox, 1, 1);

        // Actualisation finale
        vueFilmRealite.actualiser();
    }

    // TODO : à revoir (comment modifier la vue avec les données appropriées)
    public void actualiserFilmJoueur(ModeleJeu modeleJeu) {
        VueFilmJoueur vueFilmJoueur = null;
        for (Observateur o : observateurs) {
            if (o instanceof VueFilmJoueur) {
                vueFilmJoueur = (VueFilmJoueur) o;
                break;
            }
        }
        assert vueFilmJoueur != null;

        // On supprime et recrée la carte pour éviter les doublons
        vueFilmJoueur.getChildren().removeIf(node ->
                node instanceof HBox && "carte".equals(node.getId())
        );

        List<HBox> nouvelleHbox = vueFilmJoueur.afficherCarte(modeleJeu);
        vueFilmJoueur.add(nouvelleHbox.getFirst(), 0, 1);
        vueFilmJoueur.add(nouvelleHbox.getLast(), 0, 2);
        setColumnSpan(nouvelleHbox.getFirst(), 2);
        setColumnSpan(nouvelleHbox.getLast(), 2);

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
