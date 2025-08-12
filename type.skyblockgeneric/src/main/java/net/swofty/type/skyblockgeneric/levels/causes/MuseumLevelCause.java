package net.swofty.type.skyblockgeneric.levels.causes;

import lombok.Getter;
import net.swofty.type.skyblockgeneric.levels.abstr.SkyBlockLevelCauseAbstr;
import net.swofty.type.skyblockgeneric.museum.MuseumRewards;
import net.swofty.type.skyblockgeneric.museum.rewards.MuseumXPReward;
import SkyBlockPlayer;

@Getter
public class MuseumLevelCause extends SkyBlockLevelCauseAbstr {
    private MuseumRewards reward;

    public MuseumLevelCause(MuseumRewards reward) {
        this.reward = reward;
    }

    @Override
    public double xpReward() {
        MuseumXPReward xpReward = reward.getXPReward();

        if (xpReward != null) {
            return xpReward.getXp();
        }

        return 0;
    }

    @Override
    public boolean shouldDisplayMessage(SkyBlockPlayer player) {
        return false;
    }
}
