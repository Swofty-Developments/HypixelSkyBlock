package net.swofty.types.generic.item.items.enchanted;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;

public class EnchantedEgg implements Enchanted, Sellable {
    @Override
    public double getSellValue() {
        return 432;
    }


    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7This item can be used as a",
                "§7minion upgrade for chicken",
                "§7minions. Guarantees that each",
                "§7chicken will drop an egg after",
                "§7they spawn.",
                "§7",
                "§7Can also be used to craft",
                "§7low-rarity pets."));
    }
}