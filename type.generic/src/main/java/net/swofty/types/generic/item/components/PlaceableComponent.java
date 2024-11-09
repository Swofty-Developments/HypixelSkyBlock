package net.swofty.types.generic.item.components;

import lombok.Getter;
import net.swofty.types.generic.block.BlockType;
import net.swofty.types.generic.item.SkyBlockItemComponent;
import org.jetbrains.annotations.Nullable;

public class PlaceableComponent extends SkyBlockItemComponent {
    @Getter
    private final @Nullable BlockType blockType;

    public PlaceableComponent(@Nullable String blockType) {
        this.blockType = blockType != null ? BlockType.valueOf(blockType) : null;
    }

    public boolean isPlaceable() {
        return true;
    }
}