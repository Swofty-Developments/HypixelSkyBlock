package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.commons.skyblock.item.reforge.ReforgeType;
import net.swofty.type.skyblockgeneric.enchantment.EnchantmentType;
import net.swofty.type.skyblockgeneric.fishing.FishingMedium;
import net.swofty.type.skyblockgeneric.utility.groups.EnchantItemGroups;

import java.util.List;
import java.util.Map;

@Getter
public class FishingRodMetadataComponent extends net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent {
    private final String displayName;
    private final String subtitle;
    private final FishingMedium medium;
    private final int requiredFishingLevel;
    private final Map<EnchantmentType, Integer> enchantments;
    private final String legacyConversionTarget;
    private final String legacyConversionPart;
    private final boolean rodPartsEnabled;

    public FishingRodMetadataComponent(
        String displayName,
        String subtitle,
        FishingMedium medium,
        int requiredFishingLevel,
        Map<EnchantmentType, Integer> enchantments,
        String legacyConversionTarget,
        String legacyConversionPart,
        boolean rodPartsEnabled
    ) {
        this.displayName = displayName;
        this.subtitle = subtitle;
        this.medium = medium;
        this.requiredFishingLevel = requiredFishingLevel;
        this.enchantments = Map.copyOf(enchantments);
        this.legacyConversionTarget = legacyConversionTarget;
        this.legacyConversionPart = legacyConversionPart;
        this.rodPartsEnabled = rodPartsEnabled;

        addInheritedComponent(new CustomDisplayNameComponent(ignored -> displayName));
        if (subtitle != null && !subtitle.isBlank()) {
            addInheritedComponent(new ExtraUnderNameComponent(subtitle));
        }
        addInheritedComponent(new FishingRodComponent());
        addInheritedComponent(new EnchantableComponent(List.of(EnchantItemGroups.FISHING_ROD), true));
        addInheritedComponent(new ReforgableComponent(ReforgeType.FISHING_RODS));
        addInheritedComponent(new DefaultSoulboundComponent(true));
        addInheritedComponent(new ExtraRarityComponent("FISHING ROD"));
    }
}