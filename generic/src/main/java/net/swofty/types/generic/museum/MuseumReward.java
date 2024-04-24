package net.swofty.types.generic.museum;

import net.swofty.types.generic.user.SkyBlockPlayer;

public abstract class MuseumReward {
    public abstract void onUnlock(MuseumRewards rewards, SkyBlockPlayer player);
    public abstract String loreDisplay();
}
