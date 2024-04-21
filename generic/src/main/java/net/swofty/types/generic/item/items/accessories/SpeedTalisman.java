package net.swofty.types.generic.item.items.accessories;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.ConstantStatistics;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpeedTalisman implements Talisman, ConstantStatistics {
    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "8624bacb5f1986e6477abce4ae7dca1820a5260b6233b55ba1d9ba936c84b";
    }

    @Override
    public List<String> getTalismanDisplay() {
        return List.of();
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withAdditive(ItemStatistic.SPEED, 1D)
                .build();
    }
}
