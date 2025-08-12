package net.swofty.type.skyblockgeneric.museum.rewards;

import lombok.Getter;
import net.swofty.type.generic.levels.SkyBlockLevelCause;
import net.swofty.type.generic.museum.MuseumReward;
import net.swofty.type.generic.museum.MuseumRewards;
import net.swofty.type.generic.user.HypixelPlayer;

@Getter
public class MuseumXPReward extends MuseumReward {
    private final int xp;

    public MuseumXPReward(int xp) {
        this.xp = xp;
    }
    @Override
    public void onUnlock(MuseumRewards rewards, HypixelPlayer player) {
        player.getSkyBlockExperience().addExperience(SkyBlockLevelCause.getMuseumCause(rewards));
    }

    @Override
    public String loreDisplay() {
        return "§b" + xp + " SkyBlock XP";
    }
}
