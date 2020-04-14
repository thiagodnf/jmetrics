package thiagodnf.jmetrics.util;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.point.PointSolution;

public class ParetoFrontUtils {

    public static void toFile(String filename, List<Solution<?>> population) {
        
        new SolutionListOutput(population)
            .setSeparator("\t")
            .setFunFileOutputContext(new DefaultFileOutputContext(filename))
            .print();
    }
    
    public static List<Solution<?>> getFromFile(File file) {
        return getFromFile(file.getAbsolutePath());
    }
    
    public static List<Solution<?>> getFromFile(String filename) {
        
        List<Solution<?>> population = new ArrayList<>();

        LineIterator it = null;

        int maxObjectives = -1;
        
        try {
            
            it = FileUtils.lineIterator(new File(filename), "UTF-8");
            
            while (it.hasNext()) {
                
                String[] split = it.nextLine().split(" ");
                
                if (split.length == 1 && split[0].isEmpty()) {
                    continue;
                }
                
                int numberOfObjectives = split.length;

                if (maxObjectives == -1) {
                    maxObjectives = numberOfObjectives;
                } else if (maxObjectives != numberOfObjectives) {
                    throw new IllegalArgumentException("The filename " + filename + " has different number of objectives");
                }
                
                PointSolution s = new PointSolution(numberOfObjectives);

                for (int i = 0; i < numberOfObjectives; i++) {
                    s.setObjective(i, Double.valueOf(split[i]));
                }

                population.add(s);
            }
        } catch (FileNotFoundException ex) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (it != null) {
                it.close();
            }
        }

        return population;
    }
    
    public static List<Solution<?>> removeDominatedSolutions(List<Solution<?>> population) {

        if (population.size() == 0) {
            return population;
        }

        return SolutionListUtils.getNondominatedSolutions(population);
    }
    
    public static List<Solution<?>> removeRepeatedSolutions(List<Solution<?>> population){
        
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
        
        if (population == null || s1 == null) {
            throw new IllegalArgumentException("SolutionSet and Solution cannot be null");
        }

        for (int i = 0; i < population.size(); i++) {
            
            Solution<?> s2 = population.get(i);

            if (isEqual(s1, s2)) {
                return true;
            }
        }
        
        return false;
    }
    
    public static boolean isEqual(Solution<?> s1, Solution<?> s2) {
        
        if (s1 == null || s2 == null) {
            throw new IllegalArgumentException("Soluton s1 and s2 cannot be null");
        }
        
        if (s1.getNumberOfObjectives() != s2.getNumberOfObjectives()) {
            throw new IllegalArgumentException("Solutions cannot be different number of objetives");
        }
        
        for (int i = 0; i < s1.getNumberOfObjectives(); i++) {
            
            if (s1.getObjective(i) != s2.getObjective(i)) {
                return false;
            }
        }

        return true;
    } 
}
