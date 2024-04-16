package net.swofty.types.generic.item.items.accessories;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BatTalisman implements Talisman {
    @Override
    public List<String> getTalismanDisplay() {
        return null;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "2f912e26-6ec8-33a2-96df-ba182c3ea100";
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withAdditive(ItemStatistic.HEALTH, 1D)
                .withAdditive(ItemStatistic.SPEED, 1D)
                .withAdditive(ItemStatistic.INTELLIGENCE, 1D)
                .build();
    }
}
