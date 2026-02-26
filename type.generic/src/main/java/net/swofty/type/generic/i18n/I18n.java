package net.swofty.type.generic.i18n;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;

import java.util.Locale;
import java.util.Map;

public class I18n {

    private static final LegacyComponentSerializer LEGACY =
            LegacyComponentSerializer.legacySection();

    private static final String DIALOGUE_SEPARATOR = "\\|";

    public static TranslatableComponent t(String key) {
        return Component.translatable(key);
    }

    public static TranslatableComponent t(String key, Component... args) {
        return Component.translatable(key, args);
    }

    public static String string(String key, Locale locale) {
        Component rendered = GlobalTranslator.render(Component.translatable(key), locale);
        return LEGACY.serialize(rendered);
    }

    public static String string(String key) {
        return string(key, HypixelTranslator.defaultLocale);
    }

    public static String string(String key, Map<String, String> placeholders) {
        String result = string(key);
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return result;
    }

    public static String string(String key, Locale locale, Map<String, String> placeholders) {
        String result = string(key, locale);
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return result;
    }

    public static String[] dialogueLines(String key) {
        return dialogueLines(key, HypixelTranslator.defaultLocale);
    }

    public static String[] dialogueLines(String key, Locale locale) {
        String resolved = string(key, locale);
        return resolved.split(DIALOGUE_SEPARATOR);
    }

    public static String[] dialogueLines(String key, Map<String, String> placeholders) {
        String resolved = string(key, placeholders);
        return resolved.split(DIALOGUE_SEPARATOR);
    }
}
