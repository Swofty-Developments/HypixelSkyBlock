package net.swofty.types.generic.levels.abstr;

import net.swofty.types.generic.user.SkyBlockPlayer;

public abstract class SkyBlockLevelCauseAbstr {
    public abstract double xpReward();
    public abstract boolean shouldDisplayMessage(SkyBlockPlayer player);
}
