package net.swofty.type.skyblockgeneric.warps.unlocks;

import net.swofty.type.skyblockgeneric.warps.ScrollUnlockReason;

public class ScrollUnlockCustomRecipe extends ScrollUnlockReason {
    private final String subReason;

    public ScrollUnlockCustomRecipe(String subReason) {
        this.subReason = subReason;
    }

    @Override
    public String getTitleReason() {
        return "§cScroll crafted from recipe!";
    }

    @Override
    public String getSubReason() {
        return "§7" + subReason;
    }
}

