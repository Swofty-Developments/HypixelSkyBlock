package net.swofty.types.generic.block.impl;

import lombok.NonNull;
import net.minestom.server.instance.block.Block;
import net.swofty.types.generic.user.SkyBlockPlayer;

public interface CustomSkyBlockBlock {
    @NonNull Block getDisplayMaterial();
    @NonNull Boolean shouldPlace(SkyBlockPlayer player);
    @NonNull Boolean shouldDestroy(SkyBlockPlayer player);
}
