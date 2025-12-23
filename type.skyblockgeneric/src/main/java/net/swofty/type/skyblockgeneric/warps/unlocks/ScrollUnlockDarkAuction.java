package net.swofty.type.skyblockgeneric.warps.unlocks;

import net.swofty.type.skyblockgeneric.warps.ScrollUnlockReason;

public class ScrollUnlockDarkAuction extends ScrollUnlockReason {
    public ScrollUnlockDarkAuction() {}

    @Override
    public String getTitleReason() {
        return "§cScroll bought in Dark Auction!";
    }

    @Override
    public String getSubReason() {
        return "§7Buy this scroll in a Dark Auction event.";
    }
}