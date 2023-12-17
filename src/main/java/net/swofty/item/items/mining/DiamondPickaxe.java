package net.swofty.item.items.mining;

import net.swofty.item.impl.CustomSkyBlockItem;
import net.swofty.item.impl.MiningTool;
import net.swofty.user.statistics.ItemStatistic;
import net.swofty.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.Arrays;

public class DiamondPickaxe implements CustomSkyBlockItem, MiningTool
{
      @Override
      public ItemStatistics getStatistics() {
            return ItemStatistics.builder()
                    .with(ItemStatistic.MINING_SPEED, 150)
                    .build();
      }

      @Override
      public int getBreakingPower() {
            return 4;
      }
}
