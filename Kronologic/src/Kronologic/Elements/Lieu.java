package Kronologic.Elements;

import java.util.List;

public class Lieu {

        private String name;
        private int id;
        private List<Lieu> listeLieuxAdjacents;

        public Lieu(String n, int i, List<Lieu> l) {
            this.name = n;
            this.id = i;
            this.listeLieuxAdjacents = l;
        }
}
