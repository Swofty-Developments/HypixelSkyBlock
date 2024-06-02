package net.swofty.types.generic.item.items.farming;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class TightlyTiedHayBale implements Enchanted, Sellable, SkullHead {
    @Override
    public double getSellValue() {
        return 1119744;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "f7c33cd0c14ba830da149907f7a6aae835b6a35aea01e0ce073fb3c59cc46326";
    }
}