package thiagodnf.jmetrics.metric;

import java.util.List;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.front.Front;

public class ProportionalContribution<S extends Solution<?>> extends Contribution<S> {

    private static final long serialVersionUID = -7972540957730648977L;

    public ProportionalContribution(Front referenceParetoFront) {
        super(referenceParetoFront);
    }

    @Override
    public Double evaluate(List<S> solutions) {

        if (solutions.isEmpty()) {
            return 0.0;
        }

        double value = super.evaluate(solutions);

        return value / (double) referenceParetoFront.getNumberOfPoints();
    }
}
