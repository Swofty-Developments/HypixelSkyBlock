package net.swofty.types.generic.item.components;

import lombok.Getter;
import net.swofty.types.generic.item.SkyBlockItemComponent;

public class DefaultSoulboundComponent extends SkyBlockItemComponent {
    @Getter
    private final boolean coopAllowed;

    public DefaultSoulboundComponent(boolean coopAllowed) {
        this.coopAllowed = coopAllowed;
    }
}