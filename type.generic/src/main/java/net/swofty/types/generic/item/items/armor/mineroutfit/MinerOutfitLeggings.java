package net.swofty.types.generic.item.items.armor.mineroutfit;

import net.minestom.server.color.Color;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class MinerOutfitLeggings implements CustomSkyBlockItem, CustomDisplayName, StandardItem,
                                            LeatherColour, Sellable, ArmorItem {

    @Override
    public String getDisplayName(SkyBlockItem item) {
        return "Miner's Outfit Leggings";
    }

    @Override
    public Color getLeatherColour() {
        return new Color(122, 121, 100);
    }

    @Override
    public double getSellValue() {
        return 560;
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.LEGGINGS;
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.DEFENSE, 30D)
                .build();
    }
}
