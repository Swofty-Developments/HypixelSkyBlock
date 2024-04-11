package net.swofty.types.generic.item.items.combat.mythological.drops;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class WashedUpSouvenir implements CustomSkyBlockItem, Enchanted, PetItem, SkullHead, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7§7Pet items can boost pets in",
                "§7various ways but pets can only",
                "§7hold 1 item at a time so choose",
                "§7wisely!",
                "",
                "§7§7Grants §3+5α Sea Creature",
                "§3Chance§7.",
                "",
                "§7§eRight click on your summoned",
                "§epet to give it this item!"));
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "3777f04644dec5f80bfeaa7401acfbbc150eb25d3ff8be4220e7c34426cd727c";
    }
}
