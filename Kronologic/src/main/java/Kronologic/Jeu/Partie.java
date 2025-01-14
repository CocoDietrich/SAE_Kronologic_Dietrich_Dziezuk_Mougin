package Kronologic.Jeu;

import Kronologic.Jeu.Elements.*;
import Kronologic.Jeu.Indice.GestionnaireIndices;
import Kronologic.Jeu.Indice.Indice;
import Kronologic.Jeu.Elements.GestionnairePions;
import Kronologic.Jeu.Elements.Pion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Partie {
    private int nbQuestion;
    private ArrayList<Indice> indicesDecouverts;
    private Enquete enquete;
    private Deroulement deroulement;
    private GestionnaireIndices gestionnaireIndices;
    private GestionnaireNotes gestionnaireNotes;
    private GestionnairePions gestionnairePions;
    private Elements elements;
    private Map<Integer, List<Note>> historique;

    public Partie(Enquete enquete, Deroulement deroulement, GestionnaireIndices gestionnaireIndices, GestionnaireNotes gestionnaireNotes, GestionnairePions gestionnairePions, Elements elements) {
        if (enquete == null || deroulement == null || gestionnaireIndices == null || gestionnaireNotes == null || gestionnairePions == null || elements == null) {
            throw new IllegalArgumentException("Les paramètres ne peuvent pas être nuls");
        }
        this.indicesDecouverts = new ArrayList<>();
        this.enquete = enquete;
        this.deroulement = deroulement;
        this.gestionnaireIndices = gestionnaireIndices;
        this.gestionnaireNotes = gestionnaireNotes;
        this.gestionnairePions = gestionnairePions;
        this.nbQuestion = 0;
        this.elements = elements;
        this.historique = new HashMap<>();
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
        if (i == null || indicesDecouverts.contains(i) || !gestionnaireIndices.getListeIndices().contains(i)) {
            return;
        }
        indicesDecouverts.add(i);
    }

    // Méthode permettant de faire une déduction
    public boolean faireDeduction(Lieu l, Personnage p, Temps t) {
        return enquete.faireDeduction(l, p, t);
    }

    // Méthode permettant d'ajouter une note du joueur (placer un pion)
    public void ajouterNote(Note n) {
        if (n == null || gestionnaireNotes.getNotes().contains(n)) {
            return;
        }
        gestionnaireNotes.ajouterNote(n);
        mettreAJourHistorique();
    }

    // Méthode permettant de modifier une note
    public void modifierNote(Note n, boolean absence, boolean hypothese) {
        gestionnaireNotes.modifierNote(n, absence, hypothese);
        mettreAJourHistorique();
    }

    // Méthode permettant de retirer une note du joueur (enlever un pion des cartes)
    public void supprimerNote(Note n) {
        gestionnaireNotes.supprimerNote(n);
        mettreAJourHistorique();
    }

    // Méthode permettant d'ajouter un pion
    public void ajouterPion(Pion pion) {
        gestionnairePions.ajouterPion(pion);
        gestionnaireNotes.ajouterNote(pion.getNote());
        mettreAJourHistorique();
    }

    // Méthode permettant de déplacer un pion
    public void deplacerPion(Pion pion, Lieu nouveauLieu, Temps nouveauTemps, int x, int y) {
        if (pion == null || nouveauLieu == null || nouveauTemps == null || !gestionnairePions.getPions().contains(pion)) {
            return;
        }
        gestionnaireNotes.deplacerNote(pion.getNote(), nouveauLieu, nouveauTemps);
        gestionnairePions.deplacerPion(pion, nouveauLieu, nouveauTemps, x, y);
        mettreAJourHistorique();
    }

    // Méthode permettant de supprimer un pion
    public void supprimerPion(Pion pion) {
        if (pion == null || !gestionnairePions.getPions().contains(pion)) {
            return;
        }
        gestionnairePions.supprimerPion(pion);
        if (pion.getNote() != null){
            System.out.println("Suppression de la note : " + pion.getNote().toString());
            gestionnaireNotes.supprimerNote(pion.getNote());
            mettreAJourHistorique();
        }
    }

    // Méthode permettant à l'IA de demander un indice
    public Indice demanderIndice() {
        // TODO : à implémenter
        return null;
    }

    public void mettreAJourHistorique(){
        // Copier la liste de notes pour éviter les références partagées
        List<Note> notesCopiees = new ArrayList<>(gestionnaireNotes.getNotes());

        // Mettre à jour l'historique avec une copie
        historique.put(nbQuestion, notesCopiees);
    }

    public String verifierLoupe(){
        return enquete.verifierLoupe(nbQuestion);
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

    public GestionnairePions getGestionnairePions() {
        return gestionnairePions;
    }

    public Elements getElements() {
        return elements;
    }

    public Map<Integer, List<Note>> getHistorique() {
        return historique;
    }
}
