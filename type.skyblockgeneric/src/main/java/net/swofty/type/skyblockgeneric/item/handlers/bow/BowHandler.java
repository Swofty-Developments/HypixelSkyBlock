package net.swofty.type.skyblockgeneric.item.handlers.bow;

import lombok.Builder;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.function.BiConsumer;

@Builder
public record BowHandler(BiConsumer<SkyBlockPlayer, SkyBlockItem> shootHandler) {
}
