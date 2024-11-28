package Kronologic.Jeu.Elements;

import java.util.List;

public class Lieu {

        private final String nom;
        private final int id;
        private final List<Lieu> listeLieuxAdjacents;

        public Lieu(String n, int i, List<Lieu> l) {
            this.nom = n;
            this.id = i;
            this.listeLieuxAdjacents = l;
        }

        public String getNom() {
            return nom;
        }

        public int getId() {
            return id;
        }

        public List<Lieu> getListeLieuxAdjacents() {
            return listeLieuxAdjacents;
        }
}
