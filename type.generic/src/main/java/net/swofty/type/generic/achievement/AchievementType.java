package net.swofty.type.generic.achievement;

import lombok.Getter;
import net.minestom.server.item.Material;

@Getter
public enum AchievementType {
    CHALLENGE("Challenge", "One-time achievements", Material.DIAMOND),
    TIERED("Tiered", "Multi-tier progressive achievements", Material.DIAMOND_BLOCK),
    SEASONAL("Seasonal", "Event-specific achievements", Material.MAGMA_CREAM);

    private final String displayName;
    private final String description;
    private final Material material;

    AchievementType(String displayName, String description, Material material) {
        this.displayName = displayName;
        this.description = description;
        this.material = material;
    }
}
