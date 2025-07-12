package net.swofty.types.generic.entity.mob;

import net.minestom.server.entity.EntityType;
import net.minestom.server.item.Material;

public abstract class BestiaryMob extends SkyBlockMob {
    public BestiaryMob(EntityType entityType) {
        super(entityType);
    }

    public abstract int getMaxBestiaryTier();
    public abstract int getBestiaryBracket();
    public abstract String getMobID();
    public abstract Material getDisplayItem();
    public abstract String getTexture();
}
