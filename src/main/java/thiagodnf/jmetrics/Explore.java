package thiagodnf.jmetrics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.stream.Collectors;

import org.uma.jmetal.solution.Solution;

import thiagodnf.jmetrics.util.MetricUtils;
import thiagodnf.jmetrics.util.ParetoFrontUtils;

public class Explore {

    private static String directory = "src/main/resources/example-1/";
    
    public static void main(String[] args) throws IOException {

        System.out.println("Generating");

        Path path = Paths.get(directory);

        List<Path> files = Files.walk(path)
                .filter(Files::isRegularFile)
                .filter(file -> !file.getFileName().toString().equalsIgnoreCase(".DS_Store"))
                .filter(file -> !file.getFileName().toString().equalsIgnoreCase("APPROX.tsv"))
                .collect(Collectors.toList());
        
        Map<String, List<Solution<?>>> maps = new HashMap<>();

        // Read files
        
        for (Path file : files) {

            List<Solution<?>> population = ParetoFrontUtils.getFromFile(file.toFile());

            population = ParetoFrontUtils.removeRepeatedSolutions(population);

            maps.put(file.getFileName().toString(), population);
        }
        
        // Create the approx pareto-front
        
        List<Solution<?>> approxParetoFront = new ArrayList<>();

        for (List<Solution<?>> solutions : maps.values()) {
            approxParetoFront.addAll(solutions);
        }
        
        approxParetoFront = ParetoFrontUtils.removeRepeatedSolutions(approxParetoFront);
        approxParetoFront = ParetoFrontUtils.removeDominatedSolutions(approxParetoFront);
        
        ParetoFrontUtils.toFile(directory + "APPROX.tsv", approxParetoFront);
        
        List<String> selectedMetrics = Arrays.asList(
            "Hypervolume Approx (N)",
            "Hypervolume Approx",
            "IGD (N)",
            "IGD"
        );

        StringBuilder builder = new StringBuilder();
        
        builder.append("file").append(",");
        
        for(String selectedMetric : selectedMetrics) {
            builder.append(selectedMetric).append(",");
        }
        
        builder.append("\n");
        
        for (Entry<String, List<Solution<?>>> entry : maps.entrySet()) {

            Properties metrics = MetricUtils.calculate(approxParetoFront, entry.getValue());

            builder.append(entry.getKey()).append(",");
            
            for(String selectedMetric : selectedMetrics) {
                builder.append(metrics.get(selectedMetric)).append(",");
            }
            
            builder.append("\n");
        }
        
        System.out.println(builder.toString());
    }
}
