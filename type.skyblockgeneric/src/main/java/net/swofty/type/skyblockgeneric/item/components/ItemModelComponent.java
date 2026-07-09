package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;

public class ItemModelComponent extends SkyBlockItemComponent {
    @Getter
    private final String itemModel;

    public ItemModelComponent(String itemModel) {
        this.itemModel = itemModel;
    }
}
