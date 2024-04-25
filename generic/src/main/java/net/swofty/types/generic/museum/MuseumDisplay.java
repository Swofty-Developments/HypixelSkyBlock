package net.swofty.types.generic.museum;

import net.minestom.server.entity.LivingEntity;
import net.swofty.types.generic.entity.hologram.PlayerHolograms;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public abstract class MuseumDisplay {
    public abstract Map.Entry<LivingEntity,
            PlayerHolograms.ExternalPlayerHologram> display(SkyBlockPlayer player,
                                                            MuseumDisplays category,
                                                            @Nullable SkyBlockItem item,
                                                            int position);
}
