package net.swofty.types.generic.warps.unlocks;

import net.swofty.types.generic.warps.ScrollUnlockReason;

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

