package net.swofty.type.skyblockgeneric.region.mining;

import lombok.Getter;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.type.skyblockgeneric.region.mining.handler.MiningHandlers;
import net.swofty.type.skyblockgeneric.region.mining.handler.SkyBlockMiningHandler;

@Getter
public enum MineableBlock {
    // Default mineable blocks (pickaxe)
    ANDESITE(Material.ANDESITE, MiningHandlers.pickaxe(15, 1), MiningBlockClassification.defaultBlock()),
    GRANITE(Material.GRANITE, MiningHandlers.pickaxe(15, 1), MiningBlockClassification.defaultBlock()),
    DIORITE(Material.DIORITE, MiningHandlers.pickaxe(15, 1), MiningBlockClassification.defaultBlock()),
    STONE(Material.STONE, MiningHandlers.pickaxe(15, 1), MiningBlockClassification.blockBlock()),
    SAND(Material.SAND, MiningHandlers.pickaxe(5, 1), MiningBlockClassification.blockBlock()),
    NETHERRACK(Material.NETHERRACK, MiningHandlers.pickaxe(4, 1), MiningBlockClassification.blockBlock()),
    COBBLESTONE(Material.COBBLESTONE, MiningHandlers.pickaxe(20, 1), MiningBlockClassification.blockBlock()),
    END_STONE(Material.END_STONE, MiningHandlers.pickaxe(30, 1), MiningBlockClassification.blockBlock()),
    OBSIDIAN(Material.END_STONE, MiningHandlers.pickaxe(500, 4), MiningBlockClassification.blockBlock()),

    // Mineable blocks (same speed for all tools and hand)
    ICE(Material.ICE, MiningHandlers.hand(10, 1), MiningBlockClassification.blockBlock()),

    // Ores (pickaxe)
    ORE_IRON(Material.IRON_ORE, MiningHandlers.pickaxe(30, 2), MiningBlockClassification.oreBlock()),
    ORE_COAL(Material.COAL_ORE, MiningHandlers.pickaxe(30, 1), MiningBlockClassification.oreBlock()),
    ORE_GOLD(Material.GOLD_ORE, MiningHandlers.pickaxe(30, 3), MiningBlockClassification.oreBlock()),
    ORE_DIAMOND(Material.DIAMOND_ORE, MiningHandlers.pickaxe(30, 3), MiningBlockClassification.oreBlock()),
    ORE_REDSTONE(Material.REDSTONE_ORE, MiningHandlers.pickaxe(30, 3), MiningBlockClassification.oreBlock()),

    // Break time with axe = blockStrength / axe_strength from AxeComponent
    // Vanilla times: Hand=3s, Wooden=1.5s, Stone=0.75s, Iron=0.5s, Diamond=0.4s, Golden=0.25s
    ACACIA_LOG(Material.ACACIA_LOG, MiningHandlers.axe(60, 60), MiningBlockClassification.foragingBlock()),
    BIRCH_LOG(Material.BIRCH_LOG, MiningHandlers.axe(60, 60), MiningBlockClassification.foragingBlock()),
    JUNGLE_LOG(Material.JUNGLE_LOG, MiningHandlers.axe(60, 60), MiningBlockClassification.foragingBlock()),
    SPRUCE_LOG(Material.SPRUCE_LOG, MiningHandlers.axe(60, 60), MiningBlockClassification.foragingBlock()),
    DARK_OAK_LOG(Material.DARK_OAK_LOG, MiningHandlers.axe(60, 60), MiningBlockClassification.foragingBlock()),
    MANGROVE_LOG(Material.MANGROVE_LOG, MiningHandlers.axe(60, 60), MiningBlockClassification.foragingBlock()),
    OAK_LOG(Material.OAK_LOG, MiningHandlers.axe(60, 60), MiningBlockClassification.foragingBlock()),

    // Farming mineable blocks (instant break)
    WHEAT(Material.WHEAT, MiningHandlers.instant(), MiningBlockClassification.wheatBlock()),
    SUGAR_CANE(Material.SUGAR_CANE, MiningHandlers.instant(), MiningBlockClassification.sugarCaneBlock()),
    CACTUS(Material.CACTUS, MiningHandlers.instant(), MiningBlockClassification.cactusBlock()),
    BROWN_MUSHROOM(Material.BROWN_MUSHROOM, MiningHandlers.instant(), MiningBlockClassification.mushroomBlock()),
    RED_MUSHROOM(Material.RED_MUSHROOM, MiningHandlers.instant(), MiningBlockClassification.mushroomBlock()),
    POTATO(Material.POTATO, MiningHandlers.instant(), MiningBlockClassification.potatoBlock()),
    CARROT(Material.CARROT, MiningHandlers.instant(), MiningBlockClassification.carrotBlock()),
    NETHER_WART(Material.NETHER_WART, MiningHandlers.instant(), MiningBlockClassification.netherWartBlock()),

    // Farming blocks with strength (using instant for now)
    PUMPKIN(Material.PUMPKIN, MiningHandlers.instant(), MiningBlockClassification.pumpkinBlock()),
    CARVED_PUMPKIN(Material.CARVED_PUMPKIN, MiningHandlers.instant(), MiningBlockClassification.pumpkinBlock()),
    MELON(Material.MELON, MiningHandlers.instant(), MiningBlockClassification.melonBlock()),

    // Dwarven mines mineable blocks (pickaxe)
    MITHRIL_WEAK(Material.GRAY_WOOL, MiningHandlers.pickaxe(500, 4), MiningBlockClassification.dwarvenMetalBlock()),
    MITHRIL_WEAK2(Material.CYAN_TERRACOTTA, MiningHandlers.pickaxe(500, 4), MiningBlockClassification.dwarvenMetalBlock()),
    MITHRIL_MEDIUM(Material.PRISMARINE, MiningHandlers.pickaxe(800, 4), MiningBlockClassification.dwarvenMetalBlock()),
    MITHRIL_MEDIUM2(Material.DARK_PRISMARINE, MiningHandlers.pickaxe(800, 4), MiningBlockClassification.dwarvenMetalBlock()),
    MITHRIL_MEDIUM3(Material.PRISMARINE_BRICKS, MiningHandlers.pickaxe(800, 4), MiningBlockClassification.dwarvenMetalBlock()),
    MITHRIL_STRONG(Material.LIGHT_BLUE_WOOL, MiningHandlers.pickaxe(1500, 4), MiningBlockClassification.dwarvenMetalBlock()),
    TITANIUM(Material.POLISHED_DIORITE, MiningHandlers.pickaxe(2500, 5), MiningBlockClassification.dwarvenMetalBlock()),
    DWARVEN_GOLD(Material.GOLD_BLOCK, MiningHandlers.pickaxe(600, 3), MiningBlockClassification.oreBlock()),
    ;

    private final Material material;
    private final SkyBlockMiningHandler miningHandler;
    private final MiningBlockClassification blockType;

    MineableBlock(Material material, SkyBlockMiningHandler miningHandler, MiningBlockClassification blockType) {
        this.material = material;
        this.miningHandler = miningHandler;
        this.blockType = blockType;
    }

    /**
     * Get block strength from the handler (backwards compatibility).
     */
    public double getStrength() {
        return miningHandler.getStrength();
    }

    /**
     * Get mining power requirement from the handler (backwards compatibility).
     */
    public int getMiningPowerRequirement() {
        return miningHandler.getMiningPowerRequirement();
    }

    public static MineableBlock get(Block block) {
        for (MineableBlock mineableBlock : values()) {
            if (block.key().equals(mineableBlock.getMaterial().key()))
                return mineableBlock;
        }
        return null;
    }
}
