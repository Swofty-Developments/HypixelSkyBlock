package net.swofty.types.generic.item.components;

import lombok.Getter;
import net.swofty.types.generic.item.SkyBlockItemComponent;
import net.swofty.types.generic.museum.MuseumableItemCategory;

@Getter
public class MuseumComponent extends SkyBlockItemComponent {
    private final MuseumableItemCategory category;

    public MuseumComponent(String category) {
        this.category = MuseumableItemCategory.valueOf(category);
    }
}