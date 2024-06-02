package net.swofty.types.generic.item.impl;

import net.swofty.types.generic.block.BlockType;
import org.jetbrains.annotations.Nullable;

public interface PlaceableCustomSkyBlockItem extends CustomSkyBlockItem {
    @Override
    default boolean isPlaceable() {
        return true;
    }

    @Nullable BlockType getAssociatedBlockType();
}
