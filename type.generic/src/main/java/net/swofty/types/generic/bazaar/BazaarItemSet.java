package net.swofty.types.generic.bazaar;

import net.swofty.types.generic.item.ItemTypeLinker;

import java.util.List;

public class BazaarItemSet {
    public List<ItemTypeLinker> items;
    public ItemTypeLinker displayMaterial;
    public String displayName;

    public BazaarItemSet(ItemTypeLinker displayMaterial, String displayName, ItemTypeLinker... items) {
        this.displayMaterial = displayMaterial;
        this.displayName = displayName;
        this.items = List.of(items);
    }
}
