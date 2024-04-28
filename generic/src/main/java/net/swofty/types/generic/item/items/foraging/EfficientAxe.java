package net.swofty.types.generic.item.items.foraging;

import net.swofty.types.generic.item.ReforgeType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.Reforgable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.Arrays;

public class EfficientAxe implements CustomSkyBlockItem, Reforgable, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.DAMAGE, 25D)
                .build();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "Drops 5 Planks from chopping",
                "Logs."
        ));
    }

    @Override
    public ReforgeType getReforgeType() {
        return ReforgeType.AXES;
    }
}
