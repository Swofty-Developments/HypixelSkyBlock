package net.swofty.types.generic.item.items.combat.mythological.drops;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.HelmetImpl;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.Arrays;

public class CrownOfGreed implements CustomSkyBlockItem, HelmetImpl, Sellable, Enchanted {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.builder()
                .with(ItemStatistic.HEALTH, 130D)
                .with(ItemStatistic.DEFENSE, 90D)
                .with(ItemStatistic.MAGIC_FIND, 4D)
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
}
