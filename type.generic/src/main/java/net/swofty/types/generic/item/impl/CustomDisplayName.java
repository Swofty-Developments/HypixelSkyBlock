package net.swofty.types.generic.item.impl;

import net.swofty.types.generic.item.SkyBlockItem;
import org.jetbrains.annotations.Nullable;

public interface CustomDisplayName {
    String getDisplayName(@Nullable SkyBlockItem item);
}
