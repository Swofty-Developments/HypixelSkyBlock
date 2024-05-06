package net.swofty.types.generic.item.items.enchanted;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;

public class EnchantedBoneMeal implements Enchanted, Sellable, NotFinishedYet {
    @Override
    public double getSellValue() {
        return 1;
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7Instantly grows crops and saplings."));
    }
}
