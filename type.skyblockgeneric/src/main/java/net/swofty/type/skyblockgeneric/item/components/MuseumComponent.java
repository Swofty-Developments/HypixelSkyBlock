package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;
import net.swofty.type.skyblockgeneric.museum.MuseumableItemCategory;

@Getter
public class MuseumComponent extends SkyBlockItemComponent {
    private final MuseumableItemCategory category;

    public MuseumComponent(String category) {
        this.category = MuseumableItemCategory.valueOf(category);
    }
}