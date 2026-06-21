package net.swofty.type.generic.collectibles;

import net.minestom.server.item.Material;

import java.util.List;
import java.util.Map;

public record CollectibleDefinition(
    String id,
    CollectibleGamemode gamemode,
    CollectibleCategory category,
    String name,
    Material iconMaterial,
    String iconTexture,
    List<String> description,
    String categoryDescriptionKey,
    Map<String, String> customData,
    CollectibleRarity rarity,
    int sortIndex,
    String selectionValue,
    CollectibleUnlockRequirement unlockRequirement
) {
    public CollectibleDefinition {
        description = description == null ? List.of() : List.copyOf(description);
        customData = customData == null ? Map.of() : Map.copyOf(customData);
        rarity = rarity == null ? CollectibleRarity.COMMON : rarity;
        unlockRequirement = unlockRequirement == null ? CollectibleUnlockRequirement.free() : unlockRequirement;
    }

    public String effectiveSelectionValue() {
        if (selectionValue == null || selectionValue.isBlank()) {
            return id;
        }
        return selectionValue;
    }
}
