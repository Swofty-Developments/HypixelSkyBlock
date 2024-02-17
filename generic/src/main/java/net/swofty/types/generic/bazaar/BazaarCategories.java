package net.swofty.types.generic.bazaar;

import lombok.Getter;
import net.minestom.server.item.Material;
import net.swofty.types.generic.item.ItemType;

import java.util.Map;
import java.util.Set;

@Getter
public enum BazaarCategories {
    FARMING(Material.GOLDEN_HOE, "§e"),
    MINING(Material.DIAMOND_PICKAXE, "§b", new BazaarItemSet(ItemType.COBBLESTONE, "Cobblestone",
            Map.entry(ItemType.COBBLESTONE, 5D), Map.entry(ItemType.ENCHANTED_COBBLESTONE, 800D))),
    COMBAT(Material.IRON_SWORD, "§c"),
    WOODS_AND_FISHES(Material.FISHING_ROD, "§6"),
    ODDITIES(Material.ENCHANTING_TABLE, "§d"),;

    public final Material displayItem;
    public final Set<BazaarItemSet> items;
    public final String color;

    BazaarCategories(Material displayItem, String color, BazaarItemSet... items) {
        this.displayItem = displayItem;
        this.color = color;
        this.items = Set.of(items);
    }

    BazaarCategories(Material displayItem, String color) {
        this.displayItem = displayItem;
        this.color = color;
        this.items = Set.of();
    }

    public static Map.Entry<BazaarCategories, BazaarItemSet> getFromItem(ItemType itemType) {
        for (BazaarCategories category : BazaarCategories.values()) {
            for (BazaarItemSet itemSet : category.items) {
                for (Map.Entry<ItemType, Double> item : itemSet.items) {
                    if (item.getKey() == itemType) {
                        return Map.entry(category, itemSet);
                    }
                }
            }
        }
        return null;
    }
}
