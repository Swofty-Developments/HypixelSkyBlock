package net.swofty.types.generic.region.mining;

import lombok.Getter;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;

@Getter
public enum MineableBlock {
    // Default mineable blocks
    ANDESITE(Material.ANDESITE, 15, 1, MiningBlockClassification.defaultBlock()),
    GRANITE(Material.GRANITE, 15, 1, MiningBlockClassification.defaultBlock()),
    DIORITE(Material.DIORITE, 15, 1, MiningBlockClassification.defaultBlock()),
    STONE(Material.STONE, 15, 1, MiningBlockClassification.blockBlock()),
    SAND(Material.SAND, 5, 1, MiningBlockClassification.blockBlock()),
    NETHERRACK(Material.NETHERRACK, 4, 1, MiningBlockClassification.blockBlock()),
    COBBLESTONE(Material.COBBLESTONE, 20, 1, MiningBlockClassification.blockBlock()),
    END_STONE(Material.END_STONE, 30, 1, MiningBlockClassification.blockBlock()),
    OBSIDIAN(Material.END_STONE, 500, 4, MiningBlockClassification.blockBlock()),
    ORE_IRON(Material.IRON_ORE, 30, 2, MiningBlockClassification.oreBlock()),
    ORE_COAL(Material.COAL_ORE, 30, 1, MiningBlockClassification.oreBlock()),
    ORE_GOLD(Material.GOLD_ORE, 30, 3, MiningBlockClassification.oreBlock()),
    ORE_DIAMOND(Material.DIAMOND_ORE, 30, 3, MiningBlockClassification.oreBlock()),
    ORE_REDSTONE(Material.REDSTONE_ORE, 30, 3, MiningBlockClassification.oreBlock()),

    // Forest mineable blocks
    ACACIA_LOG(Material.ACACIA_LOG, 1, 0, MiningBlockClassification.foragingBlock()),
    BIRCH_LOG(Material.BIRCH_LOG, 1, 0, MiningBlockClassification.foragingBlock()),
    JUNGLE_LOG(Material.JUNGLE_LOG, 1, 0, MiningBlockClassification.foragingBlock()),
    SPRUCE_LOG(Material.SPRUCE_LOG, 1, 0, MiningBlockClassification.foragingBlock()),
    DARK_OAK_LOG(Material.DARK_OAK_LOG, 1, 0, MiningBlockClassification.foragingBlock()),
    MANGROVE_LOG(Material.MANGROVE_LOG, 1, 0, MiningBlockClassification.foragingBlock()),
    OAK_LOG(Material.OAK_LOG, 1, 0, MiningBlockClassification.foragingBlock()),

    // Farming mineable blocks
    WHEAT(Material.WHEAT, 0, 0, MiningBlockClassification.wheatBlock()),
    SUGAR_CANE(Material.SUGAR_CANE, 0, 0, MiningBlockClassification.sugarCaneBlock()),
    CACTUS(Material.CACTUS, 0, 0, MiningBlockClassification.cactusBlock()),
    BROWN_MUSHROOM(Material.BROWN_MUSHROOM, 0, 0, MiningBlockClassification.mushroomBlock()),
    RED_MUSHROOM(Material.RED_MUSHROOM, 0, 0, MiningBlockClassification.mushroomBlock()),
    POTATO(Material.POTATO, 0, 0, MiningBlockClassification.potatoBlock()),
    CARROT(Material.CARROT, 0, 0, MiningBlockClassification.carrotBlock()),
    NETHER_WART(Material.NETHER_WART, 0, 0, MiningBlockClassification.netherWartBlock()),
    PUMPKIN(Material.PUMPKIN, 1, 0, MiningBlockClassification.pumpkinBlock()),
    CARVED_PUMPKIN(Material.CARVED_PUMPKIN, 1, 0, MiningBlockClassification.pumpkinBlock()),
    MELON(Material.MELON, 1, 0, MiningBlockClassification.melonBlock()),

    // Dwarven mines mineable blocks
    MITHRIL_WEAK(Material.GRAY_WOOL, 500, 4, MiningBlockClassification.dwarvenMetalBlock()),
    MITHRIL_WEAK2(Material.CYAN_TERRACOTTA, 500, 4, MiningBlockClassification.dwarvenMetalBlock()),
    MITHRIL_MEDIUM(Material.PRISMARINE, 800, 4, MiningBlockClassification.dwarvenMetalBlock()),
    MITHRIL_STRONG(Material.LIGHT_BLUE_WOOL, 1500, 4, MiningBlockClassification.dwarvenMetalBlock()),
    TITANIUM(Material.DIORITE, 2500, 5, MiningBlockClassification.dwarvenMetalBlock()),
    DWARVEN_GOLD(Material.GOLD_BLOCK, 600, 3, MiningBlockClassification.oreBlock()),
    ;

    private final Material material;
    private final double strength;
    private final int miningPowerRequirement;
    private final MiningBlockClassification blockType;

    MineableBlock(Material material, double strength, int miningPowerRequirement, MiningBlockClassification blockType) {
        this.material = material;
        this.strength = strength;
        this.miningPowerRequirement = miningPowerRequirement;
        this.blockType = blockType;
    }

    public static MineableBlock get(Block block) {
        for (MineableBlock mineableBlock : values()) {
            if (block.key().equals(mineableBlock.getMaterial().key()))
                return mineableBlock;
        }
        return null;
    }
}