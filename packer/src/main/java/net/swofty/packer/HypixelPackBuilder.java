package net.swofty.packer;

import team.unnamed.creative.BuiltResourcePack;
import team.unnamed.creative.base.Writable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class HypixelPackBuilder {
    private final PackDefinition definition;

    public HypixelPackBuilder(PackDefinition definition) {
        this.definition = definition;
    }

    public BuiltResourcePack build() {
        Path packDirectory = Path.of(definition.getPackDirectory()).toAbsolutePath();

        if (!Files.isDirectory(packDirectory)) {
            throw new IllegalStateException(
                    "Pack directory does not exist: " + packDirectory
            );
        }

        try {
            byte[] bytes = zipDirectory(packDirectory);

            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            String hash = HexFormat.of().formatHex(digest.digest(bytes));

            return BuiltResourcePack.of(
                    Writable.bytes(bytes),
                    hash
            );
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to build resource pack", e);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-1 is unavailable", e);
        }
    }

    private byte[] zipDirectory(Path directory) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (ZipOutputStream zip = new ZipOutputStream(output);
             Stream<Path> paths = Files.walk(directory)) {

            paths.filter(Files::isRegularFile).forEach(path -> {
                String entryName = directory.relativize(path)
                        .toString()
                        .replace('\\', '/');

                try {
                    zip.putNextEntry(new ZipEntry(entryName));
                    Files.copy(path, zip);
                    zip.closeEntry();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        } catch (UncheckedIOException e) {
            throw e.getCause();
        }

        return output.toByteArray();
    }

}
