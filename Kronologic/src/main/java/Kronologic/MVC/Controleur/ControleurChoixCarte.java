package Kronologic.MVC.Controleur;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Note;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.MVC.Modele.ModeleJeu;
import Kronologic.Jeu.Elements.Pion;
import Kronologic.MVC.Vue.Observateur;
import Kronologic.MVC.Vue.VueCarte;
import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.Objects;

public class ControleurChoixCarte implements EventHandler<DragEvent> {

    private ModeleJeu modeleJeu;

    public ControleurChoixCarte(ModeleJeu modeleJeu) {
        this.modeleJeu = modeleJeu;
    }

    @Override
    public void handle(DragEvent dragEvent) {
        System.out.println("--------------------");
        Pion pionAvant = (Pion) dragEvent.getGestureSource();

        VueCarte vueCarte = null;
        for (Observateur observateur : modeleJeu.getObservateurs()) {
            if (observateur instanceof VueCarte) {
                vueCarte = (VueCarte) observateur;
            }
        }
        assert vueCarte != null;

        Pion pionActuel = null;
        if (vueCarte.pions.getLast().getParent() == null && vueCarte.pions.get(vueCarte.pions.size() - 2).getParent() instanceof VueCarte) {
            pionActuel = vueCarte.pions.get(vueCarte.pions.size() - 2);
        } else {
            pionActuel = vueCarte.pions.getLast();
        }

        for (Pion p : vueCarte.pions) {
            System.out.println("user data : " + p.getUserData() + " - Id : " + p.getId());
        }

        // Si un pion représentant le même personnage ou le pion de nombre est déjà présent dans le même lieu et au même temps
        // On ne peut pas ajouter un autre pion représentant le même personnage ou le pion de nombre

        if (pionActuel.getId() != null) {
            // Si c'est un pion de personnage
            String nomPersonnagePionActuel = pionActuel.getId().substring(pionActuel.getId().lastIndexOf("/")+1, pionActuel.getId().lastIndexOf("."));
            if (nomPersonnagePionActuel.contains("Pion de Nombres")) {
                // On récupère les pions de nombres déjà placés
                ArrayList<Pion> pionsDeNombres = new ArrayList<>();
                for (Pion p : vueCarte.pions) {
                    if (p.getUserData() != null) {
                        if (p.getId().contains("Pion de Nombres")) {
                            pionsDeNombres.add(p);
                        }
                    }
                }

                // On supprime le pion actuel de la liste
                pionsDeNombres.removeLast();

                for (Pion p : pionsDeNombres) {
                    System.out.println("Pion de nombres : " + p.getUserData() + "-" + p.getId());
                }

                // Si un d'eux est placé dans le même lieu et au même temps, on ne fait rien
                /*String lieuTempsPionActuel = pionActuel.getUserData().toString().substring(0, pionActuel.getUserData().toString().lastIndexOf("-"));
                for (Pion p : pionsDeNombres) {
                    String lieuTempsPion = p.getUserData().toString().substring(0, p.getUserData().toString().lastIndexOf("-"));
                    if (lieuTempsPion.equals(lieuTempsPionActuel)) {
                        System.out.println("Pion de nombres déjà placé dans le même lieu et au même temps");
                        vueCarte.getChildren().removeLast();
                        vueCarte.zonesContenantPions.removeLast();
                        for (Polygon poly : vueCarte.zonesContenantPions) {
                            System.out.println("Zone : " + poly.getUserData());
                        }
                        return;
                    }
                }*/
            } else {
                // On récupère les pions de ce personnage déjà placés
                ArrayList<Pion> pionsMemePersonnage = new ArrayList<>();
                for (Pion p : vueCarte.pions) {
                    if (p.getUserData() != null) {
                        if (p.getId().contains(nomPersonnagePionActuel)) {
                            pionsMemePersonnage.add(p);
                        }
                    }
                }

                // On supprime le pion actuel de la liste
                pionsMemePersonnage.removeLast();

                // Si un d'eux est placé dans le même lieu et au même temps, on ne fait rien
                String lieuTempsPionActuel = pionActuel.getUserData().toString().substring(0, pionActuel.getUserData().toString().lastIndexOf("-"));
                for (Pion p : pionsMemePersonnage) {
                    String lieuTempsPion = p.getUserData().toString().substring(0, p.getUserData().toString().lastIndexOf("-"));
                    if (lieuTempsPion.equals(lieuTempsPionActuel)) {
                        System.out.println("Pion de personnage déjà placé dans le même lieu et au même temps");
                        vueCarte.getChildren().removeLast();
                        vueCarte.zonesContenantPions.removeLast();
                        for (Polygon poly : vueCarte.zonesContenantPions) {
                            System.out.println("Zone : " + poly.getUserData());
                        }
                        return;
                    }
                }
            }
        }

        // Cas du pion déplacé depuis la réserve de pion vers un lieu
        if ((pionAvant.getUserData() == null) || (pionAvant.getUserData().getClass() == Integer.class)) {
            // On récupère le lieu et le temps ciblé
            String nomLieu = ((String) pionActuel.getUserData()).split("-")[1];
            Lieu nouveauLieu = null;
            for (Lieu lieu : ModeleJeu.getPartie().getElements().getLieux()) {
                if (lieu.getNom().equals(nomLieu)) {
                    nouveauLieu = lieu;
                }
            }
            // Récupérer Temps
            String nomTemps = ((String) pionActuel.getUserData()).split("-")[0];
            Temps temps = new Temps(Integer.parseInt(String.valueOf(nomTemps.charAt(nomTemps.length() - 1))));

            Note note = null;
            if ((pionActuel.getImage().getUrl().split("/"))[2].split(".png")[0].contains("Pion de Nombres")){
                // On crée la note
                note = new Note(nouveauLieu, temps, Integer.parseInt((pionActuel.getImage().getUrl().split("/"))[2].split(".png")[0].split("_")[1]));
            } else {
                // On crée la note
                note = new Note(nouveauLieu, temps, new Personnage((pionActuel.getImage().getUrl().split("/"))[2].split(".png")[0]));
            }

            vueCarte.pions.getLast().setNote(note);
            modeleJeu.ajouterPion(note, pionActuel.getImage(), (int) pionActuel.getLayoutX(), (int) pionActuel.getLayoutY());

        } else if (!Objects.equals(pionAvant.getId(), pionActuel.getId())) {
            // Cas du pion déplacé depuis un lieu vers un autre lieu
            modeleJeu.supprimerPion(pionAvant);
            vueCarte.pions.removeLast();
            vueCarte.pions.remove(pionAvant);
            for (Polygon p : vueCarte.zonesContenantPions) {
                if (p.getUserData() == pionAvant.getUserData()) {
                    vueCarte.zonesContenantPions.remove(p);
                    break;
                }
            }
        } else {
            // On récupère le lieu et le temps ciblé
            String nomLieu = ((String) pionActuel.getUserData()).split("-")[1];
            Lieu nouveauLieu = null;
            for (Lieu lieu : ModeleJeu.getPartie().getElements().getLieux()) {
                if (lieu.getNom().equals(nomLieu)) {
                    nouveauLieu = lieu;
                }
            }
            // Récupérer Temps
            String nomTemps = ((String) pionActuel.getUserData()).split("-")[0];
            Temps temps = new Temps(Integer.parseInt(String.valueOf(nomTemps.charAt(nomTemps.length() - 1))));

            System.out.println(pionActuel.getImage().getUrl().split("/")[2].split(".png")[0]);

            Note note = null;
            if ((pionActuel.getImage().getUrl().split("/"))[2].split(".png")[0].contains("Pion de Nombres")){
                // On crée la note
                note = new Note(nouveauLieu, temps, Integer.parseInt((pionActuel.getImage().getUrl().split("/"))[2].split(".png")[0].split("_")[1]));
            } else {
                // On crée la note
                note = new Note(nouveauLieu, temps, new Personnage((pionActuel.getImage().getUrl().split("/"))[2].split(".png")[0]));
            }

            System.out.println("Note : " + note);

            vueCarte.pions.get(vueCarte.pions.size() - 2).setNote(note);

            System.out.println("Pion avant : " + pionAvant);
            System.out.println("Nouveau lieu : " + nouveauLieu);
            System.out.println("Temps : " + temps);

            modeleJeu.deplacerPion(pionAvant, nouveauLieu, temps, (int) pionActuel.getLayoutX(), (int) pionActuel.getLayoutY());
            vueCarte.pions.remove(pionAvant);

            for (Polygon p : vueCarte.zonesContenantPions) {
                if (p.getUserData() == pionAvant.getUserData()) {
                    vueCarte.zonesContenantPions.remove(p);
                    break;
                }
            }
        }
        // On supprime les doublons dans la liste des zones contenant les pions
        for (int i = 0; i < vueCarte.zonesContenantPions.size(); i++) {
            for (int j = i + 1; j < vueCarte.zonesContenantPions.size(); j++) {
                if (vueCarte.zonesContenantPions.get(i).getUserData() == vueCarte.zonesContenantPions.get(j).getUserData()) {
                    vueCarte.zonesContenantPions.remove(j);
                }
            }
        }

        for (Polygon p : vueCarte.zonesContenantPions) {
            System.out.println("Zone : " + p.getUserData());
        }

        System.out.println("Liste des notes : ");
        for (Note n : ModeleJeu.getPartie().getGestionnaireNotes().getNotes()) {
            System.out.println(n);
        }
    }
}
