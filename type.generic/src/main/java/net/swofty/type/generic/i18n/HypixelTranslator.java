package net.swofty.type.generic.i18n;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.translation.MiniMessageTranslator;
import net.kyori.adventure.translation.Translator;
import net.swofty.commons.StringUtility;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HypixelTranslator extends MiniMessageTranslator {

    public static final Locale defaultLocale = Locale.US;
    private final Map<Locale, Map<String, String>> translationsByLocale = new HashMap<>();
    private static final int EXPECTED_KEYS_PER_LOCALE = 1_000; // scale this based on actual usage to optimize HashMap sizing

    private static final Yaml YAML = createYaml();

    public HypixelTranslator() {
        super(MiniMessage.miniMessage());
        loadTranslationsRecursively(Path.of("./configuration/i18n"));
    }

    private void loadTranslationsRecursively(Path root) {
        if (root == null || !Files.isDirectory(root)) {
            return;
        }

        final List<Path> yamlFiles = new ArrayList<>(256);
        try (var paths = Files.walk(root)) {
            paths
                .filter(Files::isRegularFile)
                .filter(path -> {
                    String n = path.getFileName().toString().toLowerCase(Locale.ROOT);
                    return n.endsWith(".yml") || n.endsWith(".yaml");
                })
                .forEach(yamlFiles::add);
        } catch (IOException ignored) {
            return;
        }

        yamlFiles.sort(Path::compareTo);
        for (Path file : yamlFiles) {
            loadTranslationFile(file);
        }
    }

    private void loadTranslationFile(Path file) {
        String localeCandidate;

        String fileName = file.getFileName().toString();
        int dot = fileName.lastIndexOf('.');
        localeCandidate = dot > 0 ? fileName.substring(0, dot) : fileName;

        Locale locale = Translator.parseLocale(localeCandidate);
        if (locale == null) {
            return;
        }

        Map<String, String> parsed = parseFlatStringYaml(file);
        if (parsed.isEmpty()) {
            return;
        }

        Map<String, String> localeMap = translationsByLocale.computeIfAbsent(locale, __ -> new HashMap<>(EXPECTED_KEYS_PER_LOCALE, 0.75f));
        localeMap.putAll(parsed);
    }

    private static Map<String, String> parseFlatStringYaml(Path file) {
        try (InputStream in = Files.newInputStream(file)) {
            Object loaded = YAML.load(in);
            if (!(loaded instanceof Map<?, ?> map) || map.isEmpty()) {
                return Map.of();
            }

            Map<String, String> out = new HashMap<>(Math.max(16, (int) (map.size() / 0.75f) + 1), 0.75f);
            for (Map.Entry<?, ?> e : map.entrySet()) {
                if (!(e.getKey() instanceof String k)) continue;
                if (!(e.getValue() instanceof String v)) continue;
                out.put(k, StringUtility.unescapeJava(v));
            }
            return out;
        } catch (IOException ignored) {
            return Map.of();
        }
    }

    private static Yaml createYaml() {
        LoaderOptions options = new LoaderOptions();
        options.setMaxAliasesForCollections(50);
        options.setAllowDuplicateKeys(false);
        return new Yaml(new SafeConstructor(options));
    }

    @Override
    public @Nullable String getMiniMessageString(@NotNull String key, @NotNull Locale locale) {
        String result = getTranslation(key, locale);
        if (result == null && !locale.equals(defaultLocale)) {
            result = getTranslation(key, defaultLocale);
        }
        return result;
    }

    private @Nullable String getTranslation(String key, Locale locale) {
        Map<String, String> localeMap = translationsByLocale.get(locale);
        return localeMap == null ? null : localeMap.get(key);
    }

    @Override
    public @NotNull Key name() {
        return Key.key("hypixel:translator");
    }

}
