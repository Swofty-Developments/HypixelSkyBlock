package net.swofty.utility;

import net.minestom.server.item.Material;
import net.swofty.item.ItemType;

import java.util.Arrays;
import java.util.List;

public enum ItemGroups {
    TOOLS(),
    PICKAXE(),
    SWORD(),
    FISHING_WEAPON(),
    LONG_SWORD(),
    FISHING_ROD(),
    GAUNTLET(),
    DRILL(),
    ;

    public List<Material> materials;
    public List<ItemType> itemTypes;

    ItemGroups(List<Material> materials, List<ItemType> itemTypes) {
        this.materials = materials;
        this.itemTypes = itemTypes;
    }

    ItemGroups(ItemType... itemTypes) {
        this.itemTypes = Arrays.asList(itemTypes);
    }

    ItemGroups(Material... materials) {
        this.materials = Arrays.asList(materials);
    }

    ItemGroups() {
        this.materials = null;
        this.itemTypes = null;
    }
}
