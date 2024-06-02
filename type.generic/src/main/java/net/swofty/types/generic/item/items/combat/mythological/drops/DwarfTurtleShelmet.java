package net.swofty.types.generic.item.items.combat.mythological.drops;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class DwarfTurtleShelmet implements CustomSkyBlockItem, Enchanted, PetItem, SkullHead, NotFinishedYet {
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
                "§7Makes the pet\u0027s owner immune to",
                "§7knockback.",
                "",
                "§eRight-click on your summoned pet to",
                "§egive it this item!"));
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "664698fea5ec3f2fd7db7a8e3f4e989a1716035c2bd3666434ba1af58157";
    }
}
