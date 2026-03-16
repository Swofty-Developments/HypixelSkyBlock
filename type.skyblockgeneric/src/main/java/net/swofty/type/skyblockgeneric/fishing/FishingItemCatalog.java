package net.swofty.type.skyblockgeneric.fishing;

import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.skyblockgeneric.item.ConfigurableSkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.FishingBaitComponent;
import net.swofty.type.skyblockgeneric.item.components.FishingRodMetadataComponent;
import net.swofty.type.skyblockgeneric.item.components.FishingRodPartComponent;
import net.swofty.type.skyblockgeneric.item.components.FishingShipPartComponent;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public final class FishingItemCatalog {
    private FishingItemCatalog() {
    }

    public static @Nullable FishingRodDefinition getRod(@Nullable String itemId) {
        if (itemId == null) {
            return null;
        }

        ConfigurableSkyBlockItem item = ConfigurableSkyBlockItem.getFromID(itemId);
        if (item == null || !item.hasComponent(FishingRodMetadataComponent.class)) {
            return null;
        }

        FishingRodMetadataComponent component = item.getComponent(FishingRodMetadataComponent.class);
        return new FishingRodDefinition(
            itemId,
            component.getDisplayName(),
            component.getSubtitle(),
            component.getMedium(),
            component.getRequiredFishingLevel(),
            item.getDefaultStatistics(),
            component.getEnchantments(),
            item.getLore(),
            component.getLegacyConversionTarget(),
            component.getLegacyConversionPart(),
            component.isRodPartsEnabled()
        );
    }

    public static @Nullable RodPartDefinition getRodPart(@Nullable String itemId) {
        if (itemId == null) {
            return null;
        }

        ConfigurableSkyBlockItem item = ConfigurableSkyBlockItem.getFromID(itemId);
        if (item == null || !item.hasComponent(FishingRodPartComponent.class)) {
            return null;
        }

        FishingRodPartComponent component = item.getComponent(FishingRodPartComponent.class);
        return new RodPartDefinition(
            itemId,
            component.getDisplayName(),
            component.getCategory(),
            component.getRequiredFishingLevel(),
            item.getDefaultStatistics(),
            item.getLore(),
            component.getTagBonuses(),
            component.isTreasureOnly(),
            component.isBayouTreasureToJunk(),
            component.getMaterializedItemId(),
            component.getMaterializedChance(),
            component.getBaitPreservationChance(),
            component.getHotspotBuffMultiplier(),
            component.getTexture()
        );
    }

    public static @Nullable BaitDefinition getBait(@Nullable String itemId) {
        if (itemId == null) {
            return null;
        }

        ConfigurableSkyBlockItem item = ConfigurableSkyBlockItem.getFromID(itemId);
        if (item == null || !item.hasComponent(FishingBaitComponent.class)) {
            return null;
        }

        FishingBaitComponent component = item.getComponent(FishingBaitComponent.class);
        return new BaitDefinition(
            itemId,
            component.getDisplayName(),
            item.getDefaultStatistics(),
            item.getLore(),
            component.getTagBonuses(),
            component.getTreasureChanceBonus(),
            component.getTreasureQualityBonus(),
            component.getTrophyFishChanceBonus(),
            component.getDoubleHookChanceBonus(),
            component.getMediums(),
            component.getTexture()
        );
    }

    public static @Nullable ShipPartDefinition getShipPart(@Nullable String itemId) {
        if (itemId == null) {
            return null;
        }

        ConfigurableSkyBlockItem item = ConfigurableSkyBlockItem.getFromID(itemId);
        if (item == null || !item.hasComponent(FishingShipPartComponent.class)) {
            return null;
        }

        FishingShipPartComponent component = item.getComponent(FishingShipPartComponent.class);
        return new ShipPartDefinition(
            itemId,
            component.getDisplayName(),
            component.getSlot(),
            item.getLore(),
            component.getTexture()
        );
    }

    public static List<FishingRodDefinition> getRods() {
        return Arrays.stream(ItemType.values())
            .map(ItemType::name)
            .map(FishingItemCatalog::getRod)
            .filter(Objects::nonNull)
            .sorted(Comparator.comparingInt(FishingRodDefinition::requiredFishingLevel)
                .thenComparing(FishingRodDefinition::displayName))
            .toList();
    }

    public static List<RodPartDefinition> getRodParts() {
        return Arrays.stream(ItemType.values())
            .map(ItemType::name)
            .map(FishingItemCatalog::getRodPart)
            .filter(Objects::nonNull)
            .sorted(Comparator.comparing(RodPartDefinition::category)
                .thenComparingInt(RodPartDefinition::requiredFishingLevel)
                .thenComparing(RodPartDefinition::displayName))
            .toList();
    }

    public static List<BaitDefinition> getBaits() {
        return Arrays.stream(ItemType.values())
            .map(ItemType::name)
            .map(FishingItemCatalog::getBait)
            .filter(Objects::nonNull)
            .sorted(Comparator.comparing((BaitDefinition bait) -> ItemType.valueOf(bait.itemId()).rarity.ordinal())
                .thenComparing(BaitDefinition::displayName))
            .toList();
    }

    public static List<ShipPartDefinition> getShipParts() {
        return Arrays.stream(ItemType.values())
            .map(ItemType::name)
            .map(FishingItemCatalog::getShipPart)
            .filter(Objects::nonNull)
            .sorted(Comparator.comparing(ShipPartDefinition::slot).thenComparing(ShipPartDefinition::displayName))
            .toList();
    }
}