package net.swofty.type.skyblockgeneric.entity.mob.mobs.island;

import net.swofty.type.skyblockgeneric.entity.mob.impl.PrivateIslandBestiaryMob;
import net.swofty.type.skyblockgeneric.entity.mob.impl.PrivateIslandMobDefinitions;
import net.swofty.type.skyblockgeneric.entity.mob.impl.RegionPopulator;
import net.swofty.type.skyblockgeneric.region.RegionType;

import java.util.List;

public class MobZombie_01 extends PrivateIslandBestiaryMob implements RegionPopulator {
    private static final double[] HEALTH = {100, 115, 132, 152, 174, 201, 231, 266, 305, 351, 404, 465, 535, 615, 707};
    private static final double[] DAMAGE = {20, 23, 26, 29, 33, 37, 42, 47, 53, 60, 68, 77, 87, 98, 111};

    public MobZombie_01() {
        super(PrivateIslandMobDefinitions.zombie(1, "ZOMBIE_01"), HEALTH, DAMAGE);
    }

    @Override
    public List<Populator> getPopulators() {
        return List.of(new Populator(RegionType.PRIVATE_ISLAND, 20));
    }
}
