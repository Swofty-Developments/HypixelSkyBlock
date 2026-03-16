package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.type.skyblockgeneric.fishing.FishingMedium;

import java.util.List;
import java.util.Map;

@Getter
public class FishingBaitComponent extends net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent {
    private final String displayName;
    private final Map<String, Double> tagBonuses;
    private final double treasureChanceBonus;
    private final double treasureQualityBonus;
    private final double trophyFishChanceBonus;
    private final double doubleHookChanceBonus;
    private final List<FishingMedium> mediums;
    private final String texture;

    public FishingBaitComponent(
        String displayName,
        Map<String, Double> tagBonuses,
        double treasureChanceBonus,
        double treasureQualityBonus,
        double trophyFishChanceBonus,
        double doubleHookChanceBonus,
        List<FishingMedium> mediums,
        String texture
    ) {
        this.displayName = displayName;
        this.tagBonuses = Map.copyOf(tagBonuses);
        this.treasureChanceBonus = treasureChanceBonus;
        this.treasureQualityBonus = treasureQualityBonus;
        this.trophyFishChanceBonus = trophyFishChanceBonus;
        this.doubleHookChanceBonus = doubleHookChanceBonus;
        this.mediums = List.copyOf(mediums);
        this.texture = texture;

        addInheritedComponent(new CustomDisplayNameComponent(ignored -> displayName));
        if (texture != null && !texture.isBlank()) {
            addInheritedComponent(new SkullHeadComponent(ignored -> texture));
        }
        addInheritedComponent(new ExtraUnderNameComponent(List.of("Fishing Bait", "Consumes on Cast")));
        addInheritedComponent(new ExtraRarityComponent("BAIT"));
    }
}