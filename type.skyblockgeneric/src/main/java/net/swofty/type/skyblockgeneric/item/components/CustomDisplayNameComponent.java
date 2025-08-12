package net.swofty.type.skyblockgeneric.item.components;

import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;

import java.util.function.Function;

public class CustomDisplayNameComponent extends SkyBlockItemComponent {
    private final Function<SkyBlockItem, String> displayNameProvider;

    public CustomDisplayNameComponent(Function<SkyBlockItem, String> displayNameProvider) {
        this.displayNameProvider = displayNameProvider;
    }

    public String getDisplayName(SkyBlockItem item) {
        return displayNameProvider.apply(item);
    }
}