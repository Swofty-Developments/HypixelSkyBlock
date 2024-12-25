package net.swofty.types.generic.levels.causes;

import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.levels.abstr.SkyBlockLevelCauseAbstr;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class NewAccessoryLevelCause extends SkyBlockLevelCauseAbstr {
    public ItemType itemTypeLinker;

    public NewAccessoryLevelCause(ItemType itemTypeLinker) {
        this.itemTypeLinker = itemTypeLinker;
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
