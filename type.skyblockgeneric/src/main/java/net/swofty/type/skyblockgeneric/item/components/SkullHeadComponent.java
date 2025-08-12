package net.swofty.type.skyblockgeneric.item.components;

import net.swofty.type.generic.item.SkyBlockItem;
import net.swofty.type.generic.item.SkyBlockItemComponent;

import java.util.function.Function;

public class SkullHeadComponent extends SkyBlockItemComponent {
    private final Function<SkyBlockItem, String> textureProvider;

    public SkullHeadComponent(Function<SkyBlockItem, String> textureProvider) {
        this.textureProvider = textureProvider;
    }

    public String getSkullTexture(SkyBlockItem item) {
        return textureProvider.apply(item);
    }
}