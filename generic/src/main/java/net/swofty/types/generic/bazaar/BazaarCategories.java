package net.swofty.types.generic.bazaar;

import lombok.Getter;
import net.minestom.server.item.Material;
import net.swofty.commons.bazaar.BazaarInitializationRequest;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.utility.StringUtility;

import java.util.*;

@Getter
public enum BazaarCategories {
    FARMING(Material.GOLDEN_HOE, Material.YELLOW_STAINED_GLASS_PANE, "§e"),
    MINING(Material.DIAMOND_PICKAXE, Material.BLUE_STAINED_GLASS_PANE,
            "§b", new BazaarItemSet(ItemType.COBBLESTONE, "Cobblestone",
            ItemType.COBBLESTONE, ItemType.ENCHANTED_COBBLESTONE),
            new BazaarItemSet(ItemType.COAL, "Coal",
                    ItemType.COAL, ItemType.ENCHANTED_COAL, ItemType.ENCHANTED_CHARCOAL, ItemType.ENCHANTED_COAL_BLOCK),
            new BazaarItemSet(ItemType.IRON_INGOT, "Iron",
                    ItemType.IRON_INGOT, ItemType.ENCHANTED_IRON_INGOT, ItemType.ENCHANTED_IRON_BLOCK),
            new BazaarItemSet(ItemType.GOLD_INGOT, "Gold",
                    ItemType.GOLD_INGOT, ItemType.ENCHANTED_GOLD_INGOT, ItemType.ENCHANTED_GOLD_BLOCK),
            new BazaarItemSet(ItemType.DIAMOND, "Diamond",
                    ItemType.DIAMOND, ItemType.ENCHANTED_DIAMOND, ItemType.ENCHANTED_DIAMOND_BLOCK),
            new BazaarItemSet(ItemType.LAPIS_LAZULI, "Lapis",
                    ItemType.LAPIS_LAZULI, ItemType.ENCHANTED_LAPIS_LAZULI, ItemType.ENCHANTED_LAPIS_LAZULI_BLOCK),
            new BazaarItemSet(ItemType.EMERALD, "Emerald",
                    ItemType.EMERALD, ItemType.ENCHANTED_EMERALD, ItemType.ENCHANTED_EMERALD_BLOCK),
            new BazaarItemSet(ItemType.REDSTONE, "Redstone",
                    ItemType.REDSTONE, ItemType.ENCHANTED_REDSTONE, ItemType.ENCHANTED_REDSTONE_BLOCK),
            new BazaarItemSet(ItemType.QUARTZ, "Quartz",
                    ItemType.QUARTZ, ItemType.ENCHANTED_QUARTZ, ItemType.ENCHANTED_QUARTZ_BLOCK),
            new BazaarItemSet(ItemType.OBSIDIAN, "Obsidian",
                    ItemType.OBSIDIAN, ItemType.ENCHANTED_OBSIDIAN),
            new BazaarItemSet(ItemType.GLOWSTONE_DUST, "Glowstone",
                    ItemType.GLOWSTONE_DUST, ItemType.ENCHANTED_GLOWSTONE_DUST, ItemType.ENCHANTED_GLOWSTONE),
            new BazaarItemSet(ItemType.FLINT, "Flint & Gravel",
                    ItemType.FLINT, ItemType.ENCHANTED_FLINT, ItemType.GRAVEL),
            new BazaarItemSet(ItemType.HARD_STONE, "Hard Stone",
                    ItemType.HARD_STONE, ItemType.ENCHANTED_HARD_STONE, ItemType.CONCENTRATED_STONE),
            new BazaarItemSet(ItemType.ICE, "Ice",
                    ItemType.ICE, ItemType.PACKED_ICE, ItemType.ENCHANTED_ICE, ItemType.ENCHANTED_PACKED_ICE, ItemType.GLACIAL_FRAGMENT),
            new BazaarItemSet(ItemType.NETHERRACK, "Netherrack",
                    ItemType.NETHERRACK, ItemType.ENCHANTED_NETHERRACK),
            new BazaarItemSet(ItemType.SAND, "Sand",
                    ItemType.SAND, ItemType.ENCHANTED_SAND, ItemType.RED_SAND, ItemType.ENCHANTED_RED_SAND,ItemType.ENCHANTED_RED_SAND_CUBE),
            new BazaarItemSet(ItemType.END_STONE, "End Stone",
                    ItemType.END_STONE, ItemType.ENCHANTED_ENDSTONE),
            new BazaarItemSet(ItemType.SNOW, "Snow",
                    ItemType.SNOW, ItemType.SNOW_BLOCK, ItemType.ENCHANTED_SNOW_BLOCK),
            new BazaarItemSet(ItemType.SULPHUR, "Sulphur",
                    ItemType.SULPHUR, ItemType.ENCHANTED_SULPHUR, ItemType.ENCHANTED_SULPHUR_CUBE),
            new BazaarItemSet(ItemType.MYCELIUM, "Mycelium",
                    ItemType.MYCELIUM, ItemType.ENCHANTED_MYCELIUM, ItemType.ENCHANTED_MYCELIUM_CUBE),
            new BazaarItemSet(ItemType.LAPIS_LAZULI, "Dwarven Materials",
                    ItemType.MITHRIL, ItemType.ENCHANTED_MITHRIL, ItemType.REFINED_MITHRIL, ItemType.TITANIUM, ItemType.ENCHANTED_TITANIUM, ItemType.REFINED_TITANIUM, ItemType.ENCHANTED_MITHRIL, ItemType.STARFALL, ItemType.TREASURITE, ItemType.SORROW)
    ),
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

    @Override
    public String toString() {
        return StringUtility.toNormalCase(name());
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
