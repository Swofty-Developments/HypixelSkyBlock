package net.swofty.type.skyblockgeneric.data.regions;

import net.minestom.server.coordinate.Pos;
import net.swofty.commons.ServerType;
import net.swofty.type.skyblockgeneric.region.RegionType;
import net.swofty.type.skyblockgeneric.region.SkyBlockRegion;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class RegionCatalog {
    private static final String RESOURCE = "/Minestom.regions.yml";
    private static final Path LOCATION_FILE = Path.of("configuration/skyblock/Minestom.regions.yml");
    private static final List<SkyBlockRegion> REGIONS = new ArrayList<>();

    private static Map<String, Object> document;
    private static boolean loaded;

    private RegionCatalog() {
    }

    public static synchronized List<SkyBlockRegion> getAllRegions() {
        load();
        return List.copyOf(REGIONS);
    }

    public static synchronized void save(SkyBlockRegion region) {
        load();
        REGIONS.removeIf(existing -> existing.getName().equalsIgnoreCase(region.getName()));
        REGIONS.add(region);
        write();
    }

    public static synchronized void delete(String id) {
        load();
        REGIONS.removeIf(existing -> existing.getName().equalsIgnoreCase(id));
        write();
    }

    private static void load() {
        if (loaded) {
            return;
        }

        document = readDocument();
        Object rawRegions = document.get("regions");
        if (!(rawRegions instanceof List<?> entries)) {
            throw new IllegalStateException("Region catalog has no regions list in " + LOCATION_FILE);
        }

        for (int index = 0; index < entries.size(); index++) {
            Object rawRegion = entries.get(index);
            if (!(rawRegion instanceof Map<?, ?> fields)) {
                throw invalidEntry(index, rawRegion);
            }
            REGIONS.add(parseRegion(index, fields));
        }
        loaded = true;
    }

    private static SkyBlockRegion parseRegion(int index, Map<?, ?> fields) {
        try {
            String name = stringField(fields, fields.containsKey("id") ? "id" : "_id");
            RegionType type = RegionType.valueOf(stringField(fields, "type").toUpperCase(Locale.ROOT));
            ServerType serverType = ServerType.getSkyblockServer(
                stringField(fields, "serverType", ServerType.SKYBLOCK_HUB.name()).toUpperCase(Locale.ROOT));

            return new SkyBlockRegion(
                name,
                new Pos(intField(fields, "x1"), intField(fields, "y1"), intField(fields, "z1")),
                new Pos(intField(fields, "x2"), intField(fields, "y2"), intField(fields, "z2")),
                type,
                serverType);
        } catch (RuntimeException exception) {
            throw invalidEntry(index, fields, exception);
        }
    }

    private static Map<String, Object> readDocument() {
        try (InputStream source = openSource(); InputStreamReader reader = new InputStreamReader(source, StandardCharsets.UTF_8)) {
            Object rawDocument = new Yaml().load(reader);
            if (!(rawDocument instanceof Map<?, ?> fields)) {
                throw new IllegalStateException("Invalid region catalog in " + LOCATION_FILE);
            }

            Map<String, Object> result = new LinkedHashMap<>();
            for (Map.Entry<?, ?> entry : fields.entrySet()) {
                if (!(entry.getKey() instanceof String key)) {
                    throw new IllegalStateException("Region catalog contains a non-text key in " + LOCATION_FILE);
                }
                result.put(key, entry.getValue());
            }
            return result;
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read region catalog", exception);
        }
    }

    private static InputStream openSource() throws IOException {
        if (Files.isRegularFile(LOCATION_FILE)) {
            return Files.newInputStream(LOCATION_FILE);
        }

        InputStream resource = RegionCatalog.class.getResourceAsStream(RESOURCE);
        if (resource == null) {
            throw new FileNotFoundException(LOCATION_FILE.toString());
        }
        return resource;
    }

    private static void write() {
        List<Map<String, Object>> entries = new ArrayList<>();
        for (SkyBlockRegion region : REGIONS) {
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("id", region.getName());
            entry.put("type", region.getType().name());
            entry.put("x1", region.getFirstLocation().blockX());
            entry.put("y1", region.getFirstLocation().blockY());
            entry.put("z1", region.getFirstLocation().blockZ());
            entry.put("x2", region.getSecondLocation().blockX());
            entry.put("y2", region.getSecondLocation().blockY());
            entry.put("z2", region.getSecondLocation().blockZ());
            entry.put("serverType", region.getServerType().name());
            entries.add(entry);
        }
        document.put("regions", entries);

        Path temporaryFile = null;
        try {
            Files.createDirectories(LOCATION_FILE.getParent());
            temporaryFile = Files.createTempFile(LOCATION_FILE.getParent(), "Minestom.regions", ".yml.tmp");
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            options.setPrettyFlow(true);
            try (Writer writer = Files.newBufferedWriter(temporaryFile, StandardCharsets.UTF_8)) {
                new Yaml(options).dump(document, writer);
            }

            try {
                Files.move(temporaryFile, LOCATION_FILE, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException exception) {
                Files.move(temporaryFile, LOCATION_FILE, StandardCopyOption.REPLACE_EXISTING);
            }
            temporaryFile = null;
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to write region catalog", exception);
        } finally {
            if (temporaryFile != null) {
                try {
                    Files.deleteIfExists(temporaryFile);
                } catch (IOException ignored) {
                }
            }
        }
    }

    private static int intField(Map<?, ?> fields, String name) {
        Object value = fields.get(name);
        if (!(value instanceof Number number)) {
            throw new IllegalArgumentException("Missing numeric field " + name);
        }
        return number.intValue();
    }

    private static String stringField(Map<?, ?> fields, String name) {
        return stringField(fields, name, null);
    }

    private static String stringField(Map<?, ?> fields, String name, String defaultValue) {
        Object value = fields.get(name);
        if (value == null && defaultValue != null) {
            return defaultValue;
        }
        if (!(value instanceof String string)) {
            throw new IllegalArgumentException("Missing text field " + name);
        }
        return string;
    }

    private static IllegalStateException invalidEntry(int index, Object entry) {
        return new IllegalStateException("Invalid region catalog entry " + (index + 1) + ": " + entry);
    }

    private static IllegalStateException invalidEntry(int index, Object entry, Exception cause) {
        return new IllegalStateException("Invalid region catalog entry " + (index + 1) + ": " + entry, cause);
    }
}
