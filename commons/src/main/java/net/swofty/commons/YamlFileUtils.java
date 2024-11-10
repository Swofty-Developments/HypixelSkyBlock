package net.swofty.commons;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class YamlFileUtils {
    /**
     * Get all YAML files in a directory and its subdirectories
     * @param directory The base directory to search in
     * @return A list of File objects representing YAML files
     * @throws IOException If an I/O error occurs
     */
    public static List<File> getYamlFiles(File directory) throws IOException {
        if (!directory.exists()) {
            directory.mkdirs();
            return new ArrayList<>();
        }

        try (Stream<Path> walk = Files.walk(directory.toPath())) {
            return walk
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().toLowerCase().endsWith(".yml") ||
                            p.toString().toLowerCase().endsWith(".yaml"))
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Load a YAML file and return its contents as a Map
     * @param file The YAML file to load
     * @return Map containing the YAML contents
     * @throws IOException If an I/O error occurs
     */
    public static Map<String, Object> loadYaml(File file) throws IOException {
        try (FileInputStream input = new FileInputStream(file)) {
            Yaml yaml = new Yaml();
            return yaml.load(input);
        }
    }

    /**
     * Copy a directory from resources to a file system location
     * @param resourcePath The path in resources to copy from
     * @param destinationDir The destination directory
     * @throws IOException If an I/O error occurs
     */
    public static void copyFromResources(String resourcePath, File destinationDir) throws IOException {
        ClassLoader classLoader = YamlFileUtils.class.getClassLoader();

        try (InputStream input = classLoader.getResourceAsStream(resourcePath)) {
            if (input == null) {
                throw new FileNotFoundException("Resource not found: " + resourcePath);
            }

            // Create properties to collect resource listings
            Properties properties = new Properties();
            properties.load(input);

            // Copy each file
            for (String resourceFile : properties.stringPropertyNames()) {
                String fullResourcePath = resourcePath + "/" + resourceFile;
                File destFile = new File(destinationDir, resourceFile);

                try (InputStream fileInput = classLoader.getResourceAsStream(fullResourcePath)) {
                    if (fileInput == null) continue;

                    Files.copy(fileInput, destFile.toPath(),
                            StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    /**
     * Ensure a directory exists, creating it if necessary
     * @param directory The directory to check/create
     * @return true if the directory exists or was created, false otherwise
     */
    public static boolean ensureDirectoryExists(File directory) {
        if (!directory.exists()) {
            return directory.mkdirs();
        }
        return directory.isDirectory();
    }
}