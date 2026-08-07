package net.swofty.type.skyblockgeneric.data.fairysouls;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.skyblockgeneric.user.fairysouls.FairySoul;
import net.swofty.type.skyblockgeneric.user.fairysouls.FairySoulZone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public final class FairySoulCatalog {
    private static final String CATALOG_RESOURCE = "/Minestom.fairysouls.yml";
    private static final Path LOCATION_FILE = Path.of("configuration/skyblock/Minestom.fairysouls.yml");
    private static final List<FairySoul> SOULS = load();

    private FairySoulCatalog() {
    }

    public static List<FairySoul> getAllSouls() {
        return SOULS;
    }

    private static List<FairySoul> load() {
        List<FairySoul> souls = new ArrayList<>();
        try (InputStream source = openSource();
             BufferedReader reader = new BufferedReader(new InputStreamReader(source, StandardCharsets.UTF_8))) {
            Object rawCatalog = new Yaml().load(reader);
            if (!(rawCatalog instanceof Map<?, ?> catalog)) {
                throw new IllegalStateException("Invalid fairy soul catalog in " + LOCATION_FILE);
            }

            Object rawSouls = catalog.get("souls");
            if (!(rawSouls instanceof List<?> entries)) {
                throw new IllegalStateException("Fairy soul catalog has no souls list in " + LOCATION_FILE);
            }

            for (int index = 0; index < entries.size(); index++) {
                Object rawSoul = entries.get(index);
                if (!(rawSoul instanceof Map<?, ?> fields)) {
                    throw invalidEntry(index, rawSoul);
                }

                try {
                    int id = intField(fields, "id");
                    FairySoulZone zone = FairySoulZone.valueOf(stringField(fields, "zone"));
                    int x = intField(fields, "x");
                    int y = intField(fields, "y");
                    int z = intField(fields, "z");
                    Pos location = zone.getServerType() == null ? null : new Pos(x + 0.5, y, z + 0.5);
                    souls.add(new FairySoul(id, location, zone));
                } catch (IllegalArgumentException exception) {
                    throw invalidEntry(index, rawSoul, exception);
                }
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read fairy soul catalog", exception);
        }

        return Collections.unmodifiableList(souls);
    }

    private static int intField(Map<?, ?> fields, String name) {
        Object value = fields.get(name);
        if (!(value instanceof Number number)) {
            throw new IllegalArgumentException("Missing numeric field " + name);
        }
        return number.intValue();
    }

    private static String stringField(Map<?, ?> fields, String name) {
        Object value = fields.get(name);
        if (!(value instanceof String string)) {
            throw new IllegalArgumentException("Missing text field " + name);
        }
        return string;
    }

    private static InputStream openSource() throws IOException {
        InputStream resource = FairySoulCatalog.class.getResourceAsStream(CATALOG_RESOURCE);
        return resource != null ? resource : Files.newInputStream(LOCATION_FILE);
    }

    private static IllegalStateException invalidEntry(int index, Object entry) {
        return new IllegalStateException("Invalid fairy soul catalog entry " + (index + 1) + ": " + entry);
    }

    private static IllegalStateException invalidEntry(int index, Object entry, Exception cause) {
        return new IllegalStateException("Invalid fairy soul catalog entry " + (index + 1) + ": " + entry, cause);
    }
}
