package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;

// TODO: take a Component + make a deserializer for minimessage
@Getter
public class ExtraRarityComponent extends SkyBlockItemComponent {
    private final String extraDisplay;

    public ExtraRarityComponent(String extraDisplay) {
        this.extraDisplay = extraDisplay;
    }

    public String getExtraRarityDisplay() {
        return extraDisplay;
    }
}
