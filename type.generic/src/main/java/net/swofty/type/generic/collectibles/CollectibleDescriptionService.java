package net.swofty.type.generic.collectibles;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.translation.Argument;
import net.swofty.type.generic.i18n.I18n;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CollectibleDescriptionService {

    public static List<Component> resolveLore(CollectibleDefinition definition) {
        String categoryDescriptionKey = definition.categoryDescriptionKey();
        if (categoryDescriptionKey != null && !categoryDescriptionKey.isBlank()) {
            Component nameComponent = Component.text(definition.name());
            try {
                Component[] lines = I18n.iterable(categoryDescriptionKey, Argument.component("name", nameComponent));
                return Arrays.stream(lines)
                    .map(line -> line.colorIfAbsent(NamedTextColor.GRAY))
                    .toList();
            } catch (IllegalStateException exception) {
                if (!isMissingIterableKey(exception)) {
                    throw exception;
                }

                return List.of(
                    I18n.t(categoryDescriptionKey, Argument.component("name", nameComponent))
                        .colorIfAbsent(NamedTextColor.GRAY)
                );
            }
        }

        if (definition.description().isEmpty()) {
            return List.of();
        }

        return definition.description().stream()
            .map(line -> (Component) Component.text(line, NamedTextColor.GRAY))
            .toList();
    }

    private static boolean isMissingIterableKey(IllegalStateException exception) {
        String message = exception.getMessage();
        return message != null && message.startsWith("Missing dialogue translation key in en_US:");
    }
}
