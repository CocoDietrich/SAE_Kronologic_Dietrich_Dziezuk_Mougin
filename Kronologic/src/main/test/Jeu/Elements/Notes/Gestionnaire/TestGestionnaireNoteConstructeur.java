package Jeu.Elements.Notes.Gestionnaire;

import Kronologic.Jeu.Elements.GestionnaireNotes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGestionnaireNoteConstructeur {

    private GestionnaireNotes gestionnaireNotes;

    // On teste le constructeur de GestionnaireNotes
    @Test
    public void testGestionnaireNotes(){
        gestionnaireNotes = new GestionnaireNotes();
        assertEquals(0, gestionnaireNotes.getNotes().size());
    }




}
