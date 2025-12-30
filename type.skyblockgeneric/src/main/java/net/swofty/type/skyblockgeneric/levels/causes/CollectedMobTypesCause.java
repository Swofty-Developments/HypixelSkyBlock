package net.swofty.type.skyblockgeneric.levels.causes;

import net.swofty.type.skyblockgeneric.levels.abstr.SkyBlockLevelCauseAbstr;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class CollectedMobTypesCause extends SkyBlockLevelCauseAbstr {
    private String mobType;

    public CollectedMobTypesCause(String mobType) {
        this.mobType = mobType;
    }

    @Override
    public double xpReward() {
        return 1;
    }

    @Override
    public boolean shouldDisplayMessage(SkyBlockPlayer player) {
        return false;
    }
}
