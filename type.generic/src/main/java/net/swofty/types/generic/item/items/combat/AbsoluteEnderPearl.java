package net.swofty.types.generic.item.items.combat;


import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class AbsoluteEnderPearl implements Enchanted, Sellable, SkullHead {
    @Override
    public double getSellValue() {
        return 11200;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "67d9fe065024fff4b34c78f92cd3150d6bdfaab7c375a0ee785076e1a4a254e9";
    }
}