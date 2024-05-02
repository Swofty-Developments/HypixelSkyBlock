package net.swofty.types.generic.item.items.combat;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class WhippedMagmaCream implements Enchanted, Sellable, SkullHead {

    @Override
    public double getSellValue() {
        return 204800;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "7da530ad09e69e4678c69cf5b8e0f0751e4630355fc7f0c226c955baace30fb2";
    }
}
