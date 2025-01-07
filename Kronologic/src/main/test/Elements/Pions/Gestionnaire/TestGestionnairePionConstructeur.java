package Elements.Pions.Gestionnaire;

import Kronologic.Jeu.Elements.GestionnairePions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGestionnairePionConstructeur {

    private GestionnairePions gestionnairePions;

    // On teste le constructeur de GestionnaireNotes
    @Test
    public void testGestionnairePions(){
        gestionnairePions = new GestionnairePions();
        assertEquals(0, gestionnairePions.getPions().size());
    }




}
