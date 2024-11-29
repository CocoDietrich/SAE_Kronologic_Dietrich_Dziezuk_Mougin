package Kronologic.Jeu;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;
import Kronologic.Jeu.Elements.Temps;
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

    public Partie(Enquete enquete, Deroulement deroulement, GestionnaireIndices gestionnaireIndices) {
        this.indicesDecouverts = new ArrayList<>();
        this.enquete = enquete;
        this.deroulement = deroulement;
        this.gestionnaireIndices = gestionnaireIndices;
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
}
