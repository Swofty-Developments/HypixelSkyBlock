package net.swofty.type.skyblockgeneric.fishing;

import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import org.jetbrains.annotations.Nullable;

public final class FishingRodPartService {
    private FishingRodPartService() {
    }

    public static @Nullable RodPartDefinition getHook(SkyBlockItem rod) {
        return getPart(FishingItemCatalog.getRodPart(rod.getAttributeHandler().getFishingHook()));
    }

    public static @Nullable RodPartDefinition getLine(SkyBlockItem rod) {
        return getPart(FishingItemCatalog.getRodPart(rod.getAttributeHandler().getFishingLine()));
    }

    public static @Nullable RodPartDefinition getSinker(SkyBlockItem rod) {
        return getPart(FishingItemCatalog.getRodPart(rod.getAttributeHandler().getFishingSinker()));
    }

    public static ItemStatistics getStatistics(SkyBlockItem rod) {
        ItemStatistics.Builder builder = ItemStatistics.builder();
        append(builder, getHook(rod));
        append(builder, getLine(rod));
        append(builder, getSinker(rod));
        return builder.build();
    }

    public static void applyPart(SkyBlockItem rod, RodPartDefinition part) {
        switch (part.category()) {
            case HOOK -> rod.getAttributeHandler().setFishingHook(part.itemId());
            case LINE -> rod.getAttributeHandler().setFishingLine(part.itemId());
            case SINKER -> rod.getAttributeHandler().setFishingSinker(part.itemId());
        }
    }

    public static void removePart(SkyBlockItem rod, RodPartDefinition.PartCategory category) {
        switch (category) {
            case HOOK -> rod.getAttributeHandler().setFishingHook(null);
            case LINE -> rod.getAttributeHandler().setFishingLine(null);
            case SINKER -> rod.getAttributeHandler().setFishingSinker(null);
        }
    }

    private static @Nullable RodPartDefinition getPart(@Nullable RodPartDefinition definition) {
        return definition;
    }

    private static void append(ItemStatistics.Builder builder, @Nullable RodPartDefinition definition) {
        if (definition == null) {
            return;
        }
        for (var statistic : net.swofty.commons.skyblock.statistics.ItemStatistic.values()) {
            double amount = definition.statistics().getOverall(statistic);
            if (amount != 0) {
                builder.withBase(statistic, amount);
            }
        }
    }
}
