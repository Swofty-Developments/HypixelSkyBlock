package net.swofty.type.generic.i18n;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;

import java.util.List;
import java.util.Locale;

public class I18n {

    private static final LegacyComponentSerializer LEGACY =
            LegacyComponentSerializer.legacySection();
    private static final String DIALOGUE_SEPARATOR = "\\|";

    private static HypixelTranslator translator;

    public static void init(HypixelTranslator instance) {
        translator = instance;
    }

    public static void requireKey(String key) {
        if (translator != null && !translator.hasKey(key)) {
            throw new IllegalStateException("Missing translation key in en_US: " + key);
        }
    }

    public static TranslatableComponent t(String key) {
        requireKey(key);
        return Component.translatable(key);
    }

    public static TranslatableComponent t(String key, Component... args) {
        requireKey(key);
        return Component.translatable(key, args);
    }

    public static Component legacy(String text) {
        return LEGACY.deserialize(text);
    }

    public static String string(String key, Locale locale) {
        requireKey(key);
        Component rendered = GlobalTranslator.render(Component.translatable(key), locale);
        return LEGACY.serialize(rendered);
    }

    public static String string(String key, Locale locale, Component... args) {
        requireKey(key);
        Component rendered = GlobalTranslator.render(Component.translatable(key, args), locale);
        return LEGACY.serialize(rendered);
    }

    public static String string(String key) {
        return string(key, HypixelTranslator.defaultLocale);
    }

    public static String string(String key, Component... args) {
        return string(key, HypixelTranslator.defaultLocale, args);
    }

    public static List<String> lore(String key) {
        return List.of(string(key).split("\n"));
    }

    public static List<String> lore(String key, Locale locale) {
        return List.of(string(key, locale).split("\n"));
    }

    public static String[] dialogueLines(String key) {
        return dialogueLines(key, HypixelTranslator.defaultLocale);
    }

    public static String[] dialogueLines(String key, Locale locale) {
        String resolved = string(key, locale);
        return resolved.split(DIALOGUE_SEPARATOR);
    }

}
