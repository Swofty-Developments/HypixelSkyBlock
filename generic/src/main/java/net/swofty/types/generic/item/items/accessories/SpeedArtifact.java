package net.swofty.types.generic.item.items.accessories;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.ConstantStatistics;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpeedArtifact implements Talisman, ConstantStatistics {
    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "f06706eecb2d558ace27abda0b0b7b801d36d17dd7a890a9520dbe522374f8a6";
    }

    @Override
    public List<String> getTalismanDisplay() {
        return List.of();
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withAdditive(ItemStatistic.SPEED, 5D)
                .build();
    }
}