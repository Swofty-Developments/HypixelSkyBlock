package net.swofty.types.generic.item.components;

import lombok.Getter;
import net.minestom.server.event.player.PlayerItemAnimationEvent;
import net.swofty.types.generic.item.SkyBlockItemComponent;

import java.util.List;

public class DisableAnimationComponent extends SkyBlockItemComponent {
    @Getter
    private final List<PlayerItemAnimationEvent.ItemAnimationType> disabledAnimations;

    public DisableAnimationComponent(List<PlayerItemAnimationEvent.ItemAnimationType> disabledAnimations) {
        this.disabledAnimations = disabledAnimations;
    }
}