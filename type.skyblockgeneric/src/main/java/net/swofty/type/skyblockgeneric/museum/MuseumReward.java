package net.swofty.type.skyblockgeneric.museum;

import net.swofty.type.generic.user.HypixelPlayer;

public abstract class MuseumReward {
    public abstract void onUnlock(MuseumRewards rewards, HypixelPlayer player);
    public abstract String loreDisplay();
}
