package Kronologic.Jeu.Indice;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Personnage;

public class IndicePersonnage extends Indice {

    private final Personnage personnage;
    private final int infoPrive;

    public IndicePersonnage(Lieu l, int p, Personnage perso, int ip) {
        super(l, p);
        if (perso == null) {
            throw new IllegalArgumentException("Le personnage ne peut pas être nul.");
        }
        if (ip < 0) {
            throw new IllegalArgumentException("L'information privée ne peut pas être négatif.");
        }
        this.personnage = perso;
        this.infoPrive = ip;
    }

    @Override
    public String toString() {
        if (infoPrive == 0) {
            return "Le personnage " + personnage.getNom() + " est passé " + getInfoPublic() + " fois dans le lieu " + getLieu().getNom() + ".";
        }
        else {
            return "Le personnage " + personnage.getNom() + " est passé " + getInfoPublic() + " fois dans le lieu " + getLieu().getNom() + "."
                    + "\n De plus, il était présent dans ce le lieu au pas de temps " + infoPrive + ".";
        }
    }

    // Getters

    public Personnage getPersonnage() {
        return personnage;
    }

    public int getInfoPrive() {
        return infoPrive;
    }
}
