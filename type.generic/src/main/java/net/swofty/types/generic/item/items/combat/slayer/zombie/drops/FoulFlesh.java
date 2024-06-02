package net.swofty.types.generic.item.items.combat.slayer.zombie.drops;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.ExtraUnderNameDisplay;
import net.swofty.types.generic.item.impl.MinionFuelItem;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.Arrays;

public class FoulFlesh implements CustomSkyBlockItem, Sellable, ExtraUnderNameDisplay, MinionFuelItem {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public double getSellValue() {
        return 25000;
    }

    @Override
    public String getExtraUnderNameDisplay() {
        return "Brewing Ingredient";
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7Increases the speed of",
                "§7your minion by §a90%§7 for 5",
                "§7hours§7!"));
    }

    @Override
    public double getMinionFuelPercentage() {
        return 90;
    }

    @Override
    public long getFuelLastTimeInMS() {
        return 18000000;
    }
}
