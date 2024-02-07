package net.swofty.types.generic.item.impl;

import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public interface SkullHead extends Placeable {

    @Override
    default void onPlace(PlayerBlockPlaceEvent event, SkyBlockPlayer player, SkyBlockItem item){
        event.setCancelled(true);
    }

    String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item);
}
