package thiagodnf.jmetrics.model;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.uma.jmetal.solution.Solution;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParetoFront {

    protected Path path;

    protected List<Solution<?>> solutions;

    protected Map<String, Double> metrics;
    
    protected long executionTime;
    
    public ParetoFront(Path path, List<Solution<?>> solutions) {
        this.path = path;
        this.solutions = solutions;
        this.metrics = new HashMap<>();
    }
}
