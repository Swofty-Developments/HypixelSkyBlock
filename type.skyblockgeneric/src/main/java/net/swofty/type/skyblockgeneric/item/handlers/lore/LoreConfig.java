package net.swofty.type.skyblockgeneric.item.handlers.lore;

import net.swofty.type.generic.item.SkyBlockItem;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiFunction;

public record LoreConfig(
        @Nullable
        BiFunction<SkyBlockItem, HypixelPlayer, List<String>> loreGenerator,
        @Nullable
        BiFunction<SkyBlockItem, HypixelPlayer, String> displayNameGenerator
) { }
