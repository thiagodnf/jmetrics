package thiagodnf.jmetrics.util;

import java.util.List;
import java.util.Properties;

import org.uma.jmetal.qualityindicator.impl.Epsilon;
import org.uma.jmetal.qualityindicator.impl.ErrorRatio;
import org.uma.jmetal.qualityindicator.impl.GenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistancePlus;
import org.uma.jmetal.qualityindicator.impl.Spread;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontNormalizer;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.point.PointSolution;

import thiagodnf.jmetrics.indicator.HypervolumeApprox;

public class MetricUtils {

    public static Properties calculate(List<Solution<?>> paretoFront, List<Solution<?>> population) {

        Properties metrics = new Properties();
        
        Front referenceFront = new ArrayFront(paretoFront);
        
        FrontNormalizer frontNormalizer = new FrontNormalizer(referenceFront);

        Front normalizedReferenceFront = frontNormalizer.normalize(referenceFront);
        Front normalizedFront = frontNormalizer.normalize(new ArrayFront(population));
        List<PointSolution> normalizedPopulation = FrontUtils.convertFrontToSolutionList(normalizedFront);

        metrics.put("Hypervolume Approx (N)", new HypervolumeApprox<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
        metrics.put("Hypervolume Approx", new HypervolumeApprox<Solution<?>>(referenceFront).evaluate(population));
        
//        metrics.put("Hypervolume (N)", new PISAHypervolume<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
//        metrics.put("Hypervolume", new PISAHypervolume<Solution<?>>(referenceFront).evaluate(population));
        
        metrics.put("Epsilon (N)", new Epsilon<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
        metrics.put("Epsilon", new Epsilon<Solution<?>>(referenceFront).evaluate(population));
        metrics.put("GD (N)", new GenerationalDistance<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
        metrics.put("GD", new GenerationalDistance<Solution<?>>(referenceFront).evaluate(population));
        metrics.put("IGD (N)", new InvertedGenerationalDistance<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
        metrics.put("IGD", new InvertedGenerationalDistance<Solution<?>>(referenceFront).evaluate(population));
        metrics.put("IGD+ (N)", new InvertedGenerationalDistancePlus<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
        metrics.put("IGD+", new InvertedGenerationalDistancePlus<Solution<?>>(referenceFront).evaluate(population));
        metrics.put("Spread (N)", new Spread<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation));
        metrics.put("Spread", new Spread<Solution<?>>(referenceFront).evaluate(population));
        metrics.put("Error ratio", new ErrorRatio<List<? extends Solution<?>>>(referenceFront).evaluate(population));

        return metrics;
    }
}
