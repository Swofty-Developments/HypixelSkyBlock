package net.swofty.type.skyblockgeneric.levels.causes;

import net.swofty.commons.item.ItemType;
import net.swofty.type.generic.levels.abstr.SkyBlockLevelCauseAbstr;
import net.swofty.type.generic.user.HypixelPlayer;

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
    public boolean shouldDisplayMessage(HypixelPlayer player) {
        return true;
    }
}
