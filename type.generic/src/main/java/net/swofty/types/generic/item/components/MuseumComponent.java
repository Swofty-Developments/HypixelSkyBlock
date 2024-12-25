package net.swofty.types.generic.item.components;

import lombok.Getter;
import net.swofty.types.generic.museum.MuseumableItemCategory;
import net.swofty.types.generic.item.SkyBlockItemComponent;

@Getter
public class MuseumComponent extends SkyBlockItemComponent {
    private final MuseumableItemCategory category;

    public MuseumComponent(String category) {
        this.category = MuseumableItemCategory.valueOf(category);
    }
}