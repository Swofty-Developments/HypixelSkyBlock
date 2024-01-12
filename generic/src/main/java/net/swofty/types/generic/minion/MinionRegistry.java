package net.swofty.types.generic.minion;

import lombok.Getter;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.minion.minions.MinionCobblestone;
import net.swofty.types.generic.utility.StringUtility;

public enum MinionRegistry {
    COBBLESTONE(MinionCobblestone.class, ItemType.COBBLESTONE_MINION),
    ;

    private final Class<? extends SkyBlockMinion> minionClass;
    @Getter
    private final ItemType item;

    MinionRegistry(Class<? extends SkyBlockMinion> minionClass, ItemType item) {
        this.minionClass = minionClass;
        this.item = item;
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
