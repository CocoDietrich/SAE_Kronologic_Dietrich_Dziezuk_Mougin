package Kronologic;

import Kronologic.Elements.Enquete;
import Kronologic.Elements.Lieu;
import Kronologic.Elements.Personnage;
import Kronologic.Elements.Temps;
import Kronologic.Indice.Indice;
import Kronologic.Pions.Pion;

import java.util.ArrayList;
import java.util.List;

public class Partie {

    private final Enquete enquete;
    private List<Indice> indicesTrouves;
    private List<Pion> listePions;
    private int nbQuestion;

    public Partie(Enquete e) {
        this.enquete = e;
    }

    public void lancerEnquete(){
        this.listePions = new ArrayList<>();
        this.indicesTrouves = new ArrayList<>();
        this.nbQuestion = 0;
    }

    public Indice poserQuestion(Lieu l, Personnage p, Temps t) {
        if (p == null){
            // poser question sur indice temps
        }
        else {
            // poser question sur indice personnage
        }
        nbQuestion++;
        return null;
    }

    public boolean faireDeduction(Lieu l, Personnage p, Temps t) {
        // TODO : A implémenter
        return false;
    }

    public Indice demanderIndice(){
        // TODO : A implémenter
        return null;
    }

    public void quitterPartie(){
        // TODO : A implémenter
    }

    public Enquete getEnquete() {
        return enquete;
    }

    public List<Indice> getIndicesTrouves() {
        return indicesTrouves;
    }

    public List<Pion> getListePions() {
        return listePions;
    }

    public int getNbQuestion() {
        return nbQuestion;
    }

    public void setIndicesTrouves(List<Indice> indicesTrouves) {
        this.indicesTrouves = indicesTrouves;
    }

    public void setListePions(List<Pion> listePions) {
        this.listePions = listePions;
    }

    public void setNbQuestion(int nbQuestion) {
        this.nbQuestion = nbQuestion;
    }
}
