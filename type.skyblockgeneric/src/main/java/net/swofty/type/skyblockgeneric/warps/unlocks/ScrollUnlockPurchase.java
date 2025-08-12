package net.swofty.type.skyblockgeneric.warps.unlocks;

import net.swofty.commons.StringUtility;
import net.swofty.type.generic.warps.ScrollUnlockReason;

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
