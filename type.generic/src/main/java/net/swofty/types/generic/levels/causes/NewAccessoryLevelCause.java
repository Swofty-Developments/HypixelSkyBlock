package net.swofty.types.generic.levels.causes;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.levels.abstr.SkyBlockLevelCauseAbstr;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class NewAccessoryLevelCause extends SkyBlockLevelCauseAbstr {
    public ItemType itemType;

    public NewAccessoryLevelCause(ItemType itemType) {
        this.itemType = itemType;
    }

    @Override
    public double xpReward() {
        return 1;
    }

    @Override
    public boolean shouldDisplayMessage(SkyBlockPlayer player) {
        return true;
    }
}
