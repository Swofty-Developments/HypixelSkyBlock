package net.swofty.types.generic.item.items.accessories.speed;

import net.swofty.commons.statistics.ItemStatistic;
import net.swofty.commons.statistics.ItemStatistics;
import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.ConstantStatistics;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.item.impl.TieredTalisman;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpeedRing implements TieredTalisman, ConstantStatistics, SkullHead {
    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "c2da40a91f8fa7e1cbdd934da92a7668dc95d75b57c9c80a381c5e178cee6ba7";
    }

    @Override
    public List<String> getTalismanDisplay() {
        return List.of("§7");
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.SPEED, 3D)
                .build();
    }

    @Override
    public ItemTypeLinker getBaseTalismanTier() {
        return ItemTypeLinker.SPEED_TALISMAN;
    }

    @Override
    public Integer getTier() {
        return 2;
    }
}
