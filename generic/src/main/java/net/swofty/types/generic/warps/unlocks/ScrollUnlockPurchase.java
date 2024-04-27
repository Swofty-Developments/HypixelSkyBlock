package net.swofty.types.generic.warps.unlocks;

import net.swofty.types.generic.utility.StringUtility;
import net.swofty.types.generic.warps.ScrollUnlockReason;

public class ScrollUnlockPurchase extends ScrollUnlockReason {
    private final String npc;

    public ScrollUnlockPurchase(String npc) {
        this.npc = npc;
    }

    @Override
    public String getTitleReason() {
        return "§cScroll bought from NPC!";
    }

    @Override
    public String getSubReason() {
        return "§7Purchase the scroll from the " + StringUtility.toNormalCase(npc) + ".";
    }
}
