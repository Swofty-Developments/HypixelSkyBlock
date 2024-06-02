package net.swofty.types.generic.item.items.combat.slayer.blaze.drops;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.item.impl.TrackedUniqueItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class HighClassArchfiendDice implements CustomSkyBlockItem, SkullHead, TrackedUniqueItem, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "8931fbc922b432ec7f4712bbf603ea72754f2729a6863925abe35fb746b10770";
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§dStraight from Kickback City!",
                "",
                "§6Ability: Try Your Luck §e§lRIGHT CLICK",
                "§7Costs §66.6M §7coins to roll between 1-6.",
                "§7Gain between §c-300❤ §7and §c+300❤ §7for §a24h§7.",
                "§8Overwritten by new rolls!",
                "",
                "§7If you roll a §c6§7, earn §6100M §7coins but lose this dice."));
    }
}
