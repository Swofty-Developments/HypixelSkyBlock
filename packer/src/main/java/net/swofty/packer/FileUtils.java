package net.swofty.packer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileUtils {
    public static void deleteDirectory(String directory) throws IOException {
        deleteDirectory(new File(directory));
    }

    public static void deleteDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }

        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                deleteDirectory(file);
            } else {
                Files.delete(file.toPath());
            }
        }

        Files.delete(directory.toPath());
    }

    public static void copyDirectory(String folder, String outputDirectory) throws IOException {
        copyDirectory(new File(folder), new File(outputDirectory));
    }

    public static void copyDirectory(File folder, File outputDirectory) throws IOException {
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }

        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                copyDirectory(file, new File(outputDirectory, file.getName()));
            } else {
                Files.copy(file.toPath(), new File(outputDirectory, file.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }
}
