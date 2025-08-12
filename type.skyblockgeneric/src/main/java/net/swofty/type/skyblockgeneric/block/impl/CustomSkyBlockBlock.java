package net.swofty.type.skyblockgeneric.block.impl;

import lombok.NonNull;
import net.minestom.server.instance.block.Block;
import net.swofty.type.generic.user.HypixelPlayer;

public interface CustomSkyBlockBlock {
    @NonNull Block getDisplayMaterial();
    @NonNull Boolean shouldPlace(HypixelPlayer player);
    @NonNull Boolean shouldDestroy(HypixelPlayer player);
}
