package net.swofty.types.generic.item.items.backpacks;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Backpack;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;


public class SmallBackpack implements Backpack {
    @Override
    public int getRows() {
        return 1;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "21d837ca222cbc0bc12426f5da018c3a931b406008800960a9df112a596e7d62";
    }
}
