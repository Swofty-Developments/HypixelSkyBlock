package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;
import net.swofty.type.skyblockgeneric.museum.MuseumableItemCategory;

import java.util.List;
import java.util.Map;

@Getter
public class MuseumComponent extends SkyBlockItemComponent {
    private final MuseumableItemCategory category;
    private final String gameStage;
    private final int donationXp;
    private final Map<String, String> parent;
    private final List<String> mappedItemIds;

    public MuseumComponent(String category, String gameStage, int donationXp,
                           Map<String, String> parent, List<String> mappedItemIds) {
        this.category = MuseumableItemCategory.valueOf(category.toUpperCase());
        this.gameStage = gameStage;
        this.donationXp = donationXp;
        this.parent = parent;
        this.mappedItemIds = mappedItemIds;
    }
}