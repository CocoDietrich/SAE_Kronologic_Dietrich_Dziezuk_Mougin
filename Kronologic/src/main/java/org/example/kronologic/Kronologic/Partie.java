package org.example.kronologic.Kronologic;

import org.example.kronologic.Kronologic.Elements.*;
import org.example.kronologic.Kronologic.Indice.*;
import org.example.kronologic.Kronologic.Pions.Pion;

import java.util.ArrayList;
import java.util.List;

public class Partie {

    private final Enquete enquete;
    private List<Indice> indicesTrouves;
    private List<Pion> listePions;
    private int nbQuestion;

    public Partie(Enquete e) {
        this.enquete = e;
        this.listePions = new ArrayList<>();
        this.indicesTrouves = new ArrayList<>();
        this.nbQuestion = 0;
    }

    public Indice poserQuestion(Lieu l, Personnage p, Temps t) {
        nbQuestion++;
        if (p == null){
            // Trouver l'indice relatif au temps
            for (Indice i : enquete.getIndices()){
                if (i instanceof IndiceTemps){
                    if (i.getLieu().getId() == l.getId() && ((IndiceTemps) i).getTemps().getValeur() == t.getValeur()){
                        indicesTrouves.add(i);
                        return i;
                    }
                }
            }
        }
        else {
            // Trouver l'indice relatif au personnage
            for (Indice i : enquete.getIndices()){
                if (i instanceof IndicePersonnage){
                    if (i.getLieu().getId() == l.getId() && ((IndicePersonnage) i).getNomPersonnage().equals(p.getNom())){
                        indicesTrouves.add(i);
                        return i;
                    }
                }
            }
        }
        return null;
    }

    public boolean faireDeduction(Lieu l, Personnage p, Temps t) {
        if (enquete.getMeurtrier() == p && enquete.getLieuDuCrime() == l && enquete.getTempsDuCrime() == t){
            return true;
        }
        else {
            return false;
        }
    }

    public String historiqueQuestions(){
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= indicesTrouves.size(); i++){
            sb.insert(0, indicesTrouves.get(i-1).toString()).insert(0, " : ").insert(0, i).insert(0, "Tour ");
        }
        return sb.toString();
    }

    public Indice demanderIndice(){
        // TODO : A implémenter
        return null;
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