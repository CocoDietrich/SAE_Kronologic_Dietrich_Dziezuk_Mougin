package Kronologic.IA.IADeduction;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.constraints.PropagatorPriority;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.events.IntEventType;
import org.chocosolver.solver.variables.events.PropagatorEventType;
import org.chocosolver.util.ESat;

public class PropagateurDeplacement extends Propagator<IntVar> {

    private final IntVar salleActuelle;
    private final IntVar salleSuivante;
    private final int[][] sallesAdjacentes;

    public PropagateurDeplacement(IntVar salleActuelle, IntVar salleSuivante, int[][] sallesAdjacentes) {
        super(new IntVar[]{salleActuelle, salleSuivante}, PropagatorPriority.BINARY, true);
        this.salleActuelle = salleActuelle;
        this.salleSuivante = salleSuivante;
        this.sallesAdjacentes = sallesAdjacentes;
    }

    @Override
    public void propagate(int evtmask) throws ContradictionException {
        System.out.printf("Début de la propagation entre %s et %s%n", salleActuelle.getName(), salleSuivante.getName());

        boolean modification = false;

        if (salleActuelle.isInstantiated()) {
            int salle = salleActuelle.getValue();
            System.out.printf("%s est instanciée à %d%n", salleActuelle.getName(), salle);

            for (int valeur = salleSuivante.getLB(); valeur <= salleSuivante.getUB(); valeur = salleSuivante.nextValue(valeur)) {
                if (!estAdjacente(valeur, sallesAdjacentes[salle - 1])) {
                    salleSuivante.removeValue(valeur, this);
                    System.out.printf("Suppression de %d de %s (pas adjacente à %d)%n", valeur, salleSuivante.getName(), salle);
                    modification = true;
                }
            }
        }

        // Passer en mode passif si toutes les variables sont instanciées
        if (salleActuelle.isInstantiated() && salleSuivante.isInstantiated()) {
            System.out.println("Les deux variables sont instanciées, mise en mode passif.");
            setPassive();
        } else if (modification) {
            System.out.println("Repropagation forcée après modification.");
            this.forcePropagate(PropagatorEventType.FULL_PROPAGATION);
        }
    }


    @Override
    public void propagate(int varIdx, int mask) throws ContradictionException {
        IntVar var = vars[varIdx];
        System.out.printf("Propagation incrémentale déclenchée pour %s%n", var.getName());

        // Si le domaine est réduit à une seule valeur
        if (var.getDomainSize() == 1 && !var.isInstantiated()) {
            var.instantiateTo(var.getLB(), this);
            System.out.printf("%s instanciée à %d après réduction%n", var.getName(), var.getLB());

            // Forcer la propagation après chaque instanciation
            this.forcePropagate(PropagatorEventType.FULL_PROPAGATION);
        } else {
            System.out.printf("Aucune propagation effectuée pour %s (non instanciée ou domaine multiple)%n", var.getName());
        }
    }




    @Override
    public int getPropagationConditions(int vIdx) {
        return IntEventType.BOUND.getMask() | IntEventType.INSTANTIATE.getMask();
    }


    @Override
    public ESat isEntailed() {
        if (salleActuelle.isInstantiated() && salleSuivante.isInstantiated()) {
            int salle = salleActuelle.getValue();
            if (estAdjacente(salleSuivante.getValue(), sallesAdjacentes[salle - 1])) {
                return ESat.TRUE;
            }
            return ESat.FALSE;
        }
        return ESat.UNDEFINED;
    }

    private boolean estAdjacente(int valeur, int[] adjacentes) {
        for (int adj : adjacentes) {
            if (valeur == adj) {
                return true;
            }
        }
        return false;
    }
}