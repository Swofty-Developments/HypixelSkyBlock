package net.swofty.types.generic.item.items.enchanted;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class EnchantedTitanium implements Enchanted, Sellable, SkullHead {
    @Override
    public double getSellValue() {
        return 3200;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "3dcc0ec9873f4f8d407ba0a0f983e257787772eaf8784e226a61c7f727ac9e26";
    }
}