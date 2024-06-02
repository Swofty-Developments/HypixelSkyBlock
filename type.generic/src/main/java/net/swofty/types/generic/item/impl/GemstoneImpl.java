package net.swofty.types.generic.item.impl;

import net.swofty.types.generic.gems.GemRarity;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;

public interface GemstoneImpl extends CustomSkyBlockItem, SkullHead {
    default ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    GemRarity getAssociatedGemRarity();
    Gemstone getAssociatedGemstone();

    @Override
    default ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        Gemstone.Slots slot = Gemstone.Slots.getFromGemstone(getAssociatedGemstone());
        ArrayList<String> toReturn = new ArrayList<>();

        for (String s : getAssociatedGemRarity().getDescription()) {
            toReturn.add(s.replace("{GEM}", slot.getColor() + getName()));
        }

        toReturn.add(" ");
        toReturn.add("§7Some say that when §eharnessed");
        toReturn.add("§eproperly§7, it can give its");
        toReturn.add("§7owner extra " + slot.getColor() + slot.getSymbol() + " " +
                (getAssociatedGemstone().getCorrespondingStatistic() == null ? "" :
                        getAssociatedGemstone().getCorrespondingStatistic().getDisplayName()));

        return toReturn;
    }

    default String getName() {
        return this.getClass().getSimpleName().split("(?=\\p{Upper})")[1];
    }
}
