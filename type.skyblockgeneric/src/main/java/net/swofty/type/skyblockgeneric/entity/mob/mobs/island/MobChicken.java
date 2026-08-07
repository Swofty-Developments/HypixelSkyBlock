package net.swofty.type.skyblockgeneric.entity.mob.mobs.island;

import net.swofty.type.skyblockgeneric.entity.mob.impl.PrivateIslandBestiaryMob;
import net.swofty.type.skyblockgeneric.entity.mob.impl.PrivateIslandMobDefinitions;

public class MobChicken extends PrivateIslandBestiaryMob {
    public MobChicken() {
        super(PrivateIslandMobDefinitions.chicken(), new double[]{50}, new double[]{0});
    }
}
