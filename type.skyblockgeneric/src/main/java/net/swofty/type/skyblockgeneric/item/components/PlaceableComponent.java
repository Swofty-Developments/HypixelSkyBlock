package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.type.generic.block.BlockType;
import net.swofty.type.generic.item.SkyBlockItemComponent;
import org.jetbrains.annotations.Nullable;

public class PlaceableComponent extends SkyBlockItemComponent {
    @Getter
    private final @Nullable BlockType blockType;

    public PlaceableComponent(@Nullable String blockType) {
        this.blockType = blockType != null ? BlockType.getFromName(blockType) : null;
    }

    public boolean isPlaceable() {
        return true;
    }
}