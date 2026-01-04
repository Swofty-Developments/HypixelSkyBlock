package net.swofty.type.skywarsgame.luckyblock.spawns;

import net.minestom.server.entity.EntityType;
import net.minestom.server.instance.Instance;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

/**
 * Rideable horse from Lucky Block.
 * A battle-ready mount that lasts 60 seconds.
 */
public class RideableHorse extends RideableMob {

    private static final int DURATION_SECONDS = 60;

    public RideableHorse(SkywarsPlayer rider, Instance instance) {
        super(rider, instance);
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.HORSE;
    }

    @Override
    public String getDisplayName() {
        return "Battle Horse";
    }

    @Override
    public int getDurationSeconds() {
        return DURATION_SECONDS;
    }

    @Override
    protected void onMountCreated() {
        // Horse spawns with default appearance
        // Mounting is handled automatically by the base class
    }
}
