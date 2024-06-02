package net.swofty.types.generic.levels.causes;

import lombok.Getter;
import net.swofty.types.generic.levels.abstr.SkyBlockLevelCauseAbstr;
import net.swofty.types.generic.museum.MuseumRewards;
import net.swofty.types.generic.museum.rewards.MuseumXPReward;
import net.swofty.types.generic.user.SkyBlockPlayer;

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
