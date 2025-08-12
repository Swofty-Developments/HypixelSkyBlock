package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.type.generic.item.SkyBlockItemComponent;
import net.swofty.type.generic.museum.MuseumableItemCategory;

@Getter
public class MuseumComponent extends SkyBlockItemComponent {
    private final MuseumableItemCategory category;

    public MuseumComponent(String category) {
        this.category = MuseumableItemCategory.valueOf(category);
    }
}