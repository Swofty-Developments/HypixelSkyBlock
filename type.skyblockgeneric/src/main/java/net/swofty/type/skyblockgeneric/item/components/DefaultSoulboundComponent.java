package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;

public class DefaultSoulboundComponent extends SkyBlockItemComponent {
    @Getter
    private final boolean coopAllowed;

    public DefaultSoulboundComponent(boolean coopAllowed) {
        this.coopAllowed = coopAllowed;
    }
}