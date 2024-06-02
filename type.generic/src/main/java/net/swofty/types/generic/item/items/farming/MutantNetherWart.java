package net.swofty.types.generic.item.items.farming;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class MutantNetherWart implements Enchanted, Sellable, SkullHead {
    @Override
    public double getSellValue() {
        return 102400;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "111a3cec7aaf904212ccf93bb67a3caf3d649783ba90b8b60bb63c7687eb39f";
    }
}