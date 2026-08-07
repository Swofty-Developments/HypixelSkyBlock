package gg.itzkatze.thehypixelrecreationmod.features.hudcapture;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class GlyphAtlas {
    private static final Map<Integer, Entry> GLYPHS = new HashMap<>();
    private static boolean loaded;

    private GlyphAtlas() {
    }

    public static void reload() {
        GLYPHS.clear();
        loaded = true;

        List<Resource> resources;
        try {
            resources = Minecraft.getInstance().getResourceManager()
                    .getResourceStack(Identifier.withDefaultNamespace("font/default.json"));
        } catch (Exception exception) {
            return;
        }

        for (Resource resource : resources) {
            try (Reader reader = new InputStreamReader(resource.open())) {
                JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
                if (!root.has("providers")) {
                    continue;
                }
                for (JsonElement element : root.getAsJsonArray("providers")) {
                    readProvider(element.getAsJsonObject());
                }
            } catch (Exception ignored) {
            }
        }
    }

    private static void readProvider(JsonObject provider) {
        if (!"bitmap".equals(provider.has("type") ? provider.get("type").getAsString() : "")) {
            return;
        }
        String file = provider.has("file") ? provider.get("file").getAsString() : "?";
        int ascent = provider.has("ascent") ? provider.get("ascent").getAsInt() : 0;
        int height = provider.has("height") ? provider.get("height").getAsInt() : 8;
        if (!provider.has("chars")) {
            return;
        }

        JsonArray rows = provider.getAsJsonArray("chars");
        for (JsonElement row : rows) {
            String line = row.getAsString();
            line.codePoints().forEach(codePoint -> {
                if (codePoint != ' ' && codePoint != 0) {
                    GLYPHS.putIfAbsent(codePoint, new Entry(file, ascent, height));
                }
            });
        }
    }

    public static Entry lookup(int codePoint) {
        if (!loaded) {
            reload();
        }
        return GLYPHS.get(codePoint);
    }

    public static int size() {
        if (!loaded) {
            reload();
        }
        return GLYPHS.size();
    }

    public record Entry(String file, int ascent, int height) {
    }
}
