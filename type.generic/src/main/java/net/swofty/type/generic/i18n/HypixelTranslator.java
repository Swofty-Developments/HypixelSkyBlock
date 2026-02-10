package net.swofty.type.generic.i18n;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.translation.MiniMessageTranslator;
import net.kyori.adventure.translation.Translator;
import net.swofty.commons.StringUtility;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class HypixelTranslator extends MiniMessageTranslator {

    public static final Locale defaultLocale = Locale.US;
    private static final Path I18N_ROOT = Path.of("./configuration/i18n");

    private final Map<Locale, Map<String, Path>> fileIndexByLocale;
    private final Cache<LocaleSubsystem, Map<String, String>> bundleCache;
    private final Cache<LocaleKey, Optional<String>> keyCache;

    public HypixelTranslator() {
        super(MiniMessage.miniMessage());

        this.fileIndexByLocale = buildFileIndex(I18N_ROOT);

        this.bundleCache = Caffeine.newBuilder()
                .maximumSize(64L)
                .expireAfterAccess(Duration.ofMinutes(30))
                .build();

        this.keyCache = Caffeine.newBuilder()
                .maximumSize(250_000L)
                .expireAfterAccess(Duration.ofMinutes(10))
                .build();
    }

    @Override
    public @Nullable String getMiniMessageString(@NotNull String key, @NotNull Locale locale) {
        LocaleKey lk = new LocaleKey(locale, key);
        Optional<String> cached = keyCache.getIfPresent(lk);
        if (cached != null) {
            return cached.orElse(null);
        }

        @Nullable String resolved = resolveTranslation(key, locale);
        if (resolved == null && !locale.equals(defaultLocale)) {
            resolved = resolveTranslation(key, defaultLocale);
        }

        keyCache.put(lk, Optional.ofNullable(resolved));
        return resolved;
    }

    private @Nullable String resolveTranslation(String fullKey, Locale locale) {
        String subsystem = inferSubsystem(fullKey);
        if (subsystem == null) {
            return null;
        }

        Map<String, String> bundle = getBundle(locale, subsystem);
        if (bundle.isEmpty()) {
            return null;
        }
        return bundle.get(fullKey);
    }

    private static @Nullable String inferSubsystem(String key) {
        int dot = key.indexOf('.');
        if (dot <= 0) return null;
        return key.substring(0, dot);
    }

    private @NotNull Map<String, String> getBundle(Locale locale, String subsystem) {
        LocaleSubsystem ls = new LocaleSubsystem(locale, subsystem);
        Map<String, String> existing = bundleCache.getIfPresent(ls);
        if (existing != null) {
            return existing;
        }

        Map<String, Path> localeIndex = fileIndexByLocale.get(locale);
        if (localeIndex == null) {
            bundleCache.put(ls, Map.of());
            return Map.of();
        }

        Path file = localeIndex.get(subsystem);
        if (file == null) {
            bundleCache.put(ls, Map.of());
            return Map.of();
        }

        Map<String, String> loaded = loadPropertiesFileFlat(file);
        Map<String, String> unmodifiable = loaded.isEmpty() ? Map.of() : Map.copyOf(loaded);
        bundleCache.put(ls, unmodifiable);
        return unmodifiable;
    }

    private static Map<Locale, Map<String, Path>> buildFileIndex(Path root) {
        Map<Locale, Map<String, Path>> index = new HashMap<>(16, 0.75f);
        if (!Files.isDirectory(root)) {
            return Map.of();
        }

        try (var paths = Files.walk(root)) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().endsWith(".properties"))
                    .forEach(p -> {
                        Path rel = root.relativize(p);
                        if (rel.getNameCount() < 2) return;

                        Locale locale = Translator.parseLocale(rel.getName(0).toString());
                        if (locale == null) return;

                        String fileName = rel.getName(rel.getNameCount() - 1).toString();
                        int dot = fileName.lastIndexOf('.');
                        String subsystem = dot > 0 ? fileName.substring(0, dot) : fileName;
                        if (subsystem.isEmpty()) return;

                        index.computeIfAbsent(locale, ignored -> new HashMap<>(16, 0.75f))
                                .put(subsystem, p);
                    });
        } catch (IOException ignored) {
            // Best-effort.
        }

        index.replaceAll((ignoredLocale, m) -> Map.copyOf(m));
        return Map.copyOf(index);
    }

    private static Map<String, String> loadPropertiesFileFlat(Path file) {
        int initialCapacity = 1024;
        try {
            long size = Files.size(file);
            if (size > 0) {
                long approxEntries = Math.min(250_000L, Math.max(128L, size / 40L));
                initialCapacity = (int) Math.min(Integer.MAX_VALUE - 8L, (approxEntries / 0.75d) + 1);
            }
        } catch (IOException ignored) {
        }

        Map<String, String> out = new HashMap<>(initialCapacity, 0.75f);

        try (InputStream raw = new BufferedInputStream(Files.newInputStream(file), 1 << 16)) {
            byte[] bytes = raw.readAllBytes();
            if (bytes.length == 0) return Map.of();

            String s = new String(bytes, StandardCharsets.UTF_8);
            int len = s.length();

            int i = 0;
            while (i < len) {
                int lineStart = i;
                int lineEnd = s.indexOf('\n', i);
                if (lineEnd == -1) lineEnd = len;
                i = lineEnd + 1;

                if (lineEnd > lineStart && s.charAt(lineEnd - 1) == '\r') {
                    lineEnd--;
                }

                int p = lineStart;
                while (p < lineEnd) {
                    char c = s.charAt(p);
                    if (c != ' ' && c != '\t') break;
                    p++;
                }
                if (p >= lineEnd) continue;

                char first = s.charAt(p);
                if (first == '#' || first == ';') continue;

                int eq = -1;
                for (int j = p; j < lineEnd; j++) {
                    if (s.charAt(j) == '=') {
                        eq = j;
                        break;
                    }
                }
                if (eq == -1) continue;

                int keyEnd = eq;
                while (keyEnd > p) {
                    char c = s.charAt(keyEnd - 1);
                    if (c != ' ' && c != '\t') break;
                    keyEnd--;
                }
                if (keyEnd <= p) continue;
                String key = s.substring(p, keyEnd);

                int vStart = eq + 1;
                while (vStart < lineEnd) {
                    char c = s.charAt(vStart);
                    if (c != ' ' && c != '\t') break;
                    vStart++;
                }

                if (vStart >= lineEnd) {
                    out.put(key, "");
                    continue;
                }

                int vEnd = lineEnd;
                String rawVal;
                if (s.charAt(vStart) == '"' && vEnd > vStart + 1 && s.charAt(vEnd - 1) == '"') {
                    rawVal = s.substring(vStart + 1, vEnd - 1);
                } else {
                    rawVal = s.substring(vStart, vEnd);
                }

                out.put(key, StringUtility.unescapeJava(rawVal));
            }

            return out;
        } catch (IOException ignored) {
            return Map.of();
        }
    }

    @Override
    public @NotNull Key name() {
        return Key.key("hypixel:translator");
    }

    private record LocaleSubsystem(Locale locale, String subsystem) {
        LocaleSubsystem {
            Objects.requireNonNull(locale, "locale");
            Objects.requireNonNull(subsystem, "subsystem");
        }
    }

    private record LocaleKey(Locale locale, String key) {
        LocaleKey {
            Objects.requireNonNull(locale, "locale");
            Objects.requireNonNull(key, "key");
        }
    }
}
