package net.swofty.type.skyblockgeneric.fishing;

import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.FishingRodPartComponent;
import org.jetbrains.annotations.Nullable;

public final class FishingRodPartService {
    private FishingRodPartService() {
    }

    public static @Nullable FishingRodPartComponent getHook(SkyBlockItem rod) {
        return FishingItemSupport.getRodPart(rod.getAttributeHandler().getFishingHook());
    }

    public static @Nullable FishingRodPartComponent getLine(SkyBlockItem rod) {
        return FishingItemSupport.getRodPart(rod.getAttributeHandler().getFishingLine());
    }

    public static @Nullable FishingRodPartComponent getSinker(SkyBlockItem rod) {
        return FishingItemSupport.getRodPart(rod.getAttributeHandler().getFishingSinker());
    }

    public static ItemStatistics getStatistics(SkyBlockItem rod) {
        ItemStatistics.Builder builder = ItemStatistics.builder();
        append(builder, getHook(rod));
        append(builder, getLine(rod));
        append(builder, getSinker(rod));
        return builder.build();
    }

    public static void applyPart(SkyBlockItem rod, FishingRodPartComponent part) {
        switch (part.getCategory()) {
            case HOOK -> rod.getAttributeHandler().setFishingHook(part.getItemId());
            case LINE -> rod.getAttributeHandler().setFishingLine(part.getItemId());
            case SINKER -> rod.getAttributeHandler().setFishingSinker(part.getItemId());
        }
    }

    public static void removePart(SkyBlockItem rod, FishingPartCategory category) {
        switch (category) {
            case HOOK -> rod.getAttributeHandler().setFishingHook(null);
            case LINE -> rod.getAttributeHandler().setFishingLine(null);
            case SINKER -> rod.getAttributeHandler().setFishingSinker(null);
        }
    }

    private static void append(ItemStatistics.Builder builder, @Nullable FishingRodPartComponent definition) {
        if (definition == null) {
            return;
        }
        SkyBlockItem item = FishingItemSupport.getItem(definition.getItemId());
        if (item == null) {
            return;
        }
        for (var statistic : net.swofty.commons.skyblock.statistics.ItemStatistic.values()) {
            double amount = item.getAttributeHandler().getStatistics().getOverall(statistic);
            if (amount != 0) {
                builder.withBase(statistic, amount);
            }
        }
    }
}
