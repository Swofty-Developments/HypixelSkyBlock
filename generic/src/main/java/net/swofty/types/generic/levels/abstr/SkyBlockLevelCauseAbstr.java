package net.swofty.types.generic.levels.abstr;

import net.swofty.types.generic.user.SkyBlockPlayer;

public abstract class SkyBlockLevelCauseAbstr {
    public abstract boolean hasUnlocked(SkyBlockPlayer player);
    public abstract double xpReward();
}
