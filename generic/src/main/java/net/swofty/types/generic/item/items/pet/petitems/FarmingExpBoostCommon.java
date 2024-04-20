package net.swofty.types.generic.item.items.pet.petitems;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.Arrays;

public class FarmingExpBoostCommon implements CustomSkyBlockItem, Enchanted, PetItem, CustomDisplayName, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7Pet items can boost pets in various",
                "§7ways but pets can only hold 1 item at",
                "§7a time so choose wisely!",
                "",
                "§7Gives §a+20% §7pet exp for Farming.",
                "",
                "§eRight-click on your summoned pet to",
                "§egive it this item!"));
    }

    @Override
    public String getDisplayName(SkyBlockItem item) {
        return "Farming Exp Boost";
    }
}
