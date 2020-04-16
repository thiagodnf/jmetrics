package thiagodnf.jmetrics.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.solution.Solution;

import lombok.extern.slf4j.Slf4j;
import thiagodnf.jmetrics.constant.MetricType;
import thiagodnf.jmetrics.model.ParetoFront;

@Slf4j
public class ExportUtils {
    
    public static void toCSV(Path folder, List<ParetoFront> paretoFronts, List<MetricType> metrics) throws IOException {

        log.info("Generating output as result.csv file");

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

            row.add(paretoFront.getPath().getFileName().toString());

            for (MetricType metricType : metrics) {

                row.add(String.valueOf(paretoFront.getMetrics().get(metricType.toString())));
                row.add(String.valueOf(paretoFront.getMetrics().get(metricType.toString() + "(N)")));
            }

            lines.add(String.join(",", row));
        }

        Files.write(folder.resolve("result.csv"), lines);

        log.info("Done");
    }
    
    public static void toFile(Path file, ParetoFront paretoFront, String regex) throws IOException {

        List<String> lines = new ArrayList<>();

        List<Solution<?>> solutions = paretoFront.getSolutions();

        if (!solutions.isEmpty()) {

            int numberOfObjectives = solutions.get(0).getNumberOfObjectives();

            for (int i = 0; i < solutions.size(); i++) {

                List<String> row = new ArrayList<>();

                for (int j = 0; j < numberOfObjectives; j++) {
                    row.add(String.valueOf(solutions.get(i).getObjective(j)));
                }

                lines.add(String.join(",", row));
            }
        }

        Files.write(file, lines);
    }
}
