package net.swofty.type.skyblockgeneric.hunting;

import net.swofty.commons.skyblock.item.ItemType;

import java.time.Duration;

public enum HuntrapTier {
    RETIA_BASICA(ItemType.RETIA_BASICA, "Small Huntrap", Duration.ofHours(24)),
    RETIA_MELIORA(ItemType.RETIA_MELIORA, "Medium Huntrap", Duration.ofHours(22)),
    RETIA_ROBUSTA(ItemType.RETIA_ROBUSTA, "Large Huntrap", Duration.ofHours(19)),
    RETIA_FORTA(ItemType.RETIA_FORTA, "Greater Huntrap", Duration.ofHours(16)),
    RETIA_SUPREMA(ItemType.RETIA_SUPREMA, "Astral Huntrap", Duration.ofHours(12));

    private final ItemType itemType;
    private final String displayName;
    private final Duration duration;

    HuntrapTier(ItemType itemType, String displayName, Duration duration) {
        this.itemType = itemType;
        this.displayName = displayName;
        this.duration = duration;
    }

    public ItemType itemType() {
        return itemType;
    }

    public String displayName() {
        return displayName;
    }

    public long durationMillis() {
        return duration.toMillis();
    }
}
