package net.swofty.item.items.farming;

import net.swofty.item.SkyBlockItem;
import net.swofty.item.impl.CustomSkyBlockItem;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.statistics.ItemStatistics;

import java.util.List;

public class RookieHoe implements CustomSkyBlockItem
{
      @Override
      public ItemStatistics getStatistics() {
            return ItemStatistics.EMPTY;
      }

      @Override
      public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
            return List.of("§7Crops broken with this hoe have", "§7a §a50%§7 chance to drop a seed!");
      }
}
