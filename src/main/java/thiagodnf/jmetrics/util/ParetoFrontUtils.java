package thiagodnf.jmetrics.util;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.point.Point;

import lombok.extern.slf4j.Slf4j;
import thiagodnf.jmetrics.constant.Separator;
import thiagodnf.jmetrics.model.ParetoFront;

@Slf4j
public class ParetoFrontUtils {
    
    public static ParetoFront readOrGenerateParetoFront(Path folder, Separator separator, List<ParetoFront> paretoFronts ) {
        
        checkArgument(FileUtils.isValid(folder), "folder should be valid");
        checkNotNull(separator, "separator should be null");
        checkNotNull(paretoFronts, "paretoFronts should be null");
        
        ParetoFront approxParetoFront = null;
        
        Path pfFile = folder.resolve("pareto-front.txt");

        if (Files.exists(pfFile)) {
            approxParetoFront = readApproxParetoFront(pfFile, separator);
        } else {
            approxParetoFront = generateApproxParetoFront(pfFile, separator, paretoFronts);
        }
        
        generatePFKnownParetoFront(pfFile, separator, paretoFronts);
        
        log.info("[1/1] {}", pfFile);
        
        return approxParetoFront;
    }
    
    private static ParetoFront readApproxParetoFront(Path file, Separator separator) {

        checkArgument(FileUtils.isValid(file), "file should be valid");
        checkNotNull(separator, "separator should be null");

        log.info("Reading approx pareto-front from the directory");

        List<Solution<?>> solutions = ImportUtils.getFromFile(file, separator);

        solutions = ParetoFrontUtils.removeRepeatedSolutions(solutions);
        solutions = ParetoFrontUtils.removeDominatedSolutions(solutions);

        return new ParetoFront(file, solutions);
    }
    
    public static ParetoFront generateApproxParetoFront(Path file, Separator separator, List<ParetoFront> paretoFronts) {
        
        checkNotNull(separator, "separator should be null");
        checkNotNull(paretoFronts, "paretoFronts should be null");
        
        log.info("Generating approx pareto-front");
        
        List<Solution<?>> allSolutions = new ArrayList<>();

        for (ParetoFront paretoFront : paretoFronts) {
            allSolutions.addAll(paretoFront.getSolutions());
        }

        allSolutions = ParetoFrontUtils.removeRepeatedSolutions(allSolutions);
        allSolutions = ParetoFrontUtils.removeDominatedSolutions(allSolutions);
        
        ParetoFront approxParetoFront = new ParetoFront(file, allSolutions);
        
        ExportUtils.toFile(file, approxParetoFront, separator);
        
        return approxParetoFront;
    }
    
    public static void generatePFKnownParetoFront(Path file, Separator separator, List<ParetoFront> paretoFronts) {
        
        checkNotNull(separator, "separator should be null");
        checkNotNull(paretoFronts, "paretoFronts should be null");
        
        log.info("Generating PFKnown pareto-front");
        
        Set<String> basenames = new HashSet<>();
        
        for (ParetoFront pf : paretoFronts) {
            basenames.add(pf.getPath().getParent().toString());
        }
        
        for (String basename : basenames) {

            List<ParetoFront> filtered = new ArrayList<>();

            for (ParetoFront pf : paretoFronts) {

                if (pf.getPath().toString().startsWith(basename)) {
                    filtered.add(pf);
                }
            }

            List<Solution<?>> allSolutions = new ArrayList<>();

            for (ParetoFront pf : filtered) {
                allSolutions.addAll(pf.getSolutions());
            }
            
            allSolutions = ParetoFrontUtils.removeRepeatedSolutions(allSolutions);
            allSolutions = ParetoFrontUtils.removeDominatedSolutions(allSolutions);

            ParetoFront pfknownParetoFront = new ParetoFront(file, allSolutions);
            
            ExportUtils.toFile(Paths.get(basename).resolve("pfknow.txt"), pfknownParetoFront, separator);
            
            System.out.println(filtered.size());
        }
    }

    public static List<Solution<?>> removeDominatedSolutions(List<Solution<?>> population) {
        
        checkNotNull(population, "Population cannot be null");
        
        if (population.size() == 0) {
            return population;
        }

        return SolutionListUtils.getNonDominatedSolutions(population);
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
    
    public static boolean contains(Front front, Point p1) {

        checkNotNull(front, "front cannot be null");
        checkNotNull(p1, "p1 cannot be null");

        for (int i = 0; i < front.getNumberOfPoints(); i++) {

            Point p2 = front.getPoint(i);

            if (isEqual(p1, p2)) {
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
    
    public static boolean isEqual(Point p1, Point p2) {
        
        checkNotNull(p1, "p1 cannot be null");
        checkNotNull(p2, "p2 cannot be null");
        checkArgument(p1.getDimension() == p2.getDimension(), "points should have the same dimension");
        
        for (int i = 0; i < p1.getDimension(); i++) {
            
            if (p1.getValue(i) != p2.getValue(i)) {
                return false;
            }
        }

        return true;
    } 
}
