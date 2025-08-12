package net.swofty.type.skyblockgeneric.levels.causes;

import lombok.Getter;
import net.swofty.type.generic.levels.abstr.SkyBlockLevelCauseAbstr;
import net.swofty.type.generic.mission.MissionData;
import net.swofty.type.generic.user.HypixelPlayer;

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
    public boolean shouldDisplayMessage(HypixelPlayer player) {
        return false;
    }
}
