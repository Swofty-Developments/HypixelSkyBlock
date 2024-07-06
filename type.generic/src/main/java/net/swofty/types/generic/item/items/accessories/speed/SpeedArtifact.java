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

public class SpeedArtifact implements TieredTalisman, ConstantStatistics, SkullHead {
    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "f06706eecb2d558ace27abda0b0b7b801d36d17dd7a890a9520dbe522374f8a6";
    }

    @Override
    public List<String> getTalismanDisplay() {
        return List.of("§7");
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.SPEED, 5D)
                .build();
    }

    @Override
    public ItemTypeLinker getBaseTalismanTier() {
        return ItemTypeLinker.SPEED_TALISMAN;
    }

    @Override
    public Integer getTier() {
        return 3;
    }
}
