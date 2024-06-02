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

public class ArchfiendDice implements CustomSkyBlockItem, SkullHead, TrackedUniqueItem, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "8ff7c410a4a8b4b518b94d21402d2892fcc8fa68c3028417dd4eaa8b7e35c568";
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§6Ability: Try Your Luck §e§lRIGHT CLICK",
                "§7Costs §6666k §7coins to roll between 1-6.",
                "§7Gain between §c-120❤ §7and §c+120❤ §7for §a24h§7.",
                "§8Overwritten by new rolls!",
                "",
                "§7If you roll a §c6§7, earn §615M §7coins but lose this dice."));
    }
}
