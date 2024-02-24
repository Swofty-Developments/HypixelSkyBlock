package net.swofty.types.generic.bazaar;

import lombok.Getter;
import net.minestom.server.item.Material;
import net.swofty.commons.bazaar.BazaarInitializationRequest;
import net.swofty.types.generic.item.ItemType;

import java.util.*;

@Getter
public enum BazaarCategories {
    FARMING(Material.GOLDEN_HOE, Material.YELLOW_STAINED_GLASS_PANE, "§e"),
    MINING(Material.DIAMOND_PICKAXE, Material.BLUE_STAINED_GLASS_PANE,
            "§b", new BazaarItemSet(ItemType.COBBLESTONE, "Cobblestone",
            ItemType.COBBLESTONE, ItemType.ENCHANTED_COBBLESTONE)),
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
        List<ItemType> items = new ArrayList<>();
        for (BazaarCategories category : BazaarCategories.values()) {
            for (BazaarItemSet itemSet : category.items) {
                items.addAll(itemSet.items);
            }
        }

        return new BazaarInitializationRequest(items.stream().map(ItemType::name).toList());
    }

    public static Map.Entry<BazaarCategories, BazaarItemSet> getFromItem(ItemType itemType) {
        for (BazaarCategories category : BazaarCategories.values()) {
            for (BazaarItemSet itemSet : category.items) {
                if (itemSet.items.contains(itemType)) {
                    return new AbstractMap.SimpleEntry<>(category, itemSet);
                }
            }
        }
        return null;
    }
}
