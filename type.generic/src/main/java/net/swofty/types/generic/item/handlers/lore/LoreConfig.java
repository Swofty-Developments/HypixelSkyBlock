package net.swofty.types.generic.item.handlers.lore;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiFunction;

public record LoreConfig(
        @Nullable
        BiFunction<SkyBlockItem, SkyBlockPlayer, List<String>> loreGenerator,
        @Nullable
        BiFunction<SkyBlockItem, SkyBlockPlayer, String> displayNameGenerator
) { }
