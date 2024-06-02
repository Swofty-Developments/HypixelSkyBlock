package net.swofty.types.generic.item.items.spooky;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.Arrays;

public class BatFirework implements CustomSkyBlockItem, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7Launches a firework that",
                "§7explodes into several Bat",
                "§7Piñatas, each dropping a bunch",
                "§7of candies. Only usable in the",
                "§7§bVillage§7.",
                "",
                "§8Cooldown: §a30s",
                "",
                "§7§8§oBat Piñata drops do not",
                "§8§ocount towards leaderboard",
                "§8§oprogress!"));
    }
}
