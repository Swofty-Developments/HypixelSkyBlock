package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.minestom.server.color.Color;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;

public class LeatherColorComponent extends SkyBlockItemComponent {
    @Getter
    private final Color color;

    public LeatherColorComponent(Color color) {
        this.color = color;
    }
}