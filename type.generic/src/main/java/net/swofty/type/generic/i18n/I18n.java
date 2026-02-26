package net.swofty.type.generic.i18n;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;

import java.util.Locale;

public class I18n {

    private static final LegacyComponentSerializer LEGACY =
            LegacyComponentSerializer.legacySection();

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
}
