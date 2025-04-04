package Kronologic.MVC.Controleur;

import Kronologic.Jeu.Elements.*;
import Kronologic.Jeu.Partie;
import Kronologic.MVC.Modele.ModeleJeu;
import Kronologic.MVC.Modele.SousModeleJeu.ModeleNotes;
import Kronologic.MVC.TextCase;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.List;

public class ControleurChoixTableau implements EventHandler<MouseEvent> {

    private final ModeleNotes modeleNotes;
    // Ajout de constantes pour les états
    private static final String ETAT_PRESENT = "présent";
    private static final String ETAT_ABSENT = "absent";
    private static final String ETAT_NEUTRE = "neutre";

    public ControleurChoixTableau(ModeleNotes modeleNotes) {
        this.modeleNotes = modeleNotes;
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

    // Méthode pour récupérer un lieu par son nom
    private Lieu recupererLieuParNom(String nom, List<Lieu> lieux) {
        return lieux.stream()
                .filter(l -> l.getNom().equals(nom))
                .findFirst()
                .orElse(null);
    }

    // Méthode pour récupérer un personnage par son nom
    private Personnage recupererPersonnageParNom(String nom, List<Personnage> personnages) {
        return personnages.stream()
                .filter(p -> p.getNom().equals(nom))
                .findFirst()
                .orElse(null);
    }

    public void gestionNote(TextCase text, Elements elements, String etat) {
        List<Lieu> lieux = elements.lieux();
        List<Personnage> personnages = elements.personnages();

        Lieu lieu;
        Personnage personnage = null;
        int nbPersonnage = 0;
        Temps temps = new Temps(Integer.parseInt(text.getInfo().split(" - ")[1]));

        // Récupérer les éléments en fonction de l'état
        if (text.getInfo().split(" - ")[0].equals("Nombre")) {
            lieu = recupererLieuParNom(text.getInfo().split(" - ")[2], lieux);
            nbPersonnage = Integer.parseInt(text.getText());
        } else {
            personnage = recupererPersonnageParNom(text.getInfo().split(" - ")[2], personnages);
            lieu = recupererLieuParNom(nomLieu(text.getText()), lieux);
        }

        // Traitement des notes
        switch (etat) {
            case ETAT_PRESENT:
                if (personnage != null) {
                    this.modeleNotes.ajouterNote(lieu, temps, personnage);
                } else {
                    this.modeleNotes.ajouterNote(lieu, temps, nbPersonnage);
                }
                break;
            case ETAT_ABSENT:
                if (personnage != null) {
                    this.modeleNotes.modifierNote(lieu, temps, personnage, true, false);
                } else {
                    this.modeleNotes.modifierNote(lieu, temps, nbPersonnage, true, false);
                }
                break;
            case ETAT_NEUTRE:
                if (personnage != null) {
                    this.modeleNotes.supprimerNote(lieu, temps, personnage);
                } else {
                    this.modeleNotes.supprimerNote(lieu, temps, nbPersonnage);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        TextCase text = (TextCase) mouseEvent.getSource();
        Partie partie = modeleNotes.getPartie();
        Elements elements = partie.getElements();

        String nouvelEtat;

        switch (text.getEtat()) {
            case ETAT_NEUTRE -> {
                text.setFill(Color.BLACK);
                text.setStyle("-fx-font-weight: bold; -fx-strikethrough: false; -fx-cursor: hand;");
                nouvelEtat = ETAT_PRESENT;
            }
            case ETAT_PRESENT -> {
                text.setFill(Color.GRAY);
                text.setStyle("-fx-font-weight: normal; -fx-strikethrough: true; -fx-cursor: hand;");
                nouvelEtat = ETAT_ABSENT;
            }
            case ETAT_ABSENT -> {
                text.setFill(Color.LIGHTGRAY);
                text.setStyle("-fx-font-weight: normal; -fx-strikethrough: false; -fx-cursor: hand;");
                nouvelEtat = ETAT_NEUTRE;
            }
            default -> {
                return;
            }
        }

        text.setEtat(nouvelEtat);
        gestionNote(text, elements, nouvelEtat);
        modeleNotes.notifierObservateurs();
    }
}
