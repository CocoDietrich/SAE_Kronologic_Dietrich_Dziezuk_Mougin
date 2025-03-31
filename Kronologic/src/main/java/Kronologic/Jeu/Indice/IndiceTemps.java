package Kronologic.Jeu.Indice;

import Kronologic.Jeu.Elements.Lieu;
import Kronologic.Jeu.Elements.Temps;

public class IndiceTemps extends Indice{

    private final Temps temps;
    private final String infoPrive;

    public IndiceTemps(Lieu l, int p, Temps t, String ip) {
        super(l, p);
        if (t == null) {
            throw new IllegalArgumentException("Le temps ne peut pas être nul.");
        }
        if (ip == null || ip.isEmpty()) {
            throw new IllegalArgumentException("L'information privée ne peut pas être vide.");
        }
        this.temps = t;
        this.infoPrive = ip;
    }

    @Override
    public String toString() {
        if (infoPrive.equals("Rejouer")) {
            return "Il y a " + getInfoPublic() + " personnes dans le lieu "
                    + getLieu().getNom() + " au pas de temps " + temps.getValeur() + ".";
        }
        else {
            return "Il y a " + getInfoPublic() + " personne(s) dans le lieu "
                    + getLieu().getNom() + " au pas de temps " + temps.getValeur() + "."
                    + "\n De plus, le personnage " + infoPrive + " est dans ce lieu à ce pas de temps.";
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
