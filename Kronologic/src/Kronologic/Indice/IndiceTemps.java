package Kronologic.Indice;

import Kronologic.Elements.Lieu;
import Kronologic.Elements.Temps;

public class IndiceTemps extends Indice{

    private final Temps temps;
    private final String infoPrive;

    public IndiceTemps(Lieu l, int p, Temps t, String ip) {
        super(l, p);
        this.temps = t;
        this.infoPrive = ip;
    }

    @Override
    public String toString() {
        if (infoPrive.equals("Rejouer")) {
            return "Il y a " + getInfoPublic() + " personnes dans le lieu " + getLieu().getNom() + " au pas de temps " + temps.getValeur() + ".";
        }
        else {
            return "Il y a " + getInfoPublic() + " personnes dans le lieu " + getLieu().getNom() + " au pas de temps " + temps.getValeur() + "."
                    + "\n De plus, le " + infoPrive + " est dans ce lieu à ce pas de temps.";
        }
    }

    // Getters

    public Temps getTemps() {
        return temps;
    }

    public String getInfoPrive() {
        return infoPrive;
    }
}
