package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.commons.skyblock.item.reforge.ReforgeType;
import net.swofty.type.skyblockgeneric.fishing.FishingMedium;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;
import net.swofty.type.skyblockgeneric.utility.groups.EnchantItemGroups;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Getter
public class FishingRodMetadataComponent extends SkyBlockItemComponent {
    private final FishingMedium medium;
    private final int requiredFishingLevel;
    private final boolean rodPartsEnabled;
    private final @Nullable String legacyConversionTarget;
    private final @Nullable String subtitle;
    private final List<String> extraRequirements;

    public FishingRodMetadataComponent(
        FishingMedium medium,
        int requiredFishingLevel,
        boolean rodPartsEnabled,
        @Nullable String legacyConversionTarget,
        @Nullable String subtitle,
        List<String> extraRequirements
    ) {
        this.medium = medium;
        this.requiredFishingLevel = requiredFishingLevel;
        this.rodPartsEnabled = rodPartsEnabled;
        this.legacyConversionTarget = legacyConversionTarget;
        this.subtitle = subtitle;
        this.extraRequirements = List.copyOf(extraRequirements);
        addInheritedComponent(new FishingRodComponent());
        addInheritedComponent(new EnchantableComponent(List.of(EnchantItemGroups.FISHING_ROD), true));
        addInheritedComponent(new ReforgableComponent(ReforgeType.FISHING_RODS));
        addInheritedComponent(new DefaultSoulboundComponent(true));
        addInheritedComponent(new ExtraRarityComponent("FISHING ROD"));
        addInheritedComponent(new LoreUpdateComponent("FISHING_ROD", true));
    }
}
