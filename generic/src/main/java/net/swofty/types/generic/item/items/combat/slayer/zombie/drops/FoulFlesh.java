package net.swofty.types.generic.item.items.combat.slayer.zombie.drops;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.ExtraUnderNameDisplay;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.Arrays;

public class FoulFlesh implements CustomSkyBlockItem, Sellable, ExtraUnderNameDisplay {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
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
}
