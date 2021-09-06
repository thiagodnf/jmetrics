package thiagodnf.jmetrics.metric;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.front.Front;

import thiagodnf.jmetrics.model.ParetoFront;

public class ExecutionTime<S extends Solution<?>> extends Contribution<S> {

    private static final long serialVersionUID = -7972540957730648977L;

    public ExecutionTime(Front referenceParetoFront) {
        super(referenceParetoFront);
    }

    public Double evaluate(ParetoFront pf) {
        return Double.valueOf(pf.getExecutionTime());
    }
}
