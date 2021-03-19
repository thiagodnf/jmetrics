package thiagodnf.jmetrics.util;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.point.PointSolution;

import lombok.extern.slf4j.Slf4j;
import thiagodnf.jmetrics.constant.Separator;
import thiagodnf.jmetrics.model.ParetoFront;

@Slf4j
public class ImportUtils {
    
    public static List<Solution<?>> getFromFile(Path file, Separator separator) {
        
        checkArgument(FileUtils.isValid(file), "file should be valid");
        checkNotNull(separator, "separator should be null");
        
        List<Solution<?>> population = new ArrayList<>();

        int maxObjectives = -1;
        
        try {
            
            List<String> lines = Files.readAllLines(file);

            // Remove the empty rows
            lines.forEach(String::trim);
            lines.removeIf(String::isBlank);

            if (lines.isEmpty()) {
                throw new IllegalArgumentException(String.format("%s is empty", file));
            }
            
            for (int i = 0; i < lines.size(); i++) {
                
                String line = lines.get(i);

                String[] split = line.split(separator.getRegex());
                
                int numberOfObjectives = split.length;

                if (maxObjectives == -1) {
                    maxObjectives = numberOfObjectives;
                } else if (maxObjectives != numberOfObjectives) {
                    throw new IllegalArgumentException(String.format("%s has different number of objectives on Line %s", file, i + 1));
                }

                PointSolution s = new PointSolution(numberOfObjectives);

                for (int j = 0; j < numberOfObjectives; j++) {
                    
                    if (split[j].isBlank()) {
                        throw new IllegalArgumentException(String.format("%s on Line %s and Column %s is blank", file, i + 1, j + 1));
                    }
                    
                    s.setObjective(j, Double.valueOf(split[j]));
                }

                population.add(s);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        
        return population;
    }
    
    
    
    /**
     * @param directory to read the files
     * @param separator the separator
     * @return the pareto-fronts inside the folder, by ignoring the
     *         'pareto-front.txt' file
     * @throws IOException if an I/O error happens
     */
    public static List<ParetoFront> readParetoFronts(Path directory, Separator separator) throws IOException {

        log.info("Reading all pareto-fronts from the directory");
        
        List<Path> files = FileUtils.getFilesFromFolder(directory, ".txt");
        
        List<ParetoFront> paretoFronts = new ArrayList<>();
        
        int done = 1;

        for (Path file : files) {

            log.info("[{}/{}] {}", done++, files.size(), file);
            
            List<Solution<?>> solutions = getFromFile(file, separator);

            solutions = ParetoFrontUtils.removeRepeatedSolutions(solutions);

            paretoFronts.add(new ParetoFront(file, solutions));
        }
        
        return paretoFronts;
    }
}
