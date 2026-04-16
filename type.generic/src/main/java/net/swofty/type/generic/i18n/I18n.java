package net.swofty.type.generic.i18n;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class I18n {

    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacySection();
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

    public static TranslatableComponent t(String key, ComponentLike... args) {
        requireKey(key);
        return Component.translatable(key, args);
    }

    /**
     * Fetches a translation with a key and a locale
     *
     * @param key    string representing the locale key
     * @param locale locale for player
     * @return the translation for the locale
     * @apiNote <h1>Please do your best to trying not to use this.</h1>
     */
    public static String string(String key, Locale locale) {
        requireKey(key);
        Component rendered = GlobalTranslator.render(Component.translatable(key), locale);
        return LEGACY.serialize(rendered);
    }

    /**
     * Fetches a translation with a key and a locale with optional Components
     *
     * @param key    string representing the locale key
     * @param locale locale for player
     * @param args   arguments
     * @return the translation for the locale
     * @apiNote <h1>Please do your best to trying not to use this.</h1>
     */
    public static String string(String key, Locale locale, Component... args) {
        requireKey(key);
        Component rendered = GlobalTranslator.render(Component.translatable(key, args), locale);
        return LEGACY.serialize(rendered);
    }

    @Deprecated
    public static String string(String key, Component... args) {
        return string(key, HypixelTranslator.defaultLocale, args);
    }

    public static Component[] iterable(String key) {
        return iterable(key, new Component[0]);
    }

    public static Component[] iterable(String key, ComponentLike... args) {
        if (translator == null) {
            throw new IllegalStateException("Translator not initialized");
        }

        List<Component> lines = new ArrayList<>();
        int index = 1;
        while (true) {
            String numberedKey = key + "." + index;
            if (!translator.hasKey(numberedKey)) {
                break;
            }

            lines.add(args.length == 0
                ? Component.translatable(numberedKey)
                : Component.translatable(numberedKey, args));
            index++;
        }

        if (lines.isEmpty()) {
            throw new IllegalStateException("Missing dialogue translation key in en_US: " + key + ".1");
        }

        return lines.toArray(new Component[0]);
    }

}
