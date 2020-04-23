package thiagodnf.jmetrics.util;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.SolutionListUtils;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;

import lombok.extern.slf4j.Slf4j;
import thiagodnf.jmetrics.constant.Separator;
import thiagodnf.jmetrics.model.ParetoFront;

@Slf4j
public class ParetoFrontUtils {
    
    public static Optional<ParetoFront> generateApproxParetoFront(Path folder, Separator separator, List<ParetoFront> paretoFronts) throws IOException {
        
        log.info("Generating approx pareto-front");
        
        List<Solution<?>> allParetoFronts = new ArrayList<>();

        for (ParetoFront paretoFront : paretoFronts) {
            allParetoFronts.addAll(paretoFront.getSolutions());
        }

        allParetoFronts = ParetoFrontUtils.removeRepeatedSolutions(allParetoFronts);
        allParetoFronts = ParetoFrontUtils.removeDominatedSolutions(allParetoFronts);
        
        Path file = folder.resolve("pareto-front.txt");
        
        ParetoFront approxParetoFront = new ParetoFront(file, allParetoFronts);
        
        ExportUtils.toFile(file, approxParetoFront, separator);
        
        log.info("Done");
        
        return Optional.of(approxParetoFront);
    }

    public static List<Solution<?>> removeDominatedSolutions(List<Solution<?>> population) {
        
        checkNotNull(population, "Population cannot be null");
        
        if (population.size() == 0) {
            return population;
        }

        return SolutionListUtils.getNondominatedSolutions(population);
    }
    
    public static List<Solution<?>> removeRepeatedSolutions(List<Solution<?>> population){
        
        checkNotNull(population, "Population cannot be null");
        
        List<Solution<?>> newPopulation = new ArrayList<>();
        
        for (int i = 0; i < population.size(); i++) {
            
            Solution<?> s = population.get(i);

            if (!contains(newPopulation, s)) {
                newPopulation.add(s);
            }
        }
        
        return newPopulation;
    }
    
    public static boolean contains(List<Solution<?>> population, Solution<?> s1) {
        
        checkNotNull(population, "Population cannot be null");
        checkNotNull(s1, "Soluton s1 cannot be null");

        for (int i = 0; i < population.size(); i++) {
            
            Solution<?> s2 = population.get(i);

            if (isEqual(s1, s2)) {
                return true;
            }
        }
        
        return false;
    }
    
    public static boolean isEqual(Solution<?> s1, Solution<?> s2) {
        
        checkNotNull(s1, "Soluton s1 cannot be null");
        checkNotNull(s2, "Soluton s2 cannot be null");
        checkArgument(s1.getNumberOfObjectives() == s2.getNumberOfObjectives(), "Solutions cannot be different number of objetives");
        
        for (int i = 0; i < s1.getNumberOfObjectives(); i++) {
            
            if (s1.getObjective(i) != s2.getObjective(i)) {
                return false;
            }
        }

        return true;
    } 
}
