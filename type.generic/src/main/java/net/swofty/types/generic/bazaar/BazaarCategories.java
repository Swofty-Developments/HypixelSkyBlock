package net.swofty.types.generic.bazaar;

import lombok.Getter;
import net.minestom.server.item.Material;
import net.swofty.commons.bazaar.BazaarInitializationRequest;
import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.commons.StringUtility;

import java.util.*;

@Getter
public enum BazaarCategories {
    FARMING(Material.GOLDEN_HOE, Material.YELLOW_STAINED_GLASS_PANE,
            "§e", new BazaarItemSet(ItemTypeLinker.WHEAT, "Wheat & Seeds",
            ItemTypeLinker.WHEAT, ItemTypeLinker.ENCHANTED_BREAD, ItemTypeLinker.HAY_BALE, ItemTypeLinker.ENCHANTED_HAY_BALE, ItemTypeLinker.WHEAT_SEEDS, ItemTypeLinker.ENCHANTED_SEEDS),
            new BazaarItemSet(ItemTypeLinker.CARROT, "Carrot",
                    ItemTypeLinker.CARROT, ItemTypeLinker.ENCHANTED_CARROT, ItemTypeLinker.ENCHANTED_GOLDEN_CARROT),
            new BazaarItemSet(ItemTypeLinker.POTATO, "Potato",
                    ItemTypeLinker.POTATO, ItemTypeLinker.POISONOUS_POTATO, ItemTypeLinker.ENCHANTED_POTATO, ItemTypeLinker.ENCHANTED_POISONOUS_POTATO, ItemTypeLinker.ENCHANTED_BAKED_POTATO),
            new BazaarItemSet(ItemTypeLinker.PUMPKIN, "Pumpkin",
                    ItemTypeLinker.PUMPKIN, ItemTypeLinker.ENCHANTED_PUMPKIN, ItemTypeLinker.POLISHED_PUMPKIN),
            new BazaarItemSet(ItemTypeLinker.MELON_SLICE, "Melon",
                    ItemTypeLinker.MELON_SLICE, ItemTypeLinker.ENCHANTED_GLISTERING_MELON, ItemTypeLinker.ENCHANTED_MELON, ItemTypeLinker.ENCHANTED_MELON_BLOCK),
            new BazaarItemSet(ItemTypeLinker.RED_MUSHROOM, "Mushrooms",
                    ItemTypeLinker.RED_MUSHROOM, ItemTypeLinker.BROWN_MUSHROOM, ItemTypeLinker.RED_MUSHROOM_BLOCK, ItemTypeLinker.BROWN_MUSHROOM_BLOCK, ItemTypeLinker.ENCHANTED_RED_MUSHROOM_BLOCK, ItemTypeLinker.ENCHANTED_BROWN_MUSHROOM_BLOCK),
            new BazaarItemSet(ItemTypeLinker.COCOA_BEANS, "Cocoa Beans",
                    ItemTypeLinker.COCOA_BEANS, ItemTypeLinker.ENCHANTED_COCOA_BEANS, ItemTypeLinker.ENCHANTED_COOKIE),
            new BazaarItemSet(ItemTypeLinker.CACTUS_GREEN, "Cactus",
                    ItemTypeLinker.CACTUS, ItemTypeLinker.ENCHANTED_CACTUS_GREEN, ItemTypeLinker.ENCHANTED_CACTUS),
            new BazaarItemSet(ItemTypeLinker.SUGAR_CANE, "Sugar Cane",
                    ItemTypeLinker.SUGAR_CANE, ItemTypeLinker.ENCHANTED_SUGAR, ItemTypeLinker.ENCHANTED_PAPER, ItemTypeLinker.ENCHANTED_SUGAR_CANE),
            new BazaarItemSet(ItemTypeLinker.LEATHER, "Leather & Beef",
                    ItemTypeLinker.LEATHER, ItemTypeLinker.ENCHANTED_LEATHER, ItemTypeLinker.BEEF, ItemTypeLinker.ENCHANTED_RAW_BEEF),
            new BazaarItemSet(ItemTypeLinker.PORKCHOP, "Pork",
                    ItemTypeLinker.PORKCHOP, ItemTypeLinker.ENCHANTED_PORK, ItemTypeLinker.ENCHANTED_GRILLED_PORK),
            new BazaarItemSet(ItemTypeLinker.CHICKEN, "Chicker & Feather",
                    ItemTypeLinker.CHICKEN, ItemTypeLinker.ENCHANTED_RAW_CHICKEN, ItemTypeLinker.FEATHER, ItemTypeLinker.ENCHANTED_FEATHER, ItemTypeLinker.ENCHANTED_EGG, ItemTypeLinker.OMEGA_ENCHANTED_EGG, ItemTypeLinker.SUPER_ENCHANTED_EGG),
            new BazaarItemSet(ItemTypeLinker.MUTTON, "Mutton",
                    ItemTypeLinker.MUTTON, ItemTypeLinker.ENCHANTED_MUTTON, ItemTypeLinker.ENCHANTED_COOKED_MUTTON),
            new BazaarItemSet(ItemTypeLinker.RABBIT, "Rabbit",
                    ItemTypeLinker.RABBIT, ItemTypeLinker.RABBIT_FOOT, ItemTypeLinker.RABBIT_HIDE, ItemTypeLinker.ENCHANTED_RAW_RABBIT, ItemTypeLinker.ENCHANTED_RABBIT_FOOT, ItemTypeLinker.ENCHANTED_RABBIT_HIDE),
            new BazaarItemSet(ItemTypeLinker.NETHER_WART, "Nether Warts",
                    ItemTypeLinker.NETHER_WART, ItemTypeLinker.ENCHANTED_NETHER_WART)

    ),
    MINING(Material.DIAMOND_PICKAXE, Material.BLUE_STAINED_GLASS_PANE,
            "§b", new BazaarItemSet(ItemTypeLinker.COBBLESTONE, "Cobblestone",
            ItemTypeLinker.COBBLESTONE, ItemTypeLinker.ENCHANTED_COBBLESTONE),
            new BazaarItemSet(ItemTypeLinker.COAL, "Coal",
                    ItemTypeLinker.COAL, ItemTypeLinker.ENCHANTED_COAL, ItemTypeLinker.ENCHANTED_CHARCOAL, ItemTypeLinker.ENCHANTED_COAL_BLOCK),
            new BazaarItemSet(ItemTypeLinker.IRON_INGOT, "Iron",
                    ItemTypeLinker.IRON_INGOT, ItemTypeLinker.ENCHANTED_IRON_INGOT, ItemTypeLinker.ENCHANTED_IRON_BLOCK),
            new BazaarItemSet(ItemTypeLinker.GOLD_INGOT, "Gold",
                    ItemTypeLinker.GOLD_INGOT, ItemTypeLinker.ENCHANTED_GOLD_INGOT, ItemTypeLinker.ENCHANTED_GOLD_BLOCK),
            new BazaarItemSet(ItemTypeLinker.DIAMOND, "Diamond",
                    ItemTypeLinker.DIAMOND, ItemTypeLinker.ENCHANTED_DIAMOND, ItemTypeLinker.ENCHANTED_DIAMOND_BLOCK),
            new BazaarItemSet(ItemTypeLinker.LAPIS_LAZULI, "Lapis",
                    ItemTypeLinker.LAPIS_LAZULI, ItemTypeLinker.ENCHANTED_LAPIS_LAZULI, ItemTypeLinker.ENCHANTED_LAPIS_LAZULI_BLOCK),
            new BazaarItemSet(ItemTypeLinker.EMERALD, "Emerald",
                    ItemTypeLinker.EMERALD, ItemTypeLinker.ENCHANTED_EMERALD, ItemTypeLinker.ENCHANTED_EMERALD_BLOCK),
            new BazaarItemSet(ItemTypeLinker.REDSTONE, "Redstone",
                    ItemTypeLinker.REDSTONE, ItemTypeLinker.ENCHANTED_REDSTONE, ItemTypeLinker.ENCHANTED_REDSTONE_BLOCK),
            new BazaarItemSet(ItemTypeLinker.QUARTZ, "Quartz",
                    ItemTypeLinker.QUARTZ, ItemTypeLinker.ENCHANTED_QUARTZ, ItemTypeLinker.ENCHANTED_QUARTZ_BLOCK),
            new BazaarItemSet(ItemTypeLinker.OBSIDIAN, "Obsidian",
                    ItemTypeLinker.OBSIDIAN, ItemTypeLinker.ENCHANTED_OBSIDIAN),
            new BazaarItemSet(ItemTypeLinker.GLOWSTONE_DUST, "Glowstone",
                    ItemTypeLinker.GLOWSTONE_DUST, ItemTypeLinker.ENCHANTED_GLOWSTONE_DUST, ItemTypeLinker.ENCHANTED_GLOWSTONE),
            new BazaarItemSet(ItemTypeLinker.FLINT, "Flint & Gravel",
                    ItemTypeLinker.FLINT, ItemTypeLinker.ENCHANTED_FLINT, ItemTypeLinker.GRAVEL),
            new BazaarItemSet(ItemTypeLinker.HARD_STONE, "Hard Stone",
                    ItemTypeLinker.HARD_STONE, ItemTypeLinker.ENCHANTED_HARD_STONE, ItemTypeLinker.CONCENTRATED_STONE),
            new BazaarItemSet(ItemTypeLinker.ICE, "Ice",
                    ItemTypeLinker.ICE, ItemTypeLinker.PACKED_ICE, ItemTypeLinker.ENCHANTED_ICE, ItemTypeLinker.ENCHANTED_PACKED_ICE, ItemTypeLinker.GLACIAL_FRAGMENT),
            new BazaarItemSet(ItemTypeLinker.NETHERRACK, "Netherrack",
                    ItemTypeLinker.NETHERRACK, ItemTypeLinker.ENCHANTED_NETHERRACK),
            new BazaarItemSet(ItemTypeLinker.SAND, "Sand",
                    ItemTypeLinker.SAND, ItemTypeLinker.ENCHANTED_SAND, ItemTypeLinker.RED_SAND, ItemTypeLinker.ENCHANTED_RED_SAND, ItemTypeLinker.ENCHANTED_RED_SAND_CUBE),
            new BazaarItemSet(ItemTypeLinker.END_STONE, "End Stone",
                    ItemTypeLinker.END_STONE, ItemTypeLinker.ENCHANTED_ENDSTONE),
            new BazaarItemSet(ItemTypeLinker.SNOW, "Snow",
                    ItemTypeLinker.SNOW, ItemTypeLinker.SNOW_BLOCK, ItemTypeLinker.ENCHANTED_SNOW_BLOCK),
            new BazaarItemSet(ItemTypeLinker.SULPHUR, "Sulphur",
                    ItemTypeLinker.SULPHUR, ItemTypeLinker.ENCHANTED_SULPHUR, ItemTypeLinker.ENCHANTED_SULPHUR_CUBE),
            new BazaarItemSet(ItemTypeLinker.MYCELIUM, "Mycelium",
                    ItemTypeLinker.MYCELIUM, ItemTypeLinker.ENCHANTED_MYCELIUM, ItemTypeLinker.ENCHANTED_MYCELIUM_CUBE),
            new BazaarItemSet(ItemTypeLinker.LAPIS_LAZULI, "Dwarven Materials",
                    ItemTypeLinker.MITHRIL, ItemTypeLinker.ENCHANTED_MITHRIL, ItemTypeLinker.REFINED_MITHRIL, ItemTypeLinker.TITANIUM, ItemTypeLinker.ENCHANTED_TITANIUM, ItemTypeLinker.REFINED_TITANIUM, ItemTypeLinker.ENCHANTED_MITHRIL, ItemTypeLinker.STARFALL, ItemTypeLinker.TREASURITE, ItemTypeLinker.SORROW)
    ),
    COMBAT(Material.IRON_SWORD, Material.RED_STAINED_GLASS_PANE,
            "§c",
            new BazaarItemSet(ItemTypeLinker.ROTTEN_FLESH, "Rotten Flesh",
                    ItemTypeLinker.ROTTEN_FLESH, ItemTypeLinker.ENCHANTED_ROTTEN_FLESH),
            new BazaarItemSet(ItemTypeLinker.BONE, "Bone",
                    ItemTypeLinker.BONE, ItemTypeLinker.ENCHANTED_BONE, ItemTypeLinker.ENCHANTED_BONE_BLOCK, ItemTypeLinker.ENCHANTED_BONE_MEAL),
            new BazaarItemSet(ItemTypeLinker.STRING, "Arachnids",
                    ItemTypeLinker.STRING, ItemTypeLinker.ENCHANTED_STRING, ItemTypeLinker.SPIDER_EYE, ItemTypeLinker.ENCHANTED_SPIDER_EYE, ItemTypeLinker.ENCHANTED_FERMENTED_SPIDER_EYE, ItemTypeLinker.SOUL_STRING),
            new BazaarItemSet(ItemTypeLinker.GUNPOWDER, "Gunpowder",
                    ItemTypeLinker.GUNPOWDER, ItemTypeLinker.ENCHANTED_GUNPOWDER, ItemTypeLinker.ENCHANTED_FIREWORK_ROCKET),
            new BazaarItemSet(ItemTypeLinker.ENDER_PEARL, "Ender Pearl",
                    ItemTypeLinker.ENDER_PEARL, ItemTypeLinker.ENCHANTED_ENDER_PEARL, ItemTypeLinker.ENCHANTED_EYE_OF_ENDER),
            new BazaarItemSet(ItemTypeLinker.GHAST_TEAR, "Ghast Tear",
                    ItemTypeLinker.GHAST_TEAR, ItemTypeLinker.ENCHANTED_GHAST_TEAR),
            new BazaarItemSet(ItemTypeLinker.SLIME_BALL, "Slime Drops",
                    ItemTypeLinker.SLIME_BALL, ItemTypeLinker.ENCHANTED_SLIMEBALL, ItemTypeLinker.ENCHANTED_SLIME_BLOCK, ItemTypeLinker.MAGMA_CREAM, ItemTypeLinker.ENCHANTED_MAGMA_CREAM, ItemTypeLinker.WHIPPED_MAGMA_CREAM),
            new BazaarItemSet(ItemTypeLinker.BLAZE_ROD, "Blaze Rod",
                    ItemTypeLinker.BLAZE_ROD, ItemTypeLinker.ENCHANTED_BLAZE_POWDER, ItemTypeLinker.ENCHANTED_BLAZE_ROD),
            new BazaarItemSet(ItemTypeLinker.FEATHER, "Mythological",
                    ItemTypeLinker.GRIFFIN_FEATHER, ItemTypeLinker.DAEDALUS_STICK, ItemTypeLinker.ANCIENT_CLAW, ItemTypeLinker.ENCHANTED_ANCIENT_CLAW)
    ),
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
        List<ItemTypeLinker> items = new ArrayList<>();
        for (BazaarCategories category : BazaarCategories.values()) {
            for (BazaarItemSet itemSet : category.items) {
                items.addAll(itemSet.items);
            }
        }

        return new BazaarInitializationRequest(items.stream().map(ItemTypeLinker::name).toList());
    }

    public static Map.Entry<BazaarCategories, BazaarItemSet> getFromItem(ItemTypeLinker itemTypeLinker) {
        for (BazaarCategories category : BazaarCategories.values()) {
            for (BazaarItemSet itemSet : category.items) {
                if (itemSet.items.contains(itemTypeLinker)) {
                    return new AbstractMap.SimpleEntry<>(category, itemSet);
                }
            }
        }
        return null;
    }
}
