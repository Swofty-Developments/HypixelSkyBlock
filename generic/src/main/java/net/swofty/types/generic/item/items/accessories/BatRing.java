package net.swofty.types.generic.item.items.accessories;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BatRing implements Talisman {
    @Override
    public List<String> getTalismanDisplay() {
        return null;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "9e99deef919db66ac2bd28d6302756ccd57c7f8b12b9dca8f41c3e0a04ac1cc";
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withAdditive(ItemStatistic.HEALTH, 3D)
                .withAdditive(ItemStatistic.SPEED, 2D)
                .withAdditive(ItemStatistic.INTELLIGENCE, 2D)
                .build();
    }
}
