package net.swofty.types.generic.item.items.enchanted;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.MinionFuelItem;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;

public class EnchantedCharcoal implements Enchanted, Sellable, MinionFuelItem {

    @Override
    public double getSellValue() {
        return 320;
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7Increases the speed of",
                "§7your minion by §a20%§7 for 36",
                "§7hours§7!"));
    }

    @Override
    public double getMinionFuelPercentage() {
        return 20;
    }

    @Override
    public long getFuelLastTimeInMS() {
        return 129600000; // 1.5 Days
    }
}