package net.swofty.type.skyblockgeneric.data.crystals;

import net.minestom.server.coordinate.Pos;
import net.swofty.commons.ServerType;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.HypixelConst;
import org.tinylog.Logger;
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

public final class CrystalCatalog {
    private static final String RESOURCE = "/Minestom.crystals.yml";
    private static final Path LOCATION_FILE = Path.of("configuration/skyblock/Minestom.crystals.yml");
    private static final List<CrystalEntry> CRYSTALS = new ArrayList<>();

    private static Map<String, Object> document;
    private static boolean loaded;

    private CrystalCatalog() {
    }

    public static synchronized List<CrystalData> getFromAround(ServerType type, Pos position, double distance) {
        load();
        List<CrystalData> crystals = new ArrayList<>();
        for (CrystalEntry crystal : CRYSTALS) {
            if (crystal.serverType != type || crystal.position.distance(position) > distance) {
                continue;
            }
            crystals.add(crystal.toData());
        }
        return crystals;
    }

    public static synchronized List<CrystalData> getAllCrystals() {
        load();
        return CRYSTALS.stream().map(CrystalEntry::toData).toList();
    }

    public static synchronized void addCrystal(String url, Pos position, ItemType itemType) {
        load();
        int id = CRYSTALS.stream().mapToInt(crystal -> crystal.id).max().orElse(0) + 1;
        CRYSTALS.add(new CrystalEntry(
            id,
            url,
            position,
            itemType,
            HypixelConst.getTypeLoader().getType()));
        write();
    }

    public static synchronized void removeCrystals(Pos position, double distance) {
        load();
        boolean removed = CRYSTALS.removeIf(crystal -> crystal.position.distance(position) <= distance);
        if (removed) {
            write();
        }
    }

    private static void load() {
        if (loaded) {
            return;
        }

        document = readDocument();
        Object rawCrystals = document.get("crystals");
        if (!(rawCrystals instanceof List<?> entries)) {
            throw new IllegalStateException("Crystal catalog has no crystals list in " + LOCATION_FILE);
        }

        for (int index = 0; index < entries.size(); index++) {
            Object rawCrystal = entries.get(index);
            if (!(rawCrystal instanceof Map<?, ?> fields)) {
                throw invalidEntry(index, rawCrystal);
            }
            try {
                CRYSTALS.add(parseCrystal(fields));
            } catch (RuntimeException exception) {
                Logger.error(exception, "Error parsing crystal catalog entry {} - skipping.", index + 1);
            }
        }
        loaded = true;
    }

    private static CrystalEntry parseCrystal(Map<?, ?> fields) {
        int id = intField(fields, fields.containsKey("id") ? "id" : "_id");
        String url = stringField(fields, "url");
        Pos position = new Pos(
            doubleField(fields, "x"),
            doubleField(fields, "y"),
            doubleField(fields, "z"));
        ServerType serverType = ServerType.getSkyblockServer(
            stringField(fields, "serverType").toUpperCase(Locale.ROOT));
        ItemType itemType = ItemType.valueOf(stringField(fields, "itemType").toUpperCase(Locale.ROOT));
        return new CrystalEntry(id, url, position, itemType, serverType);
    }

    private static Map<String, Object> readDocument() {
        try (InputStream source = openSource(); InputStreamReader reader = new InputStreamReader(source, StandardCharsets.UTF_8)) {
            Object rawDocument = new Yaml().load(reader);
            if (!(rawDocument instanceof Map<?, ?> fields)) {
                throw new IllegalStateException("Invalid crystal catalog in " + LOCATION_FILE);
            }

            Map<String, Object> result = new LinkedHashMap<>();
            for (Map.Entry<?, ?> entry : fields.entrySet()) {
                if (!(entry.getKey() instanceof String key)) {
                    throw new IllegalStateException("Crystal catalog contains a non-text key in " + LOCATION_FILE);
                }
                result.put(key, entry.getValue());
            }
            return result;
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read crystal catalog", exception);
        }
    }

    private static InputStream openSource() throws IOException {
        if (Files.isRegularFile(LOCATION_FILE)) {
            return Files.newInputStream(LOCATION_FILE);
        }

        InputStream resource = CrystalCatalog.class.getResourceAsStream(RESOURCE);
        if (resource == null) {
            throw new FileNotFoundException(LOCATION_FILE.toString());
        }
        return resource;
    }

    private static void write() {
        List<Map<String, Object>> entries = new ArrayList<>();
        for (CrystalEntry crystal : CRYSTALS) {
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("id", crystal.id);
            entry.put("url", crystal.url);
            entry.put("x", crystal.position.x());
            entry.put("y", crystal.position.y());
            entry.put("z", crystal.position.z());
            entry.put("serverType", crystal.serverType.name());
            entry.put("itemType", crystal.itemType.name());
            entries.add(entry);
        }
        document.put("crystals", entries);

        Path temporaryFile = null;
        try {
            Files.createDirectories(LOCATION_FILE.getParent());
            temporaryFile = Files.createTempFile(LOCATION_FILE.getParent(), "Minestom.crystals", ".yml.tmp");
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
            throw new IllegalStateException("Unable to write crystal catalog", exception);
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

    private static double doubleField(Map<?, ?> fields, String name) {
        Object value = fields.get(name);
        if (!(value instanceof Number number)) {
            throw new IllegalArgumentException("Missing numeric field " + name);
        }
        return number.doubleValue();
    }

    private static String stringField(Map<?, ?> fields, String name) {
        Object value = fields.get(name);
        if (!(value instanceof String string)) {
            throw new IllegalArgumentException("Missing text field " + name);
        }
        return string;
    }

    private static IllegalStateException invalidEntry(int index, Object entry) {
        return new IllegalStateException("Invalid crystal catalog entry " + (index + 1) + ": " + entry);
    }

    private record CrystalEntry(int id, String url, Pos position, ItemType itemType, ServerType serverType) {
        private CrystalData toData() {
            return new CrystalData(url, position.add(0.5, 0, 0.5), itemType, serverType);
        }
    }

    public static class CrystalData {
        public final String url;
        public final Pos position;
        public final ItemType itemType;
        public final ServerType serverType;

        private CrystalData(String url, Pos position, ItemType itemType, ServerType serverType) {
            this.url = url;
            this.position = position;
            this.itemType = itemType;
            this.serverType = serverType;
        }
    }
}
