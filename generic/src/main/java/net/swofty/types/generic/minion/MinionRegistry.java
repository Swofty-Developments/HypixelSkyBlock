package net.swofty.types.generic.minion;

import net.swofty.types.generic.minion.minions.MinionCobblestone;
import net.swofty.types.generic.utility.StringUtility;

public enum MinionRegistry {
    COBBLESTONE(MinionCobblestone.class),
    ;

    private final Class<? extends SkyBlockMinion> minionClass;

    MinionRegistry(Class<? extends SkyBlockMinion> minionClass) {
        this.minionClass = minionClass;
    }

    public SkyBlockMinion asSkyBlockMinion() {
        try {
            return minionClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create minion instance", e);
        }
    }

    public String getDisplay() {
        return StringUtility.toNormalCase(name()) + " Minion";
    }
}
