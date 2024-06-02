package net.swofty.types.generic.item.items.enchanted;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;

public class OmegaEnchantedEgg implements Enchanted, Sellable {
    @Override
    public double getSellValue() {
        return 432;
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7§7Used to craft the strangest of",
                "§7pets."));
    }
}