package thiagodnf.jmetrics.metric;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.front.Front;

import thiagodnf.jmetrics.model.ParetoFront;

public class NumberOfSolutions<S extends Solution<?>> extends Contribution<S> {

    private static final long serialVersionUID = 6958192670098790153L;

    public NumberOfSolutions(Front referenceParetoFront) {
        super(referenceParetoFront);
    }

    public Double evaluate(ParetoFront pf) {
        return Double.valueOf(pf.getSolutions().size());
    }
}
