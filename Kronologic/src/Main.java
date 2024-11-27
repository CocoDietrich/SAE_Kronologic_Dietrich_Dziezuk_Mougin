import Data.JsonReader;
import Kronologic.Enquete;
import Kronologic.Partie;
import MVC.Accueil.ControleurAccueil;
import MVC.Accueil.ModeleAccueil;
import MVC.Accueil.VueAccueil;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main {
    public static void main(String[] args){
        Enquete enquete = JsonReader.lireEnqueteDepuisJson("data/enquete_base.json");

        Partie partie = new Partie(enquete);

        // Afficher accueil en JavaFX

        Stage stage = new Stage();

        ModeleAccueil modele = new ModeleAccueil();
        VueAccueil vueAccueil = new VueAccueil();
        new ControleurAccueil(modele, vueAccueil);
    }
}