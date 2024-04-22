package net.swofty.types.generic.item.items.accessories;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.ConstantStatistics;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpeedRing implements Talisman, ConstantStatistics {
    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "c2da40a91f8fa7e1cbdd934da92a7668dc95d75b57c9c80a381c5e178cee6ba7";
    }

    @Override
    public List<String> getTalismanDisplay() {
        return List.of();
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withAdditive(ItemStatistic.SPEED, 3D)
                .build();
    }
}