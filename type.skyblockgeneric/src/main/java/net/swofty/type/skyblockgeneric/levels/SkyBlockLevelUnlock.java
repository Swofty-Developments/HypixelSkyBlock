package net.swofty.type.skyblockgeneric.levels;

import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.List;

public abstract class SkyBlockLevelUnlock {
    public abstract UnlockType type();
    public abstract ItemStack.Builder getItemDisplay(HypixelPlayer player, int level);
    public abstract List<String> getDisplay(HypixelPlayer player, int level);

    public enum UnlockType {
        STATISTIC,
        CUSTOM
    }
}
