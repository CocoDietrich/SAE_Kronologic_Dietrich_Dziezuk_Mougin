package Kronologic.Elements;

import java.util.List;

public class Lieu {

        private final String name;
        private final int id;
        private final List<Lieu> listeLieuxAdjacents;

        public Lieu(String n, int i, List<Lieu> l) {
            this.name = n;
            this.id = i;
            this.listeLieuxAdjacents = l;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public List<Lieu> getListeLieuxAdjacents() {
            return listeLieuxAdjacents;
        }
}
