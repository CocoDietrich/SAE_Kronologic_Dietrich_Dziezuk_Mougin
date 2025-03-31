package Kronologic.Jeu.Elements;

import Kronologic.Jeu.Images;

import java.util.List;

public class Lieu {

        private final String nom;
        private final int id;
        private final List<Lieu> listeLieuxAdjacents;
        private final static int NOMBRE_LIEU = 6;

        public Lieu(String n, int i, List<Lieu> l) {
            if (i == 0) {
                throw new IllegalArgumentException("L'id du lieu ne peut pas être null");
            } else if (i < 0) {
                throw new NullPointerException("Le nom du lieu ne peut pas être null");
            }
            else if (i > NOMBRE_LIEU) {
                throw new IllegalArgumentException("L'id du lieu ne peut pas être supérieur à " + NOMBRE_LIEU);
            }

            if (n == null) {
                throw new NullPointerException("Le nom du lieu ne peut pas être vide");
            } else if (n.isEmpty()) {
                throw new NullPointerException("Le nom du lieu ne peut pas être vide");
            } else if (!Images.Lieux.getLieux().containsValue(n)) {
                throw new IllegalArgumentException("Le lieu ne possède pas le bon nom");
            } else if (!Images.Lieux.getLieux().get(i).equals(n)) {
                throw new IllegalArgumentException("Le lieu ne possède pas la bonne combinaison nom et id");
            }

            this.nom = n;
            this.id = i;
            this.listeLieuxAdjacents = l;
        }

        public Lieu(int i) {
            if (i == 0) {
                throw new IllegalArgumentException("L'id du lieu ne peut pas être null");
            } else if (i < 0) {
                throw new NullPointerException("Le nom du lieu ne peut pas être null");
            }
            else if (i > NOMBRE_LIEU) {
                throw new IllegalArgumentException("L'id du lieu ne peut pas être supérieur à " + NOMBRE_LIEU);
            }
            this.nom = null;
            this.id = i;
            this.listeLieuxAdjacents = null;
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
