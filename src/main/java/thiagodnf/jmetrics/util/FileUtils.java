package thiagodnf.jmetrics.util;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {
    
    /**
     * @param folder should not be null and should exists
     * @param extension should not be null
     * 
     * @return all files from a given folder and file extension
     * 
     * @throws IOException if an I/O error occurs 
     */
    public static List<Path> getFilesFromFolder(Path folder, String extension) throws IOException {
        
        checkArgument(FileUtils.isValid(folder), "folder should be valid");
        checkNotNull(extension, "extension should not be null");
        
        return Files.walk(folder)
                .filter(Files::isRegularFile)
                .filter(file -> !file.getFileName().toString().equalsIgnoreCase("pareto-front.txt"))
                .filter(s -> s.toString().endsWith(extension))
                .collect(Collectors.toList());
    }
    
    /**
     * @param path to be validated
     * @return True if path is not null and exists, otherwise, false
     */
    public static boolean isValid(Path path) {

        if (path != null && Files.exists(path)) {
            return true;
        }

        return false;
    }
    
    public static void write(Path file, List<String> lines) {

        try {
            Files.write(file, lines);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}
