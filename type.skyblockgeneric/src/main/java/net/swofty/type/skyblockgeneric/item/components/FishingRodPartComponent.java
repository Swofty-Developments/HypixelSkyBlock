package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.type.skyblockgeneric.fishing.RodPartDefinition;

import java.util.Map;

@Getter
public class FishingRodPartComponent extends net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent {
    private final String displayName;
    private final RodPartDefinition.PartCategory category;
    private final int requiredFishingLevel;
    private final Map<String, Double> tagBonuses;
    private final boolean treasureOnly;
    private final boolean bayouTreasureToJunk;
    private final String materializedItemId;
    private final double materializedChance;
    private final double baitPreservationChance;
    private final double hotspotBuffMultiplier;
    private final String texture;

    public FishingRodPartComponent(
        String displayName,
        RodPartDefinition.PartCategory category,
        int requiredFishingLevel,
        Map<String, Double> tagBonuses,
        boolean treasureOnly,
        boolean bayouTreasureToJunk,
        String materializedItemId,
        double materializedChance,
        double baitPreservationChance,
        double hotspotBuffMultiplier,
        String texture
    ) {
        this.displayName = displayName;
        this.category = category;
        this.requiredFishingLevel = requiredFishingLevel;
        this.tagBonuses = Map.copyOf(tagBonuses);
        this.treasureOnly = treasureOnly;
        this.bayouTreasureToJunk = bayouTreasureToJunk;
        this.materializedItemId = materializedItemId;
        this.materializedChance = materializedChance;
        this.baitPreservationChance = baitPreservationChance;
        this.hotspotBuffMultiplier = hotspotBuffMultiplier;
        this.texture = texture;

        addInheritedComponent(new CustomDisplayNameComponent(ignored -> displayName));
        if (texture != null && !texture.isBlank()) {
            addInheritedComponent(new SkullHeadComponent(ignored -> texture));
        }
        addInheritedComponent(new ExtraRarityComponent("ROD PART"));
    }
}