package Kronologic.MVC.Controleur;

import Kronologic.Jeu.Elements.*;
import Kronologic.Jeu.Partie;
import Kronologic.MVC.Modele.ModeleJeu;
import Kronologic.MVC.Vue.TextCase;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import javax.sound.sampled.LineUnavailableException;
import java.io.FilterOutputStream;
import java.util.List;

public class ControleurChoixTableau implements EventHandler<MouseEvent> {

    private ModeleJeu modeleJeu;

    public ControleurChoixTableau(ModeleJeu modeleJeu) {
        this.modeleJeu = modeleJeu;
    }

    public static String nomLieu(String nomCourt){
        return switch (nomCourt) {
            case "GF" -> "Grand foyer";
            case "GE" -> "Grand escalier";
            case "SA" -> "Salle";
            case "SC" -> "Scène";
            case "FD" -> "Foyer de la danse";
            case "FC" -> "Foyer du chant";
            default -> "";
        };
    }

    public void gestionNote(TextCase text, Elements elements, String etat) {
        // On récupère les lieux et les personnages
        List<Lieu> lieux = elements.getLieux();
        List<Personnage> personnages = elements.getPersonnages();

        Lieu lieu = null;
        Personnage personnage = null;
        int nbPersonnage = 0;
        Temps temps = null;

        if (text.getInfo().split(" - ")[0].equals("Nombre")) {
            // Cas du nombre de Personnages

            // On récupère le Lieu
            for (Lieu l : lieux) {
                if (l.getNom().equals(text.getInfo().split(" - ")[2])) {
                    lieu = l;
                }
            }

            // On récupère le nombre de Personnages
            nbPersonnage = Integer.parseInt(text.getText());
        } else {
            // Cas de la position du personnage

            // On récupère le Personnage
            for (Personnage p : personnages) {
                if (p.getNom().equals(text.getInfo().split(" - ")[2])) {
                    personnage = p;
                }
            }

            // On récupère le Lieu
            for (Lieu l : lieux) {
                if (l.getNom().equals(nomLieu(text.getText()))) {
                    lieu = l;
                }
            }
        }

        // On récupère le Temps
        temps = new Temps(Integer.parseInt(text.getInfo().split(" - ")[1]));

        switch (etat) {
            case "présent":
                if (personnage != null) {
                    this.modeleJeu.ajouterNote(lieu, temps,  personnage);
                } else {
                    this.modeleJeu.ajouterNote(lieu, temps, nbPersonnage);
                }
                break;
            case "absent":
                if (personnage != null) {
                    System.out.println("modif");
                    this.modeleJeu.modifierNote(lieu, temps, personnage, true, false);
                } else {
                    System.out.println("modif");
                    this.modeleJeu.modifierNote(lieu, temps, nbPersonnage, true, false);
                }
                break;
                case "neutre":
                    if (personnage != null) {
                        System.out.println("suppr");
                        this.modeleJeu.supprimerNote(lieu, temps, personnage);
                    } else {
                        System.out.println("suppr");
                        this.modeleJeu.supprimerNote(lieu, temps, nbPersonnage);
                    }
                break;
            default:
                break;
        }
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        TextCase text = (TextCase) mouseEvent.getSource();

        // Récupérer l'état actuel
        String etat = text.getEtat();

        // On récupère les éléments du jeu
        Partie partie = ModeleJeu.getPartie();
        Elements elements = partie.getElements();

        // Basculer entre les états : neutre -> sélectionné -> absence -> neutre
        switch (etat) {
            case "neutre":
                System.out.println("neutre");
                // État sélectionné : texte noir et gras
                text.setFill(Color.BLACK);
                text.setStyle("-fx-font-weight: bold; " +
                        "-fx-strikethrough: false;" +
                        "-fx-cursor: hand;");
                text.setEtat("présent");
                gestionNote(text, elements, text.getEtat());
                break;
            case "présent":
                System.out.println("présent");
                // État absence : texte gris et barré
                text.setFill(Color.GRAY);
                text.setStyle("-fx-font-weight: normal; " +
                        "-fx-strikethrough: true;" +
                        "-fx-cursor: hand;");
                text.setEtat("absent");
                gestionNote(text, elements, text.getEtat());
                break;

            case "absent":
                System.out.println("absent");
                // Retour à l'état neutre
                text.setFill(Color.LIGHTGRAY);
                text.setStyle("-fx-font-weight: normal; " +
                        "-fx-strikethrough: false;" +
                        "-fx-cursor: hand;");
                text.setEtat("neutre");
                gestionNote(text, elements, text.getEtat());
                break;
        }
    }
}
