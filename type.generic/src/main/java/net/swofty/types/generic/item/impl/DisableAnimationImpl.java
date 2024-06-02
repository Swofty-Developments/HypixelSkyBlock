package net.swofty.types.generic.item.impl;

import net.minestom.server.event.player.PlayerItemAnimationEvent;

import java.util.List;

public interface DisableAnimationImpl {
    List<PlayerItemAnimationEvent.ItemAnimationType> getDisabledAnimations();
}
