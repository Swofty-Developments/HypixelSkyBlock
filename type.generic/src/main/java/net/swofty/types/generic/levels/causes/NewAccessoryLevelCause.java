package net.swofty.types.generic.levels.causes;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.levels.abstr.SkyBlockLevelCauseAbstr;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class NewAccessoryLevelCause extends SkyBlockLevelCauseAbstr {
    public ItemTypeLinker itemTypeLinker;

    public NewAccessoryLevelCause(ItemTypeLinker itemTypeLinker) {
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
