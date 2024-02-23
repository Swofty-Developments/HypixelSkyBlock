package net.swofty.types.generic.bazaar;

import net.minestom.server.item.Material;
import net.swofty.types.generic.item.ItemType;

import java.util.List;
import java.util.Map;

public class BazaarItemSet {
    public List<Map.Entry<ItemType, Map.Entry<Double, Double>>> items;
    public ItemType displayMaterial;
    public String displayName;

    public BazaarItemSet(ItemType displayMaterial, String displayName, Map.Entry<ItemType, Map.Entry<Double, Double>>... items) {
        this.items = List.of(items);
        this.displayMaterial = displayMaterial;
        this.displayName = displayName;
    }
}
