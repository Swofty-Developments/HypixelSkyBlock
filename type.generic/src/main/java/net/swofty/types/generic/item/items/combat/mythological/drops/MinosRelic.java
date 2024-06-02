package net.swofty.types.generic.item.items.combat.mythological.drops;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class MinosRelic implements CustomSkyBlockItem, Enchanted, PetItem, SkullHead, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7§7Pet items can boost pets in various",
                "§7ways but pets can only hold 1 item at",
                "§7a time so choose wisely!",
                "",
                "§7§7Increases all pet stats by §a33.3%§7.",
                "",
                "§7§eRight-click on your summoned pet to",
                "§egive it this item!"));
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "40b4648cbd817c7b5fc654c9c054e360d81bbfe1a00f214657a174e3e0d07d21";
    }
}
