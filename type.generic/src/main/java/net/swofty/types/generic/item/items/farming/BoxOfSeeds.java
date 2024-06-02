package net.swofty.types.generic.item.items.farming;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class BoxOfSeeds implements Enchanted, Sellable, SkullHead {
    @Override
    public double getSellValue() {
        return 76800;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "2c3f854bb65265ab3ff2e362669a5f806ab84dc240a4b99f9a62ab7267728ba3";
    }
}