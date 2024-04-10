package net.swofty.types.generic.minion.extension;

import lombok.Getter;
import net.swofty.types.generic.minion.extension.extensions.MinionSkinExtension;

@Getter
public enum MinionExtensions {
    SKIN_SLOT(MinionSkinExtension.class, 10),
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
