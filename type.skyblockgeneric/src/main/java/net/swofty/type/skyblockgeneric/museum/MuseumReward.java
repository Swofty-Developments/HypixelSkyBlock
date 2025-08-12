package net.swofty.type.skyblockgeneric.museum;

import SkyBlockPlayer;

public abstract class MuseumReward {
    public abstract void onUnlock(MuseumRewards rewards, SkyBlockPlayer player);
    public abstract String loreDisplay();
}
