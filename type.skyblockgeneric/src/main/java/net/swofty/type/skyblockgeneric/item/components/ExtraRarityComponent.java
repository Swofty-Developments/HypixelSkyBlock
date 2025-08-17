package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;

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
