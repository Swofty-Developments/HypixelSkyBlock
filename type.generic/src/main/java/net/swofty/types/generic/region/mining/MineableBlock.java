package net.swofty.types.generic.region.mining;

import lombok.Getter;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.utility.MaterialQuantifiableRandom;

@Getter
public enum MineableBlock {
    // default mineable blocks
    ANDESITE(Material.ANDESITE, 15, 1, MiningLoot.defaultLoot(), MiningBlockClassification.defaultBlock()),
    GRANITE(Material.GRANITE, 15, 1, MiningLoot.defaultLoot(), MiningBlockClassification.defaultBlock()),
    DIORITE(Material.DIORITE, 15, 1, MiningLoot.defaultLoot(), MiningBlockClassification.defaultBlock()),
    STONE(Material.STONE, 15, 1, MiningLoot.defaultLoot(), MiningBlockClassification.blockBlock()),
    SAND(Material.SAND, 5, 1, MiningLoot.defaultLoot(), MiningBlockClassification.blockBlock()),
    NETHERRACK(Material.NETHERRACK, 4, 1, MiningLoot.defaultLoot(), MiningBlockClassification.blockBlock()),
    COBBLESTONE(Material.COBBLESTONE, 20, 1, MiningLoot.defaultLoot(), MiningBlockClassification.blockBlock()),
    END_STONE(Material.END_STONE, 30, 1, MiningLoot.defaultLoot(), MiningBlockClassification.blockBlock()),
    OBSIDIAN(Material.END_STONE, 500, 4, MiningLoot.defaultLoot(), MiningBlockClassification.blockBlock()),
    ORE_IRON(Material.IRON_ORE, 30, 2, MiningLoot.defaultLoot(), MiningBlockClassification.oreBlock()),
    ORE_COAL(Material.COAL_ORE, 30, 1, MiningLoot.defaultLoot(), MiningBlockClassification.oreBlock()),
    ORE_GOLD(Material.GOLD_ORE, 30, 3, MiningLoot.defaultLoot(), MiningBlockClassification.oreBlock()),
    ORE_DIAMOND(Material.DIAMOND_ORE, 30, 3, MiningLoot.defaultLoot(), MiningBlockClassification.oreBlock()),
    ORE_REDSTONE(Material.REDSTONE_ORE, 30, 3, MiningLoot.defaultLoot(), MiningBlockClassification.oreBlock()),

    // Forest mineable blocks
    ACACIA_LOG(Material.ACACIA_LOG, 1, 0, MiningLoot.defaultLoot(), MiningBlockClassification.foragingBlock()),
    BIRCH_LOG(Material.BIRCH_LOG, 1, 0, MiningLoot.defaultLoot(), MiningBlockClassification.foragingBlock()),
    JUNGLE_LOG(Material.JUNGLE_LOG, 1, 0, MiningLoot.defaultLoot(), MiningBlockClassification.foragingBlock()),
    SPRUCE_LOG(Material.SPRUCE_LOG, 1, 0, MiningLoot.defaultLoot(), MiningBlockClassification.foragingBlock()),
    DARK_OAK_LOG(Material.DARK_OAK_LOG, 1, 0, MiningLoot.defaultLoot(), MiningBlockClassification.foragingBlock()),
    MANGROVE_LOG(Material.MANGROVE_LOG, 1, 0, MiningLoot.defaultLoot(), MiningBlockClassification.foragingBlock()),
    OAK_LOG(Material.OAK_LOG, 1, 0, MiningLoot.defaultLoot(), MiningBlockClassification.foragingBlock()),

    // Farming mineable blocks
    WHEAT(Material.WHEAT, 0, 0, MiningLoot.defaultLoot(), MiningBlockClassification.wheatBlock()),
    SUGAR_CANE(Material.SUGAR_CANE, 0, 0, MiningLoot.defaultLoot(), MiningBlockClassification.sugarCaneBlock()),
    CACTUS(Material.CACTUS, 0, 0, MiningLoot.defaultLoot(), MiningBlockClassification.cactusBlock()),
    BROWN_MUSHROOM(Material.BROWN_MUSHROOM, 0, 0, MiningLoot.defaultLoot(), MiningBlockClassification.mushroomBlock()),
    RED_MUSHROOM(Material.RED_MUSHROOM, 0, 0, MiningLoot.defaultLoot(), MiningBlockClassification.mushroomBlock()),
    POTATO(Material.POTATO, 0, 0, MiningLoot.custom(Material.POTATO, 2, 5), MiningBlockClassification.potatoBlock()),
    CARROT(Material.CARROT, 0, 0, MiningLoot.custom(Material.CARROT, 2, 5), MiningBlockClassification.carrotBlock()),
    NETHER_WART(Material.NETHER_WART, 0, 0, MiningLoot.custom(Material.NETHER_WART, 2, 4), MiningBlockClassification.netherWartBlock()),
    PUMPKIN(Material.PUMPKIN, 1, 0, MiningLoot.defaultLoot(), MiningBlockClassification.pumpkinBlock()),
    CARVED_PUMPKIN(Material.CARVED_PUMPKIN, 1, 0, MiningLoot.defaultLoot(), MiningBlockClassification.pumpkinBlock()),
    MELON(Material.MELON, 1, 0, MiningLoot.custom(Material.MELON_SLICE, 3, 7), MiningBlockClassification.melonBlock()),

    // Dwarven mines mineable blocks
    MITHRIL_WEAK(Material.GRAY_WOOL, 500, 4, MiningLoot.none(), MiningBlockClassification.dwarvenMetalBlock()),
    MITHRIL_WEAK2(Material.CYAN_TERRACOTTA, 500, 4, MiningLoot.none(), MiningBlockClassification.dwarvenMetalBlock()),
    MITHRIL_MEDIUM(Material.PRISMARINE, 800, 4, MiningLoot.none(), MiningBlockClassification.dwarvenMetalBlock()),
    MITHRIL_STRONG(Material.LIGHT_BLUE_WOOL, 1500, 4, MiningLoot.none(), MiningBlockClassification.dwarvenMetalBlock()),
    TITANIUM(Material.DIORITE, 2500, 5, MiningLoot.none(), MiningBlockClassification.dwarvenMetalBlock()),
    DWARVEN_GOLD(Material.GOLD_BLOCK, 600, 3, MiningLoot.custom(Material.GOLD_INGOT, 2, 4), MiningBlockClassification.oreBlock()),
    ;

    private final Material material;
    private final double strength;
    private final int miningPowerRequirement;
    private final MaterialQuantifiableRandom drops;
    private final MiningBlockClassification blockType;

    MineableBlock(Material material, double strength, int miningPowerRequirement, MiningLoot drops, MiningBlockClassification blockType) {
        this.material = material;
        this.strength = strength;
        this.miningPowerRequirement = miningPowerRequirement;
        this.drops = drops.identifier().equals("default") ?
                new MaterialQuantifiableRandom(new SkyBlockItem(material), 1, 1) :
                drops.material();
        this.blockType = blockType;
    }

    public static MineableBlock get(Block block) {
        for (MineableBlock mineableBlock : values()) {
            if (block.namespace().equals(mineableBlock.getMaterial().namespace()))
                return mineableBlock;
        }
        return null;
    }
}
