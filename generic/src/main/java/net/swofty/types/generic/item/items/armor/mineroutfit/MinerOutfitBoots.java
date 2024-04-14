package net.swofty.types.generic.item.items.armor.mineroutfit;

import net.minestom.server.color.Color;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class MinerOutfitBoots implements CustomSkyBlockItem, CustomDisplayName, StandardItem, LeatherColour, Sellable {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public Color getLeatherColour() {
        return new Color(128, 128, 128);
    }

    @Override
    public double getSellValue() {
        return 19200;
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.BOOTS;
    }

    @Override
    public String getDisplayName() {
        return "Miner's Outfit Boots";
    }
}
