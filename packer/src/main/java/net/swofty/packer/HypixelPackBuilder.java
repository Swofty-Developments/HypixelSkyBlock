package net.swofty.packer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.kyori.adventure.key.Key;
import team.unnamed.creative.BuiltResourcePack;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.lang.Language;
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackReader;
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackWriter;
import team.unnamed.creative.texture.Texture;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class HypixelPackBuilder {
    private static final int PACK_FORMAT = 75;
    private static final int MIN_FORMAT = PACK_FORMAT;
    private static final int MAX_FORMAT = PACK_FORMAT;
    private static final int MODERN_FORMAT_THRESHOLD = 64;
    private static final long ZIP_ENTRY_TIMESTAMP_MILLIS = 0L;

    private final PackDefinition definition;

    public HypixelPackBuilder(PackDefinition definition) {
        this.definition = definition;
    }

    public BuiltResourcePack build() {
        File packDirectory = new File(definition.getPackDirectory()).getAbsoluteFile();
        if (!packDirectory.isDirectory()) {
            throw new IllegalStateException("Pack directory does not exist: " + packDirectory.getPath());
        }

        ResourcePack pack = MinecraftResourcePackReader.minecraft()
                .readFromDirectory(packDirectory);
        pack.packMeta(PACK_FORMAT, "Hypixel");

        addCustomTextures(pack);
        addCustomFontProviders(pack);
        applyLangOverrides(pack);

        BuiltResourcePack built = MinecraftResourcePackWriter.minecraft().build(pack);
        return ensureModernPackMeta(built);
    }

    private void addCustomTextures(ResourcePack pack) {
        File texturesDirFile = new File(definition.getTexturesDirectory());
        if (!texturesDirFile.exists() || !texturesDirFile.isDirectory()) return;

        File[] files = texturesDirFile.listFiles((dir, name) -> name.endsWith(".png"));
        if (files == null) return;
        java.util.Arrays.sort(files, Comparator.comparing(File::getName));

        for (File textureFile : files) {
            pack.texture(Texture.texture(
                    Key.key("hypixel", textureFile.getName()),
                    Writable.file(textureFile)
            ));
        }
    }

    private void addCustomFontProviders(ResourcePack pack) {
        List<FontProvider> customProviders = definition.getFontProviders();
        if (customProviders.isEmpty()) return;

        Key defaultFontKey = Key.key("minecraft", "default");
        Font existingFont = pack.font(defaultFontKey);

        List<FontProvider> providers = new ArrayList<>();
        if (existingFont != null) {
            providers.addAll(existingFont.providers());
        }
        providers.addAll(customProviders);

        pack.font(Font.font(defaultFontKey, providers));
    }

    private void applyLangOverrides(ResourcePack pack) {
        Map<String, String> overrides = definition.getLangOverrides();
        if (overrides.isEmpty()) return;

        Key enUsKey = Key.key("minecraft", "en_us");
        Language existingLang = pack.language(enUsKey);

        Map<String, String> translations;
        if (existingLang != null) {
            translations = new TreeMap<>(existingLang.translations());
        } else {
            translations = new TreeMap<>();
        }

        translations.putAll(overrides);
        pack.language(Language.language(enUsKey, new LinkedHashMap<>(translations)));
    }

    private BuiltResourcePack ensureModernPackMeta(BuiltResourcePack built) {
        if (PACK_FORMAT <= MODERN_FORMAT_THRESHOLD) {
            return built;
        }

        byte[] rewritten = rewritePackMcmeta(built.bytes());
        return BuiltResourcePack.of(rewritten, sha1Hex(rewritten));
    }

    private byte[] rewritePackMcmeta(byte[] zipBytes) {
        try (
                ByteArrayInputStream inputBuffer = new ByteArrayInputStream(zipBytes);
                ZipInputStream zipInput = new ZipInputStream(inputBuffer);
                ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
                ZipOutputStream zipOutput = new ZipOutputStream(outputBuffer)
        ) {
            ZipEntry entry;
            boolean sawPackMeta = false;
            List<ZipContentEntry> entries = new ArrayList<>();
            while ((entry = zipInput.getNextEntry()) != null) {
                byte[] content = readAllBytes(zipInput);
                String name = entry.getName();

                if ("pack.mcmeta".equals(name)) {
                    sawPackMeta = true;
                    entries.add(new ZipContentEntry(name, rewritePackMetaJson(content)));
                } else {
                    entries.add(new ZipContentEntry(name, content));
                }
                zipInput.closeEntry();
            }

            if (!sawPackMeta) {
                entries.add(new ZipContentEntry("pack.mcmeta", rewritePackMetaJson(new byte[0])));
            }

            entries.sort(Comparator.comparing(ZipContentEntry::name));

            for (ZipContentEntry contentEntry : entries) {
                ZipEntry outEntry = new ZipEntry(contentEntry.name());
                outEntry.setTime(ZIP_ENTRY_TIMESTAMP_MILLIS);
                zipOutput.putNextEntry(outEntry);
                zipOutput.write(contentEntry.content());
                zipOutput.closeEntry();
            }

            zipOutput.finish();
            return outputBuffer.toByteArray();
        } catch (IOException exception) {
            throw new UncheckedIOException("Failed to rewrite pack.mcmeta in built resource pack", exception);
        }
    }

    private byte[] rewritePackMetaJson(byte[] existingContent) {
        JsonObject root = parseRootObject(existingContent);
        JsonObject packMeta = root.has("pack") && root.get("pack").isJsonObject()
                ? root.getAsJsonObject("pack")
                : new JsonObject();

        packMeta.remove("supported_formats");
        packMeta.remove("pack_format");
        packMeta.addProperty("min_format", MIN_FORMAT);
        packMeta.addProperty("max_format", MAX_FORMAT);
        if (!packMeta.has("description")) {
            packMeta.addProperty("description", "Hypixel");
        }

        root.add("pack", packMeta);
        return toCanonicalJson(root).getBytes(StandardCharsets.UTF_8);
    }

    private JsonObject parseRootObject(byte[] content) {
        if (content.length == 0) {
            return new JsonObject();
        }
        try {
            return JsonParser.parseString(new String(content, StandardCharsets.UTF_8)).getAsJsonObject();
        } catch (Exception ignored) {
            return new JsonObject();
        }
    }

    private byte[] readAllBytes(InputStream stream) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = stream.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        return output.toByteArray();
    }

    private String sha1Hex(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            return HexFormat.of().formatHex(digest.digest(data));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-1 algorithm is unavailable", exception);
        }
    }

    private String toCanonicalJson(JsonElement element) {
        if (element == null || element.isJsonNull()) {
            return "null";
        }

        if (element.isJsonObject()) {
            JsonObject object = element.getAsJsonObject();
            List<String> keys = new ArrayList<>(object.keySet());
            keys.sort(String::compareTo);

            StringBuilder builder = new StringBuilder();
            builder.append('{');
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                if (i > 0) {
                    builder.append(',');
                }
                builder.append('"').append(escapeJson(key)).append('"').append(':');
                builder.append(toCanonicalJson(object.get(key)));
            }
            builder.append('}');
            return builder.toString();
        }

        if (element.isJsonArray()) {
            StringBuilder builder = new StringBuilder();
            builder.append('[');
            int index = 0;
            for (JsonElement entry : element.getAsJsonArray()) {
                if (index++ > 0) {
                    builder.append(',');
                }
                builder.append(toCanonicalJson(entry));
            }
            builder.append(']');
            return builder.toString();
        }

        JsonPrimitive primitive = element.getAsJsonPrimitive();
        if (primitive.isString()) {
            return "\"" + escapeJson(primitive.getAsString()) + "\"";
        }
        return primitive.toString();
    }

    private String escapeJson(String input) {
        StringBuilder builder = new StringBuilder(input.length() + 16);
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            switch (c) {
                case '"' -> builder.append("\\\"");
                case '\\' -> builder.append("\\\\");
                case '\b' -> builder.append("\\b");
                case '\f' -> builder.append("\\f");
                case '\n' -> builder.append("\\n");
                case '\r' -> builder.append("\\r");
                case '\t' -> builder.append("\\t");
                default -> {
                    if (c <= 0x1F) {
                        builder.append(String.format("\\u%04x", (int) c));
                    } else {
                        builder.append(c);
                    }
                }
            }
        }
        return builder.toString();
    }

    private record ZipContentEntry(String name, byte[] content) {}
}
