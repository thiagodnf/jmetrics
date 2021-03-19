package thiagodnf.jmetrics.util;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.uma.jmetal.solution.Solution;

import lombok.extern.slf4j.Slf4j;
import thiagodnf.jmetrics.constant.MetricType;
import thiagodnf.jmetrics.constant.Separator;
import thiagodnf.jmetrics.model.ParetoFront;

@Slf4j
public class ExportUtils {
    
    public static void toCSV(Path folder, List<ParetoFront> paretoFronts, EnumSet<MetricType> metrics) {

        log.info("Generating output as result.csv file");
        
        Path output = folder.resolve("result.csv");

        List<String> lines = new ArrayList<>();

        List<String> header = new ArrayList<>();

        header.add("file");

        for (MetricType metricType : metrics) {
            header.add(metricType.toString());
            header.add(metricType.toString() + "(N)");
        }

        lines.add(String.join(",", header));

        for (ParetoFront paretoFront : paretoFronts) {

            List<String> row = new ArrayList<>();

            row.add(paretoFront.getPath().toString());

            for (MetricType metricType : metrics) {
                row.add(String.valueOf(paretoFront.getMetrics().get(metricType.toString())));
                row.add(String.valueOf(paretoFront.getMetrics().get(metricType.toString() + "(N)")));
            }

            lines.add(String.join(",", row));
        }

        FileUtils.write(output, lines);
        
        log.info("[1/1] {}", output);
    }
    
    public static void toFile(Path file, ParetoFront paretoFront, Separator separator) {

        List<String> lines = new ArrayList<>();

        List<Solution<?>> solutions = paretoFront.getSolutions();

        if (!solutions.isEmpty()) {

            int numberOfObjectives = solutions.get(0).getNumberOfObjectives();

            for (int i = 0; i < solutions.size(); i++) {

                List<String> row = new ArrayList<>();

                for (int j = 0; j < numberOfObjectives; j++) {
                    row.add(String.valueOf(solutions.get(i).getObjective(j)));
                }

                lines.add(String.join(separator.getRegex(), row));
            }
        }
        
        FileUtils.write(file, lines);       
    }
}
