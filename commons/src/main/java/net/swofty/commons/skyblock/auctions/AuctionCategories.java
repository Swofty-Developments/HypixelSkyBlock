package net.swofty.commons.skyblock.auctions;

import lombok.Getter;
import net.minestom.server.item.Material;

import java.util.List;

@Getter
public enum AuctionCategories {
    WEAPONS(Material.GOLDEN_SWORD, Material.ORANGE_STAINED_GLASS_PANE, "§6", List.of("Swords", "Bows", "Axes", "Magic Weapons")),
    ARMOR(Material.DIAMOND_CHESTPLATE, Material.BLUE_STAINED_GLASS_PANE, "§b", List.of("Helmets", "Chestplates", "Leggings", "Boots")),
    ACCESSORIES(Material.SKELETON_SKULL, Material.GREEN_STAINED_GLASS_PANE, "§a", List.of("Rings", "Necklaces", "Talismans", "Artifacts")),
    CONSUMABLES(Material.APPLE, Material.RED_STAINED_GLASS_PANE, "§c", List.of("Potions", "Food", "Enchantments", "Books")),
    BLOCKS(Material.COBBLESTONE, Material.BROWN_STAINED_GLASS_PANE, "§e", List.of("Building Blocks", "Decoration Blocks", "Redstone", "Transportation")),
    TOOLS(Material.STICK, Material.PURPLE_STAINED_GLASS_PANE, "§d", List.of("Tools", "Specials", "Magic")),
    ;

    private final Material displayMaterial;
    private final Material material;
    private final String color;
    private final List<String> examples;

    AuctionCategories(Material displayMaterial, Material material, String color, List<String> examples) {
        this.displayMaterial = displayMaterial;
        this.material = material;
        this.color = color;
        this.examples = examples;
    }
}
