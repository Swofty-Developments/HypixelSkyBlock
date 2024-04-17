package net.swofty.types.generic.item.impl;

import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.swofty.types.generic.block.BlockType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public interface PlaceableCustomSkyBlockItem extends CustomSkyBlockItem {
    @Override
    default boolean isPlaceable() {
        return true;
    }

    @Nullable BlockType getAssociatedBlockType();
}
