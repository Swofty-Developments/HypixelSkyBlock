package net.swofty.type.skyblockgeneric.levels.causes;

import lombok.Getter;
import net.swofty.type.generic.levels.abstr.SkyBlockLevelCauseAbstr;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.fairysouls.FairySoulExchangeLevels;

@Getter
public class FairySoulExchangeLevelCause extends SkyBlockLevelCauseAbstr {
    private final FairySoulExchangeLevels level;
    private int exchangeCount;

    public FairySoulExchangeLevelCause(int exchangeCount) {
        this.exchangeCount = exchangeCount;
        this.level = FairySoulExchangeLevels.getLevel(exchangeCount);
    }

    @Override
    public double xpReward() {
        return 0;
    }

    @Override
    public boolean shouldDisplayMessage(HypixelPlayer player) {
        return false;
    }
}
