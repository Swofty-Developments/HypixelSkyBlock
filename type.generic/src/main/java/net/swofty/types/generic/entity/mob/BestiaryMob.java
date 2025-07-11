package net.swofty.types.generic.entity.mob;

import net.minestom.server.entity.EntityType;

public abstract class BestiaryMob extends SkyBlockMob {
    public BestiaryMob(EntityType entityType) {
        super(entityType);
    }

    public abstract int getMaxBestiaryTier();
    public abstract int getBestiaryBracket();
    public abstract String getMobID();
}
