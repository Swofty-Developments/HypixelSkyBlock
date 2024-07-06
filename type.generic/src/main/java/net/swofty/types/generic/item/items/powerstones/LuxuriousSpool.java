package net.swofty.types.generic.item.items.powerstones;

import net.swofty.commons.statistics.ItemStatistics;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.PowerStone;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class LuxuriousSpool implements SkullHead, PowerStone {

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "";
    }
}
