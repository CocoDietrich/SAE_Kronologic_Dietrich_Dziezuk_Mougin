package Kronologic.Pions;

public class PionNombre extends Pion {

    private int nombre;

    public PionNombre(int i, boolean h) {
        super(i, h);
        this.nombre = 0;
        // OU
        this.nombre = 0;
    }

    public void modifierNombre(int n) {
        this.nombre = n;
    }
}
