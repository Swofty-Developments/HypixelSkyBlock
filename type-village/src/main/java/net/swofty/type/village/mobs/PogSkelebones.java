package net.swofty.type.village.mobs;

import net.minestom.server.entity.EntityType;
import net.swofty.types.generic.entity.mob.SkyBlockMob;
import net.swofty.types.generic.entity.mob.impl.RegionPopulator;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.List;

public class PogSkelebones extends SkyBlockMob implements RegionPopulator {
    public PogSkelebones(EntityType entityType) {
        super(entityType);
    }

    @Override
    public String getDisplayName() {
        return "Pog Skelebones";
    }

    @Override
    public Integer getLevel() {
        return 5;
    }

    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.builder()
                .with(ItemStatistic.HEALTH, 10)
                .with(ItemStatistic.DAMAGE, 5)
                .build();
    }

    @Override
    public List<Populator> getPopulators() {
        return List.of(new Populator(RegionType.BANK, 10));
    }
}
