package net.swofty.type.skyblockgeneric.entity.mob.mobs.island;

import net.swofty.type.skyblockgeneric.entity.mob.impl.PrivateIslandBestiaryMob;
import net.swofty.type.skyblockgeneric.entity.mob.impl.PrivateIslandMobDefinitions;

public class MobEnderman extends PrivateIslandBestiaryMob {
    private static final double[] HEALTH = {240, 276, 317, 365, 420, 483, 555, 639, 735, 846, 973, 1119, 1287, 1480, 1702};
    private static final double[] DAMAGE = {50, 57, 65, 74, 85, 98, 113, 130, 149, 171, 197, 226, 260, 299, 344};

    public MobEnderman() {
        super(PrivateIslandMobDefinitions.enderman(1), HEALTH, DAMAGE);
    }
}
