package thiagodnf.jmetrics.constant;

import java.util.EnumSet;

public enum MetricType {
    
    Hypervolume,
    
    HypervolumeApprox,
    
    Epsilon,
    
    GD,
    
    IGD,
    
    IGDPlus,
    
    Spread,
    
    ErrorRatio,
    
    Contribution,
    
    ProportionalContribution,
    
    ExecutionTime,
    
    NumberOfSolutions,
    
    ALL
    
    ;
    
    public static EnumSet<MetricType> getAll() {
       
        EnumSet<MetricType> all =  EnumSet.allOf(MetricType.class);
        
        all.remove(MetricType.ALL);
        
        return all;
    }
}
