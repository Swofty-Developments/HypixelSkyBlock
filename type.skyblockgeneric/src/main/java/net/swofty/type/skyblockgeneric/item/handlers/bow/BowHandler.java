package net.swofty.type.skyblockgeneric.item.handlers.bow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.swofty.type.generic.item.SkyBlockItem;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.function.BiConsumer;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class BowHandler {
    private final BiConsumer<HypixelPlayer, SkyBlockItem> shootHandler;
}
