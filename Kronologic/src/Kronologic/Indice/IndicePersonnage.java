package Kronologic.Indice;

import Kronologic.Elements.Lieu;

public class IndicePersonnage extends Indice {

    private final String nomPersonnage;
    private final int infoPrive;

    public IndicePersonnage(Lieu l, int p, String n, int ip) {
        super(l, p);
        this.nomPersonnage = n;
        this.infoPrive = ip;
    }

    @Override
    public String toString() {
        if (infoPrive == 0) {
            return "Le personnage " + nomPersonnage + " est passé " + getInfoPublic() + " fois dans le lieu " + getLieu().getName() + ".";
        }
        else {
            return "Le personnage " + nomPersonnage + " est passé " + getInfoPublic() + " fois dans le lieu " + getLieu().getName() + "."
                    + "\n De plus, " + nomPersonnage + " était présent dans ce le lieu au pas de temps " + infoPrive + ".";
        }
    }

    // Getters

    public String getNomPersonnage() {
        return nomPersonnage;
    }

    public int getInfoPrive() {
        return infoPrive;
    }
}
