package net.swofty.types.generic.region.mining;

import lombok.Getter;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.utility.MaterialQuantifiableRandom;

@Getter
public enum MineableBlock {
    // default minable blocks
    STONE(Material.STONE, 15, 1, MiningLoot.defaultLoot(), ItemStatistic.MINING_FORTUNE),
    SAND(Material.SAND, 5, 1, MiningLoot.defaultLoot(), ItemStatistic.MINING_FORTUNE),
    NETHERRACK(Material.NETHERRACK, 4, 1, MiningLoot.defaultLoot(), ItemStatistic.MINING_FORTUNE),
    COBBLESTONE(Material.COBBLESTONE, 20, 1, MiningLoot.defaultLoot(), ItemStatistic.MINING_FORTUNE),
    ANDESITE(Material.ANDESITE, 15, 1, MiningLoot.defaultLoot(), ItemStatistic.MINING_FORTUNE),
    GRANITE(Material.GRANITE, 15, 1, MiningLoot.defaultLoot(), ItemStatistic.MINING_FORTUNE),
    DIORITE(Material.DIORITE, 15, 1, MiningLoot.defaultLoot(), ItemStatistic.MINING_FORTUNE),
    END_STONE(Material.END_STONE, 30, 1, MiningLoot.defaultLoot(), ItemStatistic.MINING_FORTUNE),
    OBSIDIAN(Material.END_STONE, 500, 4, MiningLoot.defaultLoot(), ItemStatistic.MINING_FORTUNE),
    ORE_IRON(Material.IRON_ORE, 30, 2, MiningLoot.defaultLoot(), ItemStatistic.MINING_FORTUNE),
    ORE_COAL(Material.COAL_ORE, 30, 1, MiningLoot.defaultLoot(), ItemStatistic.MINING_FORTUNE),
    ORE_GOLD(Material.GOLD_ORE, 30, 3, MiningLoot.defaultLoot(), ItemStatistic.MINING_FORTUNE),
    ORE_DIAMOND(Material.DIAMOND_ORE, 30, 3, MiningLoot.defaultLoot(), ItemStatistic.MINING_FORTUNE),
    ORE_REDSTONE(Material.REDSTONE_ORE, 30, 3, MiningLoot.defaultLoot(), ItemStatistic.MINING_FORTUNE),

    // Forest minable blocks
    ACACIA_LOG(Material.ACACIA_LOG, 1, 0, MiningLoot.defaultLoot(), ItemStatistic.FORAGING_FORTUNE),
    BIRCH_LOG(Material.BIRCH_LOG, 1, 0, MiningLoot.defaultLoot(), ItemStatistic.FORAGING_FORTUNE),
    JUNGLE_LOG(Material.JUNGLE_LOG, 1, 0, MiningLoot.defaultLoot(), ItemStatistic.FORAGING_FORTUNE),
    SPRUCE_LOG(Material.SPRUCE_LOG, 1, 0, MiningLoot.defaultLoot(), ItemStatistic.FORAGING_FORTUNE),
    DARK_OAK_LOG(Material.DARK_OAK_LOG, 1, 0, MiningLoot.defaultLoot(), ItemStatistic.FORAGING_FORTUNE),
    MANGROVE_LOG(Material.MANGROVE_LOG, 1, 0, MiningLoot.defaultLoot(), ItemStatistic.FORAGING_FORTUNE),
    OAK_LOG(Material.OAK_LOG, 1, 0, MiningLoot.defaultLoot(), ItemStatistic.FORAGING_FORTUNE),

    // Farming minable blocks
    WHEAT(Material.WHEAT, 0, 0, MiningLoot.defaultLoot(), ItemStatistic.FARMING_FORTUNE),
    SUGAR_CANE(Material.SUGAR_CANE, 0, 0, MiningLoot.defaultLoot(), ItemStatistic.FARMING_FORTUNE),
    CACTUS(Material.CACTUS, 0, 0, MiningLoot.defaultLoot(), ItemStatistic.FARMING_FORTUNE),
    BROWN_MUSHROOM(Material.BROWN_MUSHROOM, 0, 0, MiningLoot.defaultLoot(), ItemStatistic.FARMING_FORTUNE),
    RED_MUSHROOM(Material.RED_MUSHROOM, 0, 0, MiningLoot.defaultLoot(), ItemStatistic.FARMING_FORTUNE),
    POTATO(Material.POTATO, 0, 0, MiningLoot.custom(Material.POTATO, 2, 5), ItemStatistic.FARMING_FORTUNE),
    CARROT(Material.CARROT, 0, 0, MiningLoot.custom(Material.CARROT, 2, 5), ItemStatistic.FARMING_FORTUNE),
    NETHER_WART(Material.NETHER_WART, 0, 0, MiningLoot.custom(Material.NETHER_WART, 2, 4), ItemStatistic.FARMING_FORTUNE),
    PUMPKIN(Material.PUMPKIN, 1, 0, MiningLoot.defaultLoot(), ItemStatistic.FARMING_FORTUNE),
    CARVED_PUMPKIN(Material.CARVED_PUMPKIN, 1, 0, MiningLoot.defaultLoot(), ItemStatistic.FARMING_FORTUNE),
    MELON(Material.MELON, 1, 0, MiningLoot.custom(Material.MELON_SLICE, 3, 7), ItemStatistic.FARMING_FORTUNE),

    // Dwarven mines minable blocks
    MITHRIL_WEAK(Material.GRAY_WOOL, 500, 4, MiningLoot.none(), ItemStatistic.MINING_FORTUNE),
    MITHRIL_WEAK2(Material.CYAN_TERRACOTTA, 500, 4, MiningLoot.none(), ItemStatistic.MINING_FORTUNE),
    MITHRIL_MEDIUM(Material.PRISMARINE, 800, 4, MiningLoot.none(), ItemStatistic.MINING_FORTUNE),
    MITHRIL_STRONG(Material.LIGHT_BLUE_WOOL, 1500, 4, MiningLoot.none(), ItemStatistic.MINING_FORTUNE),
    TITANIUM(Material.DIORITE, 2500, 5, MiningLoot.none(), ItemStatistic.MINING_FORTUNE),
    DWARVEN_GOLD(Material.GOLD_BLOCK, 600, 3, MiningLoot.custom(Material.GOLD_INGOT, 2, 4), ItemStatistic.MINING_FORTUNE),
    ;

    private final Material material;
    private final double strength;
    private final int miningPowerRequirement;
    private final MaterialQuantifiableRandom drops;
    private final ItemStatistic fortuneType;

    MineableBlock(Material material, double strength, int miningPowerRequirement, MiningLoot drops, ItemStatistic fortuneType) {
        this.material = material;
        this.strength = strength;
        this.miningPowerRequirement = miningPowerRequirement;
        this.drops = drops.identifier().equals("default") ?
                new MaterialQuantifiableRandom(new SkyBlockItem(material), 1, 1) :
                drops.material();
        this.fortuneType = fortuneType;
    }

    public static MineableBlock get(Block block) {
        for (MineableBlock mineableBlock : values()) {
            if (block.namespace().equals(mineableBlock.getMaterial().namespace()))
                return mineableBlock;
        }
        return null;
    }
}
