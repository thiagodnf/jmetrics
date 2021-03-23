package thiagodnf.jmetrics.metric;

import java.util.List;

import org.uma.jmetal.qualityindicator.impl.GenericIndicator;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.impl.ArrayFront;
import org.uma.jmetal.util.point.Point;

import thiagodnf.jmetrics.util.ParetoFrontUtils;

public class Contribution<S extends Solution<?>> extends GenericIndicator<S> {

    private static final long serialVersionUID = 7750277341449581309L;

    public Contribution(Front referenceParetoFront) {
        super(referenceParetoFront);
    }

    @Override
    public Double evaluate(List<S> solutions) {

        if (solutions.isEmpty()) {
            return 0.0;
        }

        double value = 0.0;

        Front front = new ArrayFront(solutions);

        // normalize solutions
        for (int i = 0; i < front.getNumberOfPoints(); i++) {

            Point point = front.getPoint(i);

            if (ParetoFrontUtils.contains(referenceParetoFront, point)) {
                value++;
            }
        }

        return value;
    }
    

    @Override
    public boolean isTheLowerTheIndicatorValueTheBetter() {
        return false;
    }
}
