package net.swofty.types.generic.bazaar;

import lombok.Getter;
import net.minestom.server.item.Material;
import net.swofty.commons.bazaar.BazaarInitializationRequest;
import net.swofty.types.generic.item.ItemType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
public enum BazaarCategories {
    FARMING(Material.GOLDEN_HOE, Material.YELLOW_STAINED_GLASS_PANE, "§e"),
    MINING(Material.DIAMOND_PICKAXE, Material.BLUE_STAINED_GLASS_PANE,
            "§b", new BazaarItemSet(
                    ItemType.COBBLESTONE, "Cobblestone",
                    Map.entry(ItemType.COBBLESTONE, Map.entry(6.8D, 4.4D)),
                    Map.entry(ItemType.ENCHANTED_COBBLESTONE, Map.entry(1000D, 900D)))),
    COMBAT(Material.IRON_SWORD, Material.RED_STAINED_GLASS_PANE, "§c"),
    WOODS_AND_FISHES(Material.FISHING_ROD, Material.ORANGE_STAINED_GLASS_PANE, "§6"),
    ODDITIES(Material.ENCHANTING_TABLE, Material.PINK_STAINED_GLASS_PANE, "§d"),;

    private final Material displayItem;
    private final Material glassItem;
    private final Set<BazaarItemSet> items;
    private final String color;

    BazaarCategories(Material displayItem, Material glassItem, String color, BazaarItemSet... items) {
        this.displayItem = displayItem;
        this.glassItem = glassItem;
        this.color = color;
        this.items = Set.of(items);
    }

    BazaarCategories(Material displayItem, Material glassItem, String color) {
        this.displayItem = displayItem;
        this.glassItem = glassItem;
        this.color = color;
        this.items = Set.of();
    }

    public static BazaarInitializationRequest getInitializationRequest() {
        Map<String, Map.Entry<Double, Double>> items = new HashMap<>();
        for (BazaarCategories category : BazaarCategories.values()) {
            for (BazaarItemSet itemSet : category.items) {
                for (Map.Entry<ItemType, Map.Entry<Double, Double>> item : itemSet.items) {
                    items.put(item.getKey().name(), item.getValue());
                }
            }
        }

        return new BazaarInitializationRequest(items);
    }

    public static Map.Entry<BazaarCategories, BazaarItemSet> getFromItem(ItemType itemType) {
        for (BazaarCategories category : BazaarCategories.values()) {
            for (BazaarItemSet itemSet : category.items) {
                for (Map.Entry<ItemType, Map.Entry<Double, Double>> item : itemSet.items) {
                    if (item.getKey() == itemType) {
                        return Map.entry(category, itemSet);
                    }
                }
            }
        }
        return null;
    }
}
