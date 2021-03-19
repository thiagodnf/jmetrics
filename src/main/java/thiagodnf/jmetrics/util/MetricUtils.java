package thiagodnf.jmetrics.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.uma.jmetal.qualityindicator.impl.Epsilon;
import org.uma.jmetal.qualityindicator.impl.ErrorRatio;
import org.uma.jmetal.qualityindicator.impl.GenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistancePlus;
import org.uma.jmetal.qualityindicator.impl.Spread;
import org.uma.jmetal.qualityindicator.impl.hypervolume.impl.PISAHypervolume;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.impl.ArrayFront;
import org.uma.jmetal.util.front.util.FrontNormalizer;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.point.PointSolution;

import lombok.extern.slf4j.Slf4j;
import thiagodnf.jmetrics.constant.MetricType;
import thiagodnf.jmetrics.metric.HypervolumeApprox;
import thiagodnf.jmetrics.model.ParetoFront;

@Slf4j
public class MetricUtils {
    
    public static void calculate(ParetoFront approxParetoFront, ParetoFront paretoFront, List<MetricType> metrics) {

        log.debug("Calculating metrics for: \033[0;32m {} \u001b[0m", paretoFront.getPath().toFile().getName());
        
        Map<String, Double> values = new HashMap<>();
        
        Front referenceFront = new ArrayFront(approxParetoFront.getSolutions());
        
        FrontNormalizer frontNormalizer = new FrontNormalizer(referenceFront);

        Front normalizedReferenceFront = frontNormalizer.normalize(referenceFront);
        Front normalizedFront = frontNormalizer.normalize(new ArrayFront(paretoFront.getSolutions()));
        
        List<PointSolution> normalizedPopulation = FrontUtils.convertFrontToSolutionList(normalizedFront);

        if (metrics.contains(MetricType.HypervolumeApprox)) {
            values.put(MetricType.HypervolumeApprox + "(N)", new HypervolumeApprox<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
            values.put(MetricType.HypervolumeApprox.toString(), new HypervolumeApprox<Solution<?>>(referenceFront).evaluate(paretoFront.getSolutions()));
        }
        
        if (metrics.contains(MetricType.Hypervolume)) {
            values.put(MetricType.Hypervolume + "(N)", new PISAHypervolume<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
            values.put(MetricType.Hypervolume.toString(), new PISAHypervolume<Solution<?>>(referenceFront).evaluate(paretoFront.getSolutions()));
        }
        
        if (metrics.contains(MetricType.Epsilon)) {
            values.put(MetricType.Epsilon + "(N)", new Epsilon<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
            values.put(MetricType.Epsilon.toString(), new Epsilon<Solution<?>>(referenceFront).evaluate(paretoFront.getSolutions()));
        }
        
        if (metrics.contains(MetricType.GD)) {
            values.put(MetricType.GD + "(N)", new GenerationalDistance<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
            values.put(MetricType.GD.toString(), new GenerationalDistance<Solution<?>>(referenceFront).evaluate(paretoFront.getSolutions()));
        }
        
        if (metrics.contains(MetricType.IGD)) {
            values.put(MetricType.IGD + "(N)", new InvertedGenerationalDistance<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
            values.put(MetricType.IGD.toString(), new InvertedGenerationalDistance<Solution<?>>(referenceFront).evaluate(paretoFront.getSolutions()));
        }
        
        if (metrics.contains(MetricType.IGDPlus)) {
            values.put(MetricType.IGDPlus + "(N)", new InvertedGenerationalDistancePlus<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
            values.put(MetricType.IGDPlus.toString(), new InvertedGenerationalDistancePlus<Solution<?>>(referenceFront).evaluate(paretoFront.getSolutions()));
        }
        
        if (metrics.contains(MetricType.Spread)) {
            values.put(MetricType.Spread + "(N)", new Spread<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
            values.put(MetricType.Spread.toString(), new Spread<Solution<?>>(referenceFront).evaluate(paretoFront.getSolutions()));
        }
        
        if (metrics.contains(MetricType.ErrorRatio)) {
            values.put(MetricType.ErrorRatio.toString(), new ErrorRatio<List<? extends Solution<?>>>(referenceFront).evaluate(paretoFront.getSolutions()));
        }

        paretoFront.setMetrics(values);
    }
}
    