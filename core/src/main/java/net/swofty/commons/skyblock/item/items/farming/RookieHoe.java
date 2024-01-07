package net.swofty.commons.skyblock.item.items.farming;

import net.swofty.commons.skyblock.item.SkyBlockItem;
import net.swofty.commons.skyblock.item.impl.CustomSkyBlockItem;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.user.statistics.ItemStatistics;

import java.util.List;

public class RookieHoe implements CustomSkyBlockItem {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }

    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return List.of("§7Crops broken with this hoe have", "§7a §a50%§7 chance to drop a seed!");
    }
}
