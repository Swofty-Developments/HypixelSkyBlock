package net.swofty.type.skywarslobby.kit;

import lombok.Getter;
import net.minestom.server.item.Material;

/**
 * Rarity levels for SkyWars kits
 */
@Getter
public enum SkywarsKitRarity {
    COMMON("§a", "COMMON", Material.LIME_STAINED_GLASS_PANE, 1),
    RARE("§9", "RARE", Material.BLUE_STAINED_GLASS_PANE, 2),
    LEGENDARY("§6", "LEGENDARY", Material.ORANGE_STAINED_GLASS_PANE, 3),
    MYTHICAL("§d", "MYTHICAL", Material.PINK_STAINED_GLASS_PANE, 4);

    private final String colorCode;
    private final String displayName;
    private final Material glassPane;
    private final int sortOrder;

    SkywarsKitRarity(String colorCode, String displayName, Material glassPane, int sortOrder) {
        this.colorCode = colorCode;
        this.displayName = displayName;
        this.glassPane = glassPane;
        this.sortOrder = sortOrder;
    }

    /**
     * Get the formatted display string with color
     */
    public String getFormattedName() {
        return colorCode + displayName;
    }
}
