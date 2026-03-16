package net.swofty.type.skyblockgeneric.fishing;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public record ShipPartDefinition(
    String itemId,
    String displayName,
    ShipPartSlot slot,
    List<String> lore,
    @Nullable String texture
) {
    public enum ShipPartSlot {
        HELM,
        ENGINE,
        HULL
    }
}
