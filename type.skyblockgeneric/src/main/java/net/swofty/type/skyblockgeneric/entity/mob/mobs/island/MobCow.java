package net.swofty.type.skyblockgeneric.entity.mob.mobs.island;

import net.swofty.type.skyblockgeneric.entity.mob.impl.PrivateIslandBestiaryMob;
import net.swofty.type.skyblockgeneric.entity.mob.impl.PrivateIslandMobDefinitions;

public class MobCow extends PrivateIslandBestiaryMob {
    public MobCow() {
        super(PrivateIslandMobDefinitions.cow(), new double[]{50}, new double[]{0});
    }
}
