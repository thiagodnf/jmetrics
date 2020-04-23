package thiagodnf.jmetrics.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.point.PointSolution;

import lombok.extern.slf4j.Slf4j;
import thiagodnf.jmetrics.constant.Separator;
import thiagodnf.jmetrics.model.ParetoFront;

@Slf4j
public class ImportUtils {
    
    public static List<Solution<?>> getFromFile(File file, Separator separator) {
        
        log.debug("Reading file: \033[0;32m {} \u001b[0m", file.getName());
        
        List<Solution<?>> population = new ArrayList<>();

        LineIterator it = null;

        int maxObjectives = -1;
        
        try {
            
            it = FileUtils.lineIterator(file, "UTF-8");
            
            while (it.hasNext()) {
                
                String line = it.nextLine();
                
                String[] split = line.split(separator.getRegex());
                
                if (split.length == 1 && split[0].isEmpty()) {
                    continue;
                }
                
                int numberOfObjectives = split.length;

                if (maxObjectives == -1) {
                    maxObjectives = numberOfObjectives;
                } else if (maxObjectives != numberOfObjectives) {
                    throw new IllegalArgumentException("The filename " + file + " has different number of objectives");
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
    
    public static List<Path> readFilesGivenADirectory(Path directory) throws IOException {
        
        return Files.walk(directory)
              .filter(Files::isRegularFile)
              .filter(file -> !file.getFileName().toString().equalsIgnoreCase(".DS_Store"))
              .filter(file -> !file.getFileName().toString().equalsIgnoreCase("pareto-front.txt"))
              .filter(file -> !file.getFileName().toString().equalsIgnoreCase("result.csv"))
              .collect(Collectors.toList());
    }
    
    public static Optional<ParetoFront> readApproxParetoFront(Path directory, Separator separator) throws IOException {

        log.info("Reading Approx pareto-front from the directory");
        
        Path file = directory.resolve("pareto-front.txt");

        if (!Files.exists(file)) {
            return Optional.empty();
        }

        List<Solution<?>> solutions = getFromFile(file.toFile(), separator);

        solutions = ParetoFrontUtils.removeRepeatedSolutions(solutions);

        ParetoFront parentoFront = new ParetoFront(file, solutions);
        
        log.info("Done");
        
        return Optional.of(parentoFront);
    }
    
    /**
     * This method returns the pareto-fronts inside the folder, by ignoring the
     * pareto-front.txt file
     * 
     * @param directory to read the files
     * @param separator the separator
     * @return a list of pareto-fronts
     * @throws IOException if an I/O error is thrown when accessing the starting file.
     */
    public static List<ParetoFront> readParetoFronts(Path directory, Separator separator) throws IOException {

        log.info("Reading all pareto-fronts on directory");
        
        List<Path> files = ImportUtils.readFilesGivenADirectory(directory);

        List<ParetoFront> paretoFronts = new ArrayList<>();

        for (Path file : files) {

            List<Solution<?>> solutions = getFromFile(file.toFile(), separator);

            solutions = ParetoFrontUtils.removeRepeatedSolutions(solutions);

            paretoFronts.add(new ParetoFront(file, solutions));
        }
        
        log.info("Done");

        return paretoFronts;
    }
}
