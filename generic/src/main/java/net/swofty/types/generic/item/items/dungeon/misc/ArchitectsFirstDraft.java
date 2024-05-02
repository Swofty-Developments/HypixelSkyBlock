package net.swofty.types.generic.item.items.dungeon.misc;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.Arrays;

public class ArchitectsFirstDraft implements CustomSkyBlockItem {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }
    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7Can be used to reset a puzzle room",
                "§7when in dungeons.",
                "",
                "§7A 'reset' button seems to have been",
                "§7hidden somewhere in the floor, just",
                "§7in case something goes wrong...",
                "",
                "§cDungeons only!"));
    }
}
