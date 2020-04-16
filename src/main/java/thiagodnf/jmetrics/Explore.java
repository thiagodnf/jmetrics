package thiagodnf.jmetrics;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import thiagodnf.jmetrics.constant.MetricType;
import thiagodnf.jmetrics.model.ParetoFront;
import thiagodnf.jmetrics.util.ExportUtils;
import thiagodnf.jmetrics.util.MetricUtils;
import thiagodnf.jmetrics.util.ParetoFrontUtils;
import thiagodnf.jmetrics.util.ImportUtils;

@Slf4j
@Command(name = "jMetrics", footer = "Copyright(c) 2020 jMetrics", description = "Evaluate results generated by jMetal Framework")
public class Explore implements Callable<Integer> {

    @Parameters(paramLabel = "folder", description = "folder that has the input files")
    private Path folder;

    @Option(names = { "-m", "--metrics" }, arity = "1..*", description = "values: ${COMPLETION-CANDIDATES}\nDefault: ${DEFAULT-VALUE}")
    private List<MetricType> metrics = Arrays.asList(MetricType.IGD);

    @Option(names = {"-d", "--debug"}, description = "set the level for debugging one")
    private boolean debug = false;
    
    @Option(names = {"-r", "--regex"}, description = "set the column separator")
    private String regex = ",";
    
    @Option(names = {"-h", "--help"}, usageHelp = true, description = "display this help and exit")
    private boolean help;
    
    public static void main(String[] args) throws IOException {

        int exitCode = new CommandLine(new Explore())
                .setCaseInsensitiveEnumValuesAllowed(true)
                .execute(args);
        
        System.exit(exitCode);
    }
    
    @Override
    public Integer call() throws Exception {
        
        ((Logger) LoggerFactory.getLogger("thiagodnf")).setLevel(debug ? Level.DEBUG : Level.INFO);
        
        log.info(String.join("", Collections.nCopies(50, "-")));
        log.info("jMetrics");
        log.info(String.join("", Collections.nCopies(50, "-")));
        
        log.info("Starting");
        log.info("Folder: \033[0;32m {} \u001b[0m", folder);
        log.info("Metrics: \033[0;32m {} \u001b[0m", metrics);
        
        List<ParetoFront> paretoFronts = ImportUtils.readParetoFronts(folder, regex);
        
        Optional<ParetoFront> approxParetoFront = ImportUtils.readApproxParetoFront(folder, regex);

        if (!approxParetoFront.isPresent()) {

            log.info("File not found");

            approxParetoFront = ParetoFrontUtils.generateApproxParetoFront(folder, regex, paretoFronts);
        }

        log.info("Calculate metrics for all files");

        for (ParetoFront paretoFront : paretoFronts) {
            MetricUtils.calculate(approxParetoFront.get(), paretoFront, metrics);
        }
        
        log.info("Done");
        
        ExportUtils.toCSV(folder, paretoFronts, metrics);
        
        return 0;
    }
}
