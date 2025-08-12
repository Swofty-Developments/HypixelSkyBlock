package net.swofty.type.skyblockgeneric.levels.causes;

import lombok.Getter;
import net.swofty.type.skyblockgeneric.levels.abstr.CauseEmblem;
import net.swofty.type.skyblockgeneric.levels.abstr.SkyBlockLevelCauseAbstr;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

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
