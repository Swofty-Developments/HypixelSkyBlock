package net.swofty.types.generic.item.items.farming;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class PolishedPumpkin implements Enchanted, Sellable, SkullHead {
    @Override
    public double getSellValue() {
        return 256000;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "8db711ff52eedda59c434bb03169763d7c40b5b89127778feacd63aa94dfc";
    }
}