package net.swofty.types.generic.item.items.combat.mythological.drops;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class CrochetTigerPlushie implements CustomSkyBlockItem, Enchanted, PetItem, SkullHead, NotFinishedYet {
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
                "§7§7Grants §e+35⚔ Bonus Attack Speed§7.",
                "",
                "§7§eRight-click on your summoned pet to",
                "§egive it this item!"));
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "3bddf5bae3af592858df9a150109e88c1caed8ba51e793c25aa03ca1b25db";
    }
}
