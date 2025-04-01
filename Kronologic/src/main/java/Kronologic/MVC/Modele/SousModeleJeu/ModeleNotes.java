package Kronologic.MVC.Modele.SousModeleJeu;

import Kronologic.Jeu.Elements.*;
import Kronologic.Jeu.Enums.ImageNombres;
import Kronologic.Jeu.Enums.ImagePersonnages;
import Kronologic.Jeu.Partie;
import Kronologic.MVC.Modele.Sujet;
import Kronologic.MVC.Vue.Observateur;
import Kronologic.MVC.Vue.VueCarte;
import javafx.scene.image.Image;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModeleNotes implements Sujet {

    private final Partie partie;
    private final List<Observateur> observateurs;

    public ModeleNotes(Partie partie) {
        this.observateurs = new ArrayList<>();
        this.partie = partie;
    }

    // Méthode permettant de stocker les déplacements de pions de personnage du joueur
    public void ajouterNote(Lieu l, Temps t, Personnage p) {
        Note n = new Note(l, t, p);
        VueCarte vueCarte = getVueCarte();
        if (vueCarte == null) return;

        Pion pion = new Pion(n,
                Objects.requireNonNull(ImagePersonnages.get(ImagePersonnages.getPersonnages().indexOf(p.getNom()))).getUrl());
        pion.setUserData(t.getValeur() + "-" + l.getNom() + "-");

        ajouterPion(n, Objects.requireNonNull(ImagePersonnages.get(ImagePersonnages.getPersonnages().indexOf(p.getNom()))), 0, 0);


        vueCarte.getPions().add(pion);
        notifierObservateurs();
    }

    // Méthode permettant de stocker les déplacements de pions de nombers du joueur
    public void ajouterNote(Lieu l, Temps t, int nbPersonnages) {
        Note n = new Note(l, t, nbPersonnages);
        VueCarte vueCarte = getVueCarte();
        if (vueCarte == null) return;

        // On ajoute la note à la liste des notes
        Pion pion = new Pion(n,
                Objects.requireNonNull(ImageNombres.get(ImageNombres.getNombres().indexOf(nbPersonnages)+1)).getUrl());
        pion.setUserData(t.getValeur() + "-" + l.getNom() + "-");

        ajouterPion(n, Objects.requireNonNull(ImageNombres.get(ImageNombres.getNombres().indexOf(nbPersonnages)+1)), 0, 0);

        vueCarte.getPions().add(pion);
        notifierObservateurs();
    }

    public void modifierNote(Lieu l, Temps t, Personnage p, boolean absence, boolean hypothese) {
        // On retrouve la note correspondante au pion placé
        Note n = null;
        for (Note note : partie.getGestionnaireNotes().getNotes()) {
            if (note.getLieu().getNom().equals(l.getNom())
                    && note.getTemps().getValeur() == t.getValeur()
                    && note.getPersonnage().getNom().equals(p.getNom())) {
                n = note;
                break;
            }
        }
        if (n == null) return;

        VueCarte vueCarte = getVueCarte();
        if (vueCarte == null) return;

        // On modifie la note
        partie.modifierNote(n, absence, hypothese);

        for (Pion pion : vueCarte.getPions()) {
            if (pion.getNote() != null
                    && pion.getNote().equals(n)) {
                pion.getNote().setEstAbsence(absence);
                pion.getNote().setEstHypothese(hypothese);
            }
        }

        notifierObservateurs();
    }

    public void modifierNote(Lieu l, Temps t, int nbPersonnage, boolean absence, boolean hypothese) {
        // On retrouve la note correspondante au pion placé
        Note n = null;
        for (Note note : partie.getGestionnaireNotes().getNotes()) {
            if (note.getLieu().getNom().equals(l.getNom())
                    && note.getTemps().getValeur() == t.getValeur()
                    && note.getNbPersonnages() == nbPersonnage) {
                n = note;
                break;
            }
        }

        if (n == null) return;

        VueCarte vueCarte = getVueCarte();
        if (vueCarte == null) return;

        // On modifie la note
        partie.modifierNote(n, absence, hypothese);

        for (Pion pion : vueCarte.getPions()) {
            if (pion.getNote() != null && pion.getNote().equals(n)) {
                pion.getNote().setEstAbsence(absence);
                pion.getNote().setEstHypothese(hypothese);
            }
        }

        notifierObservateurs();
    }

    // Méthode permettant de supprimer une note du joueur
    public void supprimerNote(Lieu l, Temps t, Personnage p) {
        // On retrouve la note correspondante au pion placé
        Note n = null;
        for (Note note : partie.getGestionnaireNotes().getNotes()) {
            if (note.getLieu().getNom().equals(l.getNom())
                    && note.getTemps().getValeur() == t.getValeur()
                    && note.getPersonnage().getNom().equals(p.getNom())) {
                n = note;
                break;
            }
        }

        if (n == null) return;

        VueCarte vueCarte = getVueCarte();
        if (vueCarte == null) return;

        for (Pion pion : vueCarte.getPions()) {
            if (pion.getNote() != null && pion.getNote().equals(n)) {
                vueCarte.getPions().remove(pion);
                vueCarte.getChildren().remove(pion);
                supprimerPion(pion);
                for (Polygon zone : vueCarte.getZonesContenantPions()) {
                    if (zone.getUserData().equals(pion.getUserData())) {
                        vueCarte.getZonesContenantPions().remove(zone);
                        break;
                    }
                }
                break;
            }
        }

        if (partie.getGestionnaireNotes().getNotes().contains(n)){
            partie.supprimerNote(n);
        }

        notifierObservateurs();
    }

    public void supprimerNote(Lieu l, Temps t, int nbPersonnages) {
        // On retrouve la note correspondante au pion placé
        Note n = null;
        for (Note note : partie.getGestionnaireNotes().getNotes()) {
            if (note.getLieu().getNom().equals(l.getNom())
                    && note.getTemps().getValeur() == t.getValeur()
                    && note.getNbPersonnages() == nbPersonnages) {
                n = note;
                break;
            }
        }

        if (n == null) return;

        VueCarte vueCarte = getVueCarte();
        if (vueCarte == null) return;

        for (Pion pion : vueCarte.getPions()) {
            if (pion.getNote() != null && pion.getNote().equals(n)) {
                vueCarte.getPions().remove(pion);
                vueCarte.getChildren().remove(pion);
                supprimerPion(pion);
                for (Polygon zone : vueCarte.getZonesContenantPions()) {
                    if (zone.getUserData().equals(pion.getUserData())) {
                        vueCarte.getZonesContenantPions().remove(zone);
                        break;
                    }
                }
                break;
            }
        }

        if (partie.getGestionnaireNotes().getNotes().contains(n)){
            partie.supprimerNote(n);
        }

        notifierObservateurs();
    }

    public void ajouterPion(Note note, Image image, int x, int y) {
        Pion pion = new Pion(note, image.getUrl());
        pion.deplacerPion(x, y);
        partie.ajouterPion(pion);
    }

    public void deplacerPion(Pion pion, Lieu nouveauLieu, Temps nouveauTemps, int x, int y) {
        partie.deplacerPion(pion, nouveauLieu, nouveauTemps, x, y);
    }

    public void supprimerPion(Pion pion) {
        partie.supprimerPion(pion);
    }

    private VueCarte getVueCarte() {
        for (Observateur o : observateurs) {
            if (o instanceof VueCarte) {
                return (VueCarte) o;
            }
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

    public Partie getPartie() {
        return partie;
    }
}
