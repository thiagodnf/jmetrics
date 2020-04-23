package thiagodnf.jmetrics.constant;

import lombok.Getter;

@Getter
public enum Separator {
    
    Comma(","),
    
    SemiColon(";"),
    
    Colon(":"),
    
    Bar("|"),
    
    Tab("\t"),
    
    Space(" ")
    
    ;
    
    private String regex;
    
    private Separator(String regex) {
        this.regex = regex;
    }
}
