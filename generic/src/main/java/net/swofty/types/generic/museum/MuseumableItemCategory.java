package net.swofty.types.generic.museum;

import lombok.Getter;
import net.minestom.server.item.Material;
import net.swofty.types.generic.item.ItemType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public enum MuseumableItemCategory {
    WEAPONS("Weapons", Material.DIAMOND_SWORD, "§6"),
    ;

    private final static Map<MuseumableItemCategory, List<ItemType>> ITEMS = new HashMap<>();

    private final String category;
    private final Material material;
    private final String color;

    MuseumableItemCategory(String category, Material material, String color) {
        this.category = category;
        this.material = material;
        this.color = color;
    }

    public static void addItem(MuseumableItemCategory category, ItemType item) {
        if (!ITEMS.containsKey(category)) {
            ITEMS.put(category, new ArrayList<>());
        }
        ITEMS.get(category).add(item);
    }
}
