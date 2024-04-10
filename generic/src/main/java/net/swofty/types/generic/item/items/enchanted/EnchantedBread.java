package net.swofty.types.generic.item.items.enchanted;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.MinionFuelItem;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;

public class EnchantedBread implements Enchanted, Sellable, MinionFuelItem {

    @Override
    public double getSellValue() {
        return 60;
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7increases the speed of",
                "§7your minion by §a5%§7",
                "§7for 12 hours§7!"));
    }

    @Override
    public double getMinionFuelPercentage() {
        return 5;
    }

    @Override
    public long getFuelLastTimeInMS() {
        return 43200000; // 12 Hours
    }
}