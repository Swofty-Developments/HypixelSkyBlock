package net.swofty.types.generic.item.items.travelscroll;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.TravelScrollItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.Arrays;

public class HubCastleTravelScroll implements CustomSkyBlockItem, TravelScrollItem, NotFinishedYet {

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7Consume this item to add its destination to your fast travel options.",
                "",
                "§7Requires §bMVP§c+ §7to consume!",
                "",
                "§7Island: §aHub",
                "§7Teleport: §eCastle"));
    }
}
