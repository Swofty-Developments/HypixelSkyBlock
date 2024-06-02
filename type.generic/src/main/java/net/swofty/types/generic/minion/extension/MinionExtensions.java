package net.swofty.types.generic.minion.extension;

import lombok.Getter;
import net.swofty.types.generic.minion.extension.extensions.MinionFuelExtension;
import net.swofty.types.generic.minion.extension.extensions.MinionShippingExtension;
import net.swofty.types.generic.minion.extension.extensions.MinionSkinExtension;
import net.swofty.types.generic.minion.extension.extensions.MinionUpgradeExtension;

@Getter
public enum MinionExtensions {
    SKIN_SLOT(MinionSkinExtension.class, 10),
    FUEL_SLOT(MinionFuelExtension.class, 19),
    SHIPPING_SLOT(MinionShippingExtension.class, 28),
    UPGRADE_SLOT(MinionUpgradeExtension.class, 37, 46),
    ;

    private final Class<? extends MinionExtension> instance;
    private final int[] slots;

    MinionExtensions(Class<? extends MinionExtension> instance, int... slots) {
        this.instance = instance;
        this.slots = slots;
    }

    public static MinionExtensions getFromSlot(int slot) {
        for (MinionExtensions value : values()) {
            for (int i : value.slots) {
                if (i == slot) {
                    return value;
                }
            }
        }
        return null;
    }
}
