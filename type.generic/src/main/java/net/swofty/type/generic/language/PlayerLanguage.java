package net.swofty.type.generic.language;

import java.util.Arrays;
import java.util.Locale;

public enum PlayerLanguage {
    ENGLISH("en", "English", Locale.ENGLISH),
    JAPANESE("ja", "日本語", Locale.JAPANESE),
    KOREAN("ko", "한국어", Locale.KOREAN),
    CHINESE_SIMPLIFIED("zh_cn", "简体中文", Locale.SIMPLIFIED_CHINESE);

    private final String id;
    private final String displayName;
    private final Locale locale;

    PlayerLanguage(String id, String displayName, Locale locale) {
        this.id = id;
        this.displayName = displayName;
        this.locale = locale;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Locale getLocale() {
        return locale;
    }

    public static PlayerLanguage fromInput(String input) {
        if (input == null || input.isBlank()) {
            return null;
        }

        String normalized = input.trim().toLowerCase(Locale.ROOT).replace('-', '_');
        return Arrays.stream(values())
                .filter(language -> language.id.equals(normalized)
                        || language.name().toLowerCase(Locale.ROOT).equals(normalized)
                        || language.locale.toLanguageTag().toLowerCase(Locale.ROOT).replace('-', '_').equals(normalized))
                .findFirst()
                .orElse(null);
    }

    public static String allLanguageIds() {
        return Arrays.stream(values())
                .map(PlayerLanguage::getId)
                .reduce((left, right) -> left + ", " + right)
                .orElse("en");
    }
}
