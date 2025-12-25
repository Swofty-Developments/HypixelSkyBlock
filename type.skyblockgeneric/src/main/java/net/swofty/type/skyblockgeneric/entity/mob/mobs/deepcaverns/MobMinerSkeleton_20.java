package net.swofty.type.skyblockgeneric.entity.mob.mobs.deepcaverns;

import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.loottable.OtherLoot;

public class MobMinerSkeleton_20 extends MobMinerSkeleton_15 {
    @Override
    public Integer getLevel() {
        return 20;
    }

    @Override
    public ItemStatistics getBaseStatistics() {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.HEALTH, 300D)
                .withBase(ItemStatistic.DAMAGE, 200D)
                .withBase(ItemStatistic.SPEED, 100D)
                .build();
    }

    @Override
    public OtherLoot getOtherLoot() {
        return new OtherLoot(24, 15, 40);
    }

    @Override
    public String getMobID() {
        return "DIAMOND_SKELETON_20";
    }
}
