package net.swofty.type.generic.language;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.Translator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.Locale;

public final class LanguageAdventureTranslator implements Translator {
    private static final Key NAME = Key.key("hypixel", "language");

    @Override
    public @NotNull Key name() {
        return NAME;
    }

    @Override
    public @Nullable MessageFormat translate(@NotNull String key, @NotNull Locale locale) {
        return new MessageFormat(LanguageMessage.resolveTemplate(key, locale), locale);
    }
}
