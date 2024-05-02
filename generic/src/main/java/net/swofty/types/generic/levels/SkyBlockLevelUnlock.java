package net.swofty.types.generic.levels;

import net.minestom.server.item.ItemStack;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.List;

public abstract class SkyBlockLevelUnlock {
    public abstract UnlockType type();
    public abstract ItemStack.Builder getItemDisplay(SkyBlockPlayer player, int level);
    public abstract List<String> getDisplay(SkyBlockPlayer player, int level);

    public enum UnlockType {
        STATISTIC,
        CUSTOM
    }
}
