package net.swofty.type.skyblockgeneric.levels.abstr;

import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public abstract class SkyBlockLevelCauseAbstr {
    public abstract double xpReward();
    public abstract boolean shouldDisplayMessage(SkyBlockPlayer player);
}
