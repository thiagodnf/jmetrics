package thiagodnf.jmetrics.metric;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;

import org.uma.jmetal.qualityindicator.impl.hypervolume.Hypervolume;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.comparator.HypervolumeContributionComparator;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.impl.ArrayFront;
import org.uma.jmetal.util.point.impl.ArrayPoint;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.solutionattribute.impl.HypervolumeContributionAttribute;

public class HypervolumeApprox<S extends Solution<?>> extends Hypervolume<S> {

    private static final long serialVersionUID = -868538120043712827L;
    
    private Point referencePoint;
    
    private int numberOfObjectives;

    private static final double DEFAULT_OFFSET = 1.1;
    
    private double offset = DEFAULT_OFFSET;
    
    private final int sampleSize = 1000000;

    /**
     * Default constructor
     */
    public HypervolumeApprox() {
    }

    public HypervolumeApprox(String referenceParetoFrontFile) throws FileNotFoundException {
        super(referenceParetoFrontFile);
        numberOfObjectives = referenceParetoFront.getPointDimensions();
        referencePoint = null;
        updateReferencePoint(referenceParetoFront);
    }

    public HypervolumeApprox(Front referenceParetoFront) {
        super(referenceParetoFront);
        numberOfObjectives = referenceParetoFront.getPointDimensions();
        referencePoint = null;
        updateReferencePoint(referenceParetoFront);
    }

    @Override
    public Double evaluate(List<S> solutionList) {
        double hv = 0.0;
        if (!solutionList.isEmpty()) {
            numberOfObjectives = solutionList.get(0).getNumberOfObjectives();
            Front front = new ArrayFront(solutionList);

            // normalize solutions
            for (int i = 0; i < front.getNumberOfPoints(); i++) {
                Point point = front.getPoint(i);
                for (int j = 0; j < point.getDimension(); j++) {
                    point.setValue(j, point.getValue(j) / (offset * referencePoint.getValue(j)));
                }
            }

            int countDominated = 0;
            double[] generated = new double[numberOfObjectives];
            double totalVolume = Math.pow(offset, numberOfObjectives);
            for (int i = 0; i < sampleSize; i++) {
                for (int j = 0; j < numberOfObjectives; j++) {
                    generated[j] = JMetalRandom.getInstance().nextDouble(0, offset);
                }
                for (int k = 0; k < front.getNumberOfPoints(); k++) {
                    Point point = front.getPoint(k);
                    boolean dominatedTmp = true;
                    for (int d = 0; d < numberOfObjectives; d++) {
                        if (point.getValue(d) > generated[d]) {
                            dominatedTmp = false;
                            break;
                        }
                    }
                    if (dominatedTmp) {
                        countDominated++;
                        break;
                    }
                }
            }
            hv = (double) countDominated / (double) sampleSize * totalVolume;
        }
        return hv;
    }

    /**
     * Updates the reference point
     */
    private void updateReferencePoint(List<? extends Solution<?>> solutionList) {
        double[] maxObjectives = new double[numberOfObjectives];
        for (int i = 0; i < numberOfObjectives; i++) {
            maxObjectives[i] = 0;
        }

        for (int i = 0; i < solutionList.size(); i++) {
            for (int j = 0; j < numberOfObjectives; j++) {
                if (maxObjectives[j] < solutionList.get(i).getObjective(j)) {
                    maxObjectives[j] = solutionList.get(i).getObjective(j);
                }
            }
        }

        if (referencePoint == null) {
            referencePoint = new ArrayPoint(numberOfObjectives);
            for (int i = 0; i < numberOfObjectives; i++) {
                referencePoint.setValue(i, Double.MAX_VALUE);
            }
        }

        for (int i = 0; i < referencePoint.getDimension(); i++) {
            referencePoint.setValue(i, maxObjectives[i] + offset);
        }
    }

    /**
     * Updates the reference point
     */
    private void updateReferencePoint(Front front) {
        double[] maxObjectives = new double[numberOfObjectives];
        for (int i = 0; i < numberOfObjectives; i++) {
            maxObjectives[i] = 0;
        }

        for (int i = 0; i < front.getNumberOfPoints(); i++) {
            for (int j = 0; j < numberOfObjectives; j++) {
                if (maxObjectives[j] < front.getPoint(i).getValue(j)) {
                    maxObjectives[j] = front.getPoint(i).getValue(j);
                }
            }
        }

        if (referencePoint == null) {
            referencePoint = new ArrayPoint(numberOfObjectives);
            for (int i = 0; i < numberOfObjectives; i++) {
                referencePoint.setValue(i, Double.MAX_VALUE);
            }
        }

        for (int i = 0; i < referencePoint.getDimension(); i++) {
            referencePoint.setValue(i, maxObjectives[i] + offset);
        }
    }

    @Override
    public List<S> computeHypervolumeContribution(List<S> solutionList, List<S> referenceFrontList) {
        numberOfObjectives = solutionList.get(0).getNumberOfObjectives();
        updateReferencePoint(referenceFrontList);
        if (solutionList.size() > 1) {
            double[] contributions = new double[solutionList.size()];
            double solutionSetHV = evaluate(solutionList);

            for (int i = 0; i < solutionList.size(); i++) {
                S currentPoint = solutionList.get(i);
                solutionList.remove(i);

                contributions[i] = solutionSetHV - evaluate(solutionList);

                solutionList.add(i, currentPoint);
            }

            HypervolumeContributionAttribute<Solution<?>> hvContribution = new HypervolumeContributionAttribute<>();
            for (int i = 0; i < solutionList.size(); i++) {
                hvContribution.setAttribute(solutionList.get(i), contributions[i]);
            }

            Collections.sort(solutionList, new HypervolumeContributionComparator<>());
        }

        return solutionList;
    }

    @Override
    public double getOffset() {
        return offset;
    }

    @Override
    public void setOffset(double offset) {
        this.offset = offset;
    }

    @Override
    public String getDescription() {
        return "Hypervolume Approximation quality indicator";
    }

    @Override
    public String getName() {
        return "HV";
    }
}
