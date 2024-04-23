package net.swofty.types.generic.item.items.accessories;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BatArtifact implements Talisman {
    @Override
    public List<String> getTalismanDisplay() {
        return List.of();
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "382fc3f71b41769376a9e92fe3adbaac3772b999b219c9d6b4680ba9983e527";
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withAdditive(ItemStatistic.HEALTH, 5D)
                .withAdditive(ItemStatistic.SPEED, 3D)
                .withAdditive(ItemStatistic.INTELLIGENCE, 3D)
                .build();
    }
}