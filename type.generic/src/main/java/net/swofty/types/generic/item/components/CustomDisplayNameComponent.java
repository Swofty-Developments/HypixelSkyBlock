package net.swofty.types.generic.item.components;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.SkyBlockItemComponent;

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