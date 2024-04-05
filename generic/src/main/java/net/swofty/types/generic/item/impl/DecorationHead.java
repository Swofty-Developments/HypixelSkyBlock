package net.swofty.types.generic.item.impl;

import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.swofty.commons.ServerType;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.entity.DecorationEntityImpl;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

public interface DecorationHead extends CustomSkyBlockItem,  SkullHead, PlaceEvent {

    @Override
    default void onPlace(PlayerBlockPlaceEvent event, SkyBlockPlayer player, SkyBlockItem item) {
        event.setCancelled(true);

        if (SkyBlockConst.getTypeLoader().getType() != ServerType.ISLAND) return;

        DecorationEntityImpl entity = new DecorationEntityImpl(item , player);
        entity.spawn(player.getInstance() , event.getBlockPosition());

    }

    @Override
    default boolean isPlaceable() {
        return true;
    }
}