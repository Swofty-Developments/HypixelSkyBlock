package net.swofty.types.generic.item.items.pet.petitems;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.PetItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.Arrays;

public class AllSkillsExpBoost implements CustomSkyBlockItem, PetItem, Enchanted, NotFinishedYet {
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
                "§7Gives §a+10% §7pet exp for all skills.",
                "",
                "§eRight-click on your summoned pet to",
                "§egive it this item!"));
    }
}
