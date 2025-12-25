package net.swofty.type.skyblockgeneric.region.mining;

import net.swofty.commons.skyblock.statistics.ItemStatistic;

public record MiningBlockClassification(ItemStatistic baseSkillFortune, ItemStatistic specificBlockFortune) {

    //Main-Categories
    public static MiningBlockClassification defaultBlock() {
        return new MiningBlockClassification(null, null);
    }
    public static MiningBlockClassification miningBlock() {
        return new MiningBlockClassification(ItemStatistic.MINING_FORTUNE, null);
    }
    public static MiningBlockClassification farmingBlock() {
        return new MiningBlockClassification(ItemStatistic.FARMING_FORTUNE, null);
    }
    public static MiningBlockClassification foragingBlock() {
        return new MiningBlockClassification(ItemStatistic.FORAGING_FORTUNE, null);
    }

    //Mining Sub-Categories
    public static MiningBlockClassification oreBlock() {
        return new MiningBlockClassification(ItemStatistic.MINING_FORTUNE, ItemStatistic.ORE_FORTUNE);
    }
    public static MiningBlockClassification blockBlock() {
        return new MiningBlockClassification(ItemStatistic.MINING_FORTUNE, ItemStatistic.BLOCK_FORTUNE);
    }
    public static MiningBlockClassification dwarvenMetalBlock() {
        return new MiningBlockClassification(ItemStatistic.MINING_FORTUNE, ItemStatistic.DWARVEN_METAL_FORTUNE);
    }
    public static MiningBlockClassification gemstoneBlock() {
        return new MiningBlockClassification(ItemStatistic.MINING_FORTUNE, ItemStatistic.GEMSTONE_FORTUNE);
    }

    //Farming Sub-Categories
    public static MiningBlockClassification wheatBlock() {
        return new MiningBlockClassification(ItemStatistic.FARMING_FORTUNE, ItemStatistic.WHEAT_FORTUNE);
    }
    public static MiningBlockClassification potatoBlock() {
        return new MiningBlockClassification(ItemStatistic.FARMING_FORTUNE, ItemStatistic.POTATO_FORTUNE);
    }
    public static MiningBlockClassification carrotBlock() {
        return new MiningBlockClassification(ItemStatistic.FARMING_FORTUNE, ItemStatistic.CARROT_FORTUNE);
    }
    public static MiningBlockClassification pumpkinBlock() {
        return new MiningBlockClassification(ItemStatistic.FARMING_FORTUNE, ItemStatistic.PUMPKIN_FORTUNE);
    }
    public static MiningBlockClassification melonBlock() {
        return new MiningBlockClassification(ItemStatistic.FARMING_FORTUNE, ItemStatistic.MELON_FORTUNE);
    }
    public static MiningBlockClassification cactusBlock() {
        return new MiningBlockClassification(ItemStatistic.FARMING_FORTUNE, ItemStatistic.CACTUS_FORTUNE);
    }
    public static MiningBlockClassification netherWartBlock() {
        return new MiningBlockClassification(ItemStatistic.FARMING_FORTUNE, ItemStatistic.NETHER_WART_FORTUNE);
    }
    public static MiningBlockClassification cocoaBeansBlock() {
        return new MiningBlockClassification(ItemStatistic.FARMING_FORTUNE, ItemStatistic.COCOA_BEANS_FORTUNE);
    }
    public static MiningBlockClassification mushroomBlock() {
        return new MiningBlockClassification(ItemStatistic.FARMING_FORTUNE, ItemStatistic.MUSHROOM_FORTUNE);
    }
    public static MiningBlockClassification sugarCaneBlock() {
        return new MiningBlockClassification(ItemStatistic.FARMING_FORTUNE, ItemStatistic.SUGAR_CANE_FORTUNE);
    }

}
