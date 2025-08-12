package net.swofty.type.skyblockgeneric.levels.causes;

import lombok.Getter;
import net.swofty.type.skyblockgeneric.levels.abstr.SkyBlockLevelCauseAbstr;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import SkyBlockPlayer;

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
