package net.swofty.types.generic.museum.rewards;

import lombok.Getter;
import net.swofty.types.generic.levels.SkyBlockLevelCause;
import net.swofty.types.generic.museum.MuseumReward;
import net.swofty.types.generic.museum.MuseumRewards;
import net.swofty.types.generic.user.SkyBlockPlayer;

@Getter
public class MuseumXPReward extends MuseumReward {
    private final int xp;

    public MuseumXPReward(int xp) {
        this.xp = xp;
    }
    @Override
    public void onUnlock(MuseumRewards rewards, SkyBlockPlayer player) {
        player.getSkyBlockExperience().addExperience(SkyBlockLevelCause.getMuseumCause(rewards));
    }

    @Override
    public String loreDisplay() {
        return "§b" + xp + " SkyBlock XP";
    }
}
