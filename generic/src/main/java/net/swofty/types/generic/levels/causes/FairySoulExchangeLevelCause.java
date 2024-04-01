package net.swofty.types.generic.levels.causes;

import lombok.Getter;
import net.swofty.types.generic.levels.abstr.SkyBlockLevelCauseAbstr;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.fairysouls.FairySoulExchangeLevels;

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
    public boolean shouldDisplayMessage(SkyBlockPlayer player) {
        return false;
    }
}
