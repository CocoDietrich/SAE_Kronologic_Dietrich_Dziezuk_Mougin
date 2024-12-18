package Kronologic.Jeu;

import Kronologic.Jeu.Elements.*;
import Kronologic.Jeu.Indice.GestionnaireIndices;
import Kronologic.Jeu.Indice.Indice;

import java.util.ArrayList;
import java.util.Optional;

public class Partie {
    private int nbQuestion;
    private ArrayList<Indice> indicesDecouverts;
    private Enquete enquete;
    private Deroulement deroulement;
    private GestionnaireIndices gestionnaireIndices;
    private GestionnaireNotes gestionnaireNotes;

    public Partie(Enquete enquete, Deroulement deroulement, GestionnaireIndices gestionnaireIndices, GestionnaireNotes gestionnaireNotes) {
        this.indicesDecouverts = new ArrayList<>();
        this.enquete = enquete;
        this.deroulement = deroulement;
        this.gestionnaireIndices = gestionnaireIndices;
        this.gestionnaireNotes = gestionnaireNotes;
        this.nbQuestion = 0;
    }

    // Méthode permettant de poser une question sur un lieu et un personnage
    public Indice poserQuestionPersonnage(Lieu l, Personnage p) {
        nbQuestion++;
        Indice i = gestionnaireIndices.poserQuestionPersonnage(l, p);
        ajouterIndice(i);
        return i;
    }

    // Méthode permettant de poser une question sur un lieu et un temps
    public Indice poserQuestionTemps(Lieu l, Temps t) {
        nbQuestion++;
        Indice i = gestionnaireIndices.poserQuestionTemps(l, t);
        ajouterIndice(i);
        return i;
    }

    // Méthode permettant d'ajouter un indice à la liste des indices découverts
    public void ajouterIndice(Indice i) {
        indicesDecouverts.add(i);
    }

    // Méthode permettant de faire une déduction
    public boolean faireDeduction(Lieu l, Personnage p, Temps t) {
        return enquete.faireDeduction(l, p, t);
    }

    // Méthode permettant d'ajouter une note du joueur (placer un pion)
    public void ajouterNote(Note n) {
        gestionnaireNotes.ajouterNote(n);
    }

    // Méthode permettant de retirer une note du joueur (enlever un pion des cartes)
    public void supprimerNote(Note n) {
        gestionnaireNotes.supprimerNote(n);
    }

    // Méthode permettant à l'IA de demander un indice
    public Indice demanderIndice() {
        // TODO : à implémenter
        return null;
    }

    // Getter pour chaque attribut
    public int getNbQuestion() {
        return nbQuestion;
    }

    public ArrayList<Indice> getIndicesDecouverts() {
        return indicesDecouverts;
    }

    public Enquete getEnquete() {
        return enquete;
    }

    public Deroulement getDeroulement() {
        return deroulement;
    }

    public GestionnaireIndices getGestionnaireIndices() {
        return gestionnaireIndices;
    }

    public GestionnaireNotes getGestionnaireNotes() {
        return gestionnaireNotes;
    }
}
