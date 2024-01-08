package net.swofty.types.generic.region.mining;

import lombok.Getter;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.utility.MaterialQuantifiableRandom;

@Getter
public enum MineableBlock {
    // Default minable blocks
    STONE(Material.STONE, 15, 1, MiningLoot.Default()),
    SAND(Material.SAND, 5, 1, MiningLoot.Default()),
    NETHERRACK(Material.NETHERRACK, 4, 1, MiningLoot.Default()),
    COBBLESTONE(Material.COBBLESTONE, 20, 1, MiningLoot.Default()),
    ANDESITE(Material.ANDESITE, 15, 1, MiningLoot.Default()),
    GRANITE(Material.GRANITE, 15, 1, MiningLoot.Default()),
    DIORITE(Material.DIORITE, 15, 1, MiningLoot.Default()),
    END_STONE(Material.END_STONE, 30, 1, MiningLoot.Default()),
    OBSIDIAN(Material.END_STONE, 500, 4, MiningLoot.Default()),
    ORE_IRON(Material.IRON_ORE, 30, 2, MiningLoot.Default()),
    ORE_COAL(Material.COAL_ORE, 30, 1, MiningLoot.Default()),
    ORE_GOLD(Material.GOLD_ORE, 30, 3, MiningLoot.Default()),
    ORE_DIAMOND(Material.DIAMOND_ORE, 30, 3, MiningLoot.Default()),
    ORE_REDSTONE(Material.REDSTONE_ORE, 30, 3, MiningLoot.Default()),

    // Forest minable blocks
    ACACIA_LOG(Material.ACACIA_LOG, 1, 0, MiningLoot.Default()),
    BIRCH_LOG(Material.BIRCH_LOG, 1, 0, MiningLoot.Default()),
    JUNGLE_LOG(Material.JUNGLE_LOG, 1, 0, MiningLoot.Default()),
    SPRUCE_LOG(Material.SPRUCE_LOG, 1, 0, MiningLoot.Default()),
    DARK_OAK_LOG(Material.DARK_OAK_LOG, 1, 0, MiningLoot.Default()),
    MANGROVE_LOG(Material.MANGROVE_LOG, 1, 0, MiningLoot.Default()),
    OAK_LOG(Material.OAK_LOG, 1, 0, MiningLoot.Default()),

    // Farming minable blocks
    PUMPKIN(Material.PUMPKIN, 1, 0, MiningLoot.Default()),
    CARVED_PUMPKIN(Material.CARVED_PUMPKIN, 1, 0, MiningLoot.Default()),
    MELON(Material.MELON, 1, 0, MiningLoot.Default()),

    // Dwarven mines minable blocks
    MITHRIL_WEAK(Material.GRAY_WOOL, 500, 4, MiningLoot.None()),
    MITHRIL_WEAK2(Material.CYAN_TERRACOTTA, 500, 4, MiningLoot.None()),
    MITHRIL_MEDIUM(Material.PRISMARINE, 800, 4, MiningLoot.None()),
    MITHRIL_STRONG(Material.LIGHT_BLUE_WOOL, 1500, 4, MiningLoot.None()),
    TITANIUM(Material.DIORITE, 2500, 5, MiningLoot.None()),
    DWARVEN_GOLD(Material.GOLD_BLOCK, 600, 3, MiningLoot.Custom(Material.GOLD_INGOT, 2, 4)),
    ;

    private final Material material;
    private final double strength;
    private final int miningPowerRequirement;
    private final MaterialQuantifiableRandom drops;

    MineableBlock(Material material, double strength, int miningPowerRequirement, MiningLoot drops) {
        this.material = material;
        this.strength = strength;
        this.miningPowerRequirement = miningPowerRequirement;
        this.drops = drops.identifier().equals("default") ?
                new MaterialQuantifiableRandom(new SkyBlockItem(material), 1, 1) :
                drops.material();
    }

    public static MineableBlock get(Block block) {
        for (MineableBlock mineableBlock : values()) {
            if (block.namespace().equals(mineableBlock.getMaterial().namespace()))
                return mineableBlock;
        }
        return null;
    }
}
