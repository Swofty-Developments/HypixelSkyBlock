package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;

/**
 * Component for potion items that defines their default effect data.
 * This is used in YAML configuration to define pre-made potions.
 */
@Getter
public class PotionDataComponent extends SkyBlockItemComponent {
    private final String effectType;
    private final int level;
    private final int baseDurationSeconds;
    private final boolean isSplash;
    private final boolean isExtended;

    public PotionDataComponent(String effectType, int level, int baseDurationSeconds,
                               boolean isSplash, boolean isExtended) {
        this.effectType = effectType;
        this.level = level;
        this.baseDurationSeconds = baseDurationSeconds;
        this.isSplash = isSplash;
        this.isExtended = isExtended;

        // Add display info
        String prefix = isSplash ? "Splash " : "";
        addInheritedComponent(new ExtraUnderNameComponent(prefix + "Potion"));
    }

    public PotionDataComponent(String effectType, int level, int baseDurationSeconds) {
        this(effectType, level, baseDurationSeconds, false, false);
    }
}
