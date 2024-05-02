package net.swofty.types.generic.item.impl;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public interface SkullHead {

    String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item);
}
