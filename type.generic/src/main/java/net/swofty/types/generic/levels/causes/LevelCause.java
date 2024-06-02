package net.swofty.types.generic.levels.causes;

import lombok.Getter;
import net.swofty.types.generic.levels.abstr.CauseEmblem;
import net.swofty.types.generic.levels.abstr.SkyBlockLevelCauseAbstr;
import net.swofty.types.generic.user.SkyBlockPlayer;

@Getter
public class LevelCause extends SkyBlockLevelCauseAbstr implements CauseEmblem {
    private int level;

    public LevelCause(int level) {
        this.level = level;
    }

    @Override
    public double xpReward() {
        return 0;
    }

    @Override
    public String getEmblemRequiresMessage() {
        return "Requires SkyBlock Level " + level;
    }

    @Override
    public String emblemEisplayName() {
        return "SkyBlock Level " + level;
    }

    @Override
    public boolean shouldDisplayMessage(SkyBlockPlayer player) {
        return false; // Only used for emblems, should not display
    }
}
