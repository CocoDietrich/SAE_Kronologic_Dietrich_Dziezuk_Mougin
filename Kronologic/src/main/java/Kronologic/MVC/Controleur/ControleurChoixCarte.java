package Kronologic.MVC.Controleur;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Note;
import Kronologic.Jeu.Elements.Temps;
import Kronologic.MVC.Modele.ModeleJeu;
import Kronologic.Jeu.Elements.Pion;
import Kronologic.MVC.Modele.SousModeleJeu.ModeleNotes;
import Kronologic.MVC.Vue.Observateur;
import Kronologic.MVC.Vue.VueCarte;
import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.Objects;

// TODO : A REFACTORISER
public class ControleurChoixCarte implements EventHandler<DragEvent> {

    private final ModeleNotes modeleNotes;

    public ControleurChoixCarte(ModeleNotes modeleNotes) {
        this.modeleNotes = modeleNotes;
    }

    @Override
    public void handle(DragEvent dragEvent) {
        Pion pionAvant = (Pion) dragEvent.getGestureSource();

        VueCarte vueCarte = null;
        for (Observateur observateur : modeleNotes.getObservateurs()) {
            if (observateur instanceof VueCarte) {
                vueCarte = (VueCarte) observateur;
            }
        }
        assert vueCarte != null;

        Pion pionActuel;
        if (vueCarte.getPions().getLast().getParent() == null
                && vueCarte.getPions().get(vueCarte.getPions().size() - 2).getParent() instanceof VueCarte) {
            pionActuel = vueCarte.getPions().get(vueCarte.getPions().size() - 2);
        } else {
            pionActuel = vueCarte.getPions().getLast();
        }
        // Si un pion représentant le même personnage ou le pion de nombre est déjà présent dans le même lieu et au même temps
        // On ne peut pas ajouter un autre pion représentant le même personnage ou le pion de nombre

        if (pionActuel.getId() != null) {
            String nomPersonnagePionActuel = pionActuel.getId().substring(pionActuel.getId().lastIndexOf("/")+1,
                    pionActuel.getId().lastIndexOf("."));
            // Si c'est un pion de nombres
            if (nomPersonnagePionActuel.contains("Pion de Nombres")) {
                // On récupère les pions de nombres déjà placés
                ArrayList<Pion> pionsDeNombres = new ArrayList<>();
                for (Pion p : vueCarte.getPions()) {
                    if (p.getUserData() != null) {
                        if (p.getId().contains("Pion de Nombres")) {
                            pionsDeNombres.add(p);
                        }
                    }
                }

                // On supprime le pion actuel de la liste
                pionsDeNombres.removeLast();

                // Si un d'eux est placé dans le même lieu et au même temps, on ne fait rien
                String lieuTempsPionActuel = pionActuel.getUserData().toString().substring(0, pionActuel.getUserData().toString().lastIndexOf("-"));
                for (Pion p : pionsDeNombres) {
                    if (p.getNote() != null) {
                        String lieuTempsPion = p.getUserData().toString().substring(0, p.getUserData().toString().lastIndexOf("-"));
                        if (lieuTempsPion.equals(lieuTempsPionActuel)) {
                            vueCarte.getChildren().removeLast();
                            vueCarte.getZonesContenantPions().removeLast();
                            pionsDeNombres.removeLast();
                            return;
                        }
                    }
                }
            } else {
                // Si c'est un pion de personnage

                // On récupère les pions de ce personnage déjà placés
                ArrayList<Pion> pionsMemePersonnage = new ArrayList<>();
                for (Pion p : vueCarte.getPions()) {
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
                        if (vueCarte.getChildren().getLast() instanceof Pion) {
                            vueCarte.getChildren().removeLast();
                        }
                        vueCarte.getZonesContenantPions().removeLast();
                        vueCarte.getPions().removeLast();
                        if (pionAvant.getUserData() != null) {
                            vueCarte.getPions().remove(pionAvant);
                        }
                        for (Pion pion1 : vueCarte.getPions()) {
                            if (pion1.getUserData() != null && pionActuel.getUserData() != null) {
                                if (pion1.getUserData().equals(pionActuel.getUserData())) {
                                    if (pion1.getUserData().equals(vueCarte.getPions().getLast().getUserData())){
                                        vueCarte.getPions().removeLast();
                                    }
                                    else {
                                        vueCarte.getPions().remove(pion1);
                                    }
                                    break;
                                }
                            }
                        }
                        for (Pion pion2 : vueCarte.getPions()) {
                            if (pion2.getUserData() != null) {
                                if (pion2.getUserData().equals(pionActuel.getUserData())) {
                                    vueCarte.getPions().remove(pion2);
                                    break;
                                }
                            }
                        }
                        modeleNotes.supprimerPion(pionAvant);
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
            for (Lieu lieu : ModeleJeu.getPartie().getElements().lieux()) {
                if (lieu.getNom().equals(nomLieu)) {
                    nouveauLieu = lieu;
                }
            }
            // Récupérer Temps
            String nomTemps = ((String) pionActuel.getUserData()).split("-")[0];
            Temps temps = new Temps(Integer.parseInt(String.valueOf(nomTemps.charAt(nomTemps.length() - 1))));

            Note note;
            if ((pionActuel.getImage().getUrl().contains("Pion de Nombres"))) {
                // On crée la note
                if (vueCarte.getHypothese().isSelected() || vueCarte.getAbsence().isSelected()) {
                    int nombre = Integer.parseInt((pionActuel.getImage().getUrl().split("/"))[3].split(".png")[0].split("_")[1]);
                    note = new Note(nouveauLieu, temps, nombre);
                } else {
                    int nombre = Integer.parseInt((pionActuel.getImage().getUrl().split("/"))[2].split(".png")[0].split("_")[1]);
                    note = new Note(nouveauLieu, temps, nombre);
                }
            } else {
                // On crée la note
                if (vueCarte.getHypothese().isSelected() || vueCarte.getAbsence().isSelected()) {
                    Personnage personnage = new Personnage((pionActuel.getImage().getUrl().split("/"))[3].split(".png")[0]);
                    note = new Note(nouveauLieu, temps, personnage);
                } else {
                    Personnage personnage = new Personnage((pionActuel.getImage().getUrl().split("/"))[2].split(".png")[0]);
                    note = new Note(nouveauLieu, temps, personnage);
                }
            }

            // On vérifie si c'est une hypothèse ou une absence ou une hypothèse d'absence
            if (vueCarte.getHypothese().isSelected()) {
                note.setEstHypothese(true);
            }
            if (vueCarte.getAbsence().isSelected()) {
                note.setEstAbsence(true);
            }

            vueCarte.getPions().getLast().setNote(note);
            modeleNotes.ajouterPion(note, pionActuel.getImage(), (int) pionActuel.getLayoutX(), (int) pionActuel.getLayoutY());

        } else if (!Objects.equals(pionAvant.getId(), pionActuel.getId())) {
            // Cas du pion déplacé depuis un lieu vers un autre lieu
            modeleNotes.supprimerPion(pionAvant);
            vueCarte.getPions().removeLast();
            vueCarte.getPions().remove(pionAvant);
            for (Polygon p : vueCarte.getZonesContenantPions()) {
                if (p.getUserData() == pionAvant.getUserData()) {
                    vueCarte.getZonesContenantPions().remove(p);
                    break;
                }
            }
        } else {
            // On récupère le lieu et le temps ciblé
            String nomLieu = ((String) pionActuel.getUserData()).split("-")[1];
            Lieu nouveauLieu = null;
            for (Lieu lieu : ModeleJeu.getPartie().getElements().lieux()) {
                if (lieu.getNom().equals(nomLieu)) {
                    nouveauLieu = lieu;
                }
            }
            // Récupérer Temps
            String nomTemps = ((String) pionActuel.getUserData()).split("-")[0];
            Temps temps = new Temps(Integer.parseInt(String.valueOf(nomTemps.charAt(nomTemps.length() - 1))));

            Note note;
            if ((pionActuel.getImage().getUrl().contains("Pion de Nombres"))) {
                // On crée la note
                if (vueCarte.getHypothese().isSelected() || vueCarte.getAbsence().isSelected()) {
                    int nombre = Integer.parseInt((pionActuel.getImage().getUrl().split("/"))[3].split(".png")[0].split("_")[1]);
                    note = new Note(nouveauLieu, temps, nombre);
                } else {
                    int nombre = Integer.parseInt((pionActuel.getImage().getUrl().split("/"))[2].split(".png")[0].split("_")[1]);
                    note = new Note(nouveauLieu, temps, nombre);
                }
            } else {
                // On crée la note
                if (vueCarte.getHypothese().isSelected() || vueCarte.getAbsence().isSelected()) {
                    Personnage personnage = new Personnage((pionActuel.getImage().getUrl().split("/"))[3].split(".png")[0]);
                    note = new Note(nouveauLieu, temps, personnage);
                } else {
                    Personnage personnage = new Personnage((pionActuel.getImage().getUrl().split("/"))[2].split(".png")[0]);
                    note = new Note(nouveauLieu, temps, personnage);
                }
            }

            // On vérifie si c'est une hypothèse ou une absence ou une hypothèse d'absence
            if (vueCarte.getHypothese().isSelected()) {
                note.setEstHypothese(true);
            }
            if (vueCarte.getAbsence().isSelected()) {
                note.setEstAbsence(true);
            }

            vueCarte.getPions().get(vueCarte.getPions().size() - 2).setNote(note);

            modeleNotes.deplacerPion(pionAvant, nouveauLieu, temps, (int) pionActuel.getLayoutX(), (int) pionActuel.getLayoutY());
            vueCarte.getPions().remove(pionAvant);

            for (Polygon p : vueCarte.getZonesContenantPions()) {
                if (p.getUserData() == pionAvant.getUserData()) {
                    vueCarte.getZonesContenantPions().remove(p);
                    break;
                }
            }
        }
        // On supprime les doublons dans la liste des zones contenant les pions
        for (int i = 0; i < vueCarte.getZonesContenantPions().size(); i++) {
            for (int j = i + 1; j < vueCarte.getZonesContenantPions().size(); j++) {
                if (vueCarte.getZonesContenantPions().get(i).getUserData() == vueCarte.getZonesContenantPions().get(j).getUserData()) {
                    vueCarte.getZonesContenantPions().remove(j);
                }
            }
        }

        modeleNotes.notifierObservateurs();
    }
}
