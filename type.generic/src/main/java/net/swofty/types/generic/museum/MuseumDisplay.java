package net.swofty.types.generic.museum;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public abstract class MuseumDisplay {
    public abstract MuseumDisplayEntityInformation display(SkyBlockPlayer player,
                                                           MuseumDisplays category,
                                                           @Nullable SkyBlockItem item,
                                                           int position);
}