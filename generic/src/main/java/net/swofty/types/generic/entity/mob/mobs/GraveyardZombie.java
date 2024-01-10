package net.swofty.types.generic.entity.mob.mobs;

import net.minestom.server.entity.EntityType;
import net.swofty.types.generic.entity.mob.SkyBlockMob;
import net.swofty.types.generic.entity.mob.impl.RegionPopulator;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class GraveyardZombie extends SkyBlockMob implements RegionPopulator {

    public GraveyardZombie(EntityType entityType) {
        super(entityType);
    }

    @Override
    public String getDisplayName() {
        return "Graveyard Zombie";
    }

    @Override
    public Integer getLevel() {
        return 1;
    }

    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.builder()
                .with(ItemStatistic.HEALTH, 100)
                .with(ItemStatistic.DAMAGE, 20)
                .build();
    }

    @Override
    public List<Populator> getPopulators() {
        return Arrays.asList(
                new Populator(RegionType.GRAVEYARD, 50)
        );
    }
}
