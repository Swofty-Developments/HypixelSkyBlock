package net.swofty.types.generic.entity.mob.impl;

import lombok.Builder;
import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.entity.mob.MobRegistry;
import net.swofty.types.generic.entity.mob.SkyBlockMob;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.region.SkyBlockRegion;

import java.util.List;

public interface RegionPopulator {
    List<Populator> getPopulators();

    static void populateRegion(MobRegistry registry, Populator populator) {
        SkyBlockRegion region = SkyBlockRegion.getRandomRegionOfType(populator.regionType());

        if (region == null) return;

        Pos randomPosition = region.getRandomPositionForEntity(SkyBlockConst.getInstanceContainer());

        if (randomPosition == null) return;

        if (!SkyBlockConst.getInstanceContainer().isChunkLoaded(randomPosition))
            SkyBlockConst.getInstanceContainer().loadChunk(randomPosition).join();

        SkyBlockMob mob = registry.asMob();
        mob.setInstance(SkyBlockConst.getInstanceContainer(), randomPosition);
    }

    @Builder
    record Populator(RegionType regionType, int minimumAmountToPopulate) {}
}
