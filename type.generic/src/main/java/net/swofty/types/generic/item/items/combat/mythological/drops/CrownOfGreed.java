package net.swofty.types.generic.item.items.combat.mythological.drops;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.Arrays;

public class CrownOfGreed implements CustomSkyBlockItem, StandardItem, Sellable, Enchanted, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.HEALTH, 130D)
                .withBase(ItemStatistic.DEFENSE, 90D)
                .withBase(ItemStatistic.MAGIC_FIND, 4D)
                .build();
    }

    @Override
    public double getSellValue() {
        return 1000000;
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7Hits have §c+25% §7base damage,",
                "§7but cost §6100x §7the weapon's",
                "§7damage in §6coins §7from your",
                "§7purse."));
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.HELMET;
    }
}
