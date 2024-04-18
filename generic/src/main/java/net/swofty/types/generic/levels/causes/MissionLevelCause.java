package net.swofty.types.generic.levels.causes;

import lombok.Getter;
import net.swofty.types.generic.levels.abstr.SkyBlockLevelCauseAbstr;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.user.SkyBlockPlayer;

@Getter
public class MissionLevelCause extends SkyBlockLevelCauseAbstr {
    private final String missionKey;

    public MissionLevelCause(String missionKey) {
        this.missionKey = missionKey;
    }

    @Override
    public double xpReward() {
        return MissionData.getMissionClass(missionKey).getAttachedSkyBlockXP();
    }

    @Override
    public boolean shouldDisplayMessage(SkyBlockPlayer player) {
        return false;
    }
}
