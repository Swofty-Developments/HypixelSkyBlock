package net.swofty.type.generic.language;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

public enum LanguageMessage {
    CURRENT_LANGUAGE("language.current"),
    AVAILABLE_LANGUAGES("language.available"),
    USE_LANGUAGE_HINT("language.hint.use_command"),
    UNKNOWN_LANGUAGE("language.error.unknown"),
    LANGUAGE_UPDATED("language.updated");

    private static final Map<PlayerLanguage, Map<String, String>> MESSAGES = new EnumMap<>(PlayerLanguage.class);
    private static final LanguageAdventureTranslator ADVENTURE_TRANSLATOR = new LanguageAdventureTranslator();

    static {
        Yaml yaml = new Yaml();
        for (PlayerLanguage language : PlayerLanguage.values()) {
            MESSAGES.put(language, loadMessages(yaml, language));
        }
    }

    private final String key;

    LanguageMessage(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }

    public String format(PlayerLanguage language, Object... args) {
        return String.format(resolveTemplate(key, language), args);
    }

    public MessageFormat toMessageFormat(PlayerLanguage language) {
        return new MessageFormat(resolveTemplate(key, language), language.getLocale());
    }

    static String resolveTemplate(String key, Locale locale) {
        PlayerLanguage language = PlayerLanguage.fromInput(locale.toLanguageTag());
        if (language == null) {
            language = PlayerLanguage.ENGLISH;
        }
        return resolveTemplate(key, language);
    }

    private static String resolveTemplate(String key, PlayerLanguage language) {
        Map<String, String> localized = MESSAGES.get(language);
        if (localized != null && localized.containsKey(key)) {
            return localized.get(key);
        }

        Map<String, String> english = MESSAGES.get(PlayerLanguage.ENGLISH);
        if (english != null && english.containsKey(key)) {
            return english.get(key);
        }

        return key;
    }

    public static LanguageAdventureTranslator adventureTranslator() {
        return ADVENTURE_TRANSLATOR;
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
