package net.swofty.types.generic.item.items.accessories.speed;

import net.swofty.commons.statistics.ItemStatistic;
import net.swofty.commons.statistics.ItemStatistics;
import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.ConstantStatistics;
import net.swofty.types.generic.item.impl.TieredTalisman;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SpeedTalisman implements TieredTalisman, ConstantStatistics {
    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "8624bacb5f1986e6477abce4ae7dca1820a5260b6233b55ba1d9ba936c84b";
    }

    @Override
    public List<String> getTalismanDisplay() {
        return new ArrayList<>();
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.SPEED, 1D)
                .build();
    }

    @Override
    public ItemTypeLinker getBaseTalismanTier() {
        return ItemTypeLinker.SPEED_TALISMAN;
    }

    @Override
    public Integer getTier() {
        return 1;
    }
}
