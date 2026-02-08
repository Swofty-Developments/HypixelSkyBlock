package net.swofty.type.generic.language;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;

public final class LanguageMessage {
    public static final String CURRENT_LANGUAGE = "language.current";
    public static final String AVAILABLE_LANGUAGES = "language.available";
    public static final String USE_LANGUAGE_HINT = "language.hint.use_command";
    public static final String UNKNOWN_LANGUAGE = "language.error.unknown";
    public static final String LANGUAGE_UPDATED = "language.updated";

    private static final Map<PlayerLanguage, Map<String, String>> MESSAGES = new EnumMap<>(PlayerLanguage.class);

    static {
        Yaml yaml = new Yaml();
        for (PlayerLanguage language : PlayerLanguage.values()) {
            MESSAGES.put(language, loadMessages(yaml, language));
        }
    }

    private LanguageMessage() {
    }

    public static String formatByCode(String code, PlayerLanguage language, Object... args) {
        String template = resolveTemplate(code, language);
        return String.format(template, args);
    }

    private static String resolveTemplate(String code, PlayerLanguage language) {
        Map<String, String> localized = MESSAGES.get(language);
        if (localized != null && localized.containsKey(code)) {
            return localized.get(code);
        }

        Map<String, String> english = MESSAGES.get(PlayerLanguage.ENGLISH);
        if (english != null && english.containsKey(code)) {
            return english.get(code);
        }

        return code;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, String> loadMessages(Yaml yaml, PlayerLanguage language) {
        String resourcePath = "lang/messages_" + language.getId() + ".yml";

        try (InputStream input = LanguageMessage.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (input == null) {
                throw new IllegalStateException("Missing language resource: " + resourcePath);
            }

            Object loaded = yaml.load(input);
            if (!(loaded instanceof Map<?, ?> map)) {
                throw new IllegalStateException("Invalid YAML structure for: " + resourcePath);
            }

            return (Map<String, String>) map;
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to load language resource: " + resourcePath, exception);
        }
    }
}
