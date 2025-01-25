package net.swofty.types.generic.item.components;

import lombok.Getter;
import net.minestom.server.item.ItemAnimation;
import net.swofty.types.generic.item.SkyBlockItemComponent;

import java.util.List;

public class DisableAnimationComponent extends SkyBlockItemComponent {
    @Getter
    private final List<ItemAnimation> disabledAnimations;

    public DisableAnimationComponent(List<ItemAnimation> disabledAnimations) {
        this.disabledAnimations = disabledAnimations;
    }
}