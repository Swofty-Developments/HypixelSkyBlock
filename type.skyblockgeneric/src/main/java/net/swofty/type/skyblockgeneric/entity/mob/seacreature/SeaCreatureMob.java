package net.swofty.type.skyblockgeneric.entity.mob.seacreature;

import net.minestom.server.entity.EntityType;
import net.swofty.type.skyblockgeneric.entity.mob.SkyBlockMob;

public abstract class SeaCreatureMob extends SkyBlockMob {

    protected SeaCreatureMob(EntityType entityType) {
        super(entityType);
    }

    public abstract String getSeaCreatureId();
}
