package net.swofty.item.items.mining;

import net.swofty.item.impl.CustomSkyBlockItem;
import net.swofty.item.impl.MiningTool;
import net.swofty.user.statistics.ItemStatistic;
import net.swofty.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.Arrays;

public class PioneersPickaxe implements CustomSkyBlockItem, MiningTool
{
      @Override
      public ItemStatistics getStatistics() {
            return ItemStatistics.builder().with(ItemStatistic.MINING_SPEED, 1).build();
      }

      @Override
      public ArrayList<String> getLore() {
            return new ArrayList<>(Arrays.asList(
                    "§7§oThe very first pickaxe",
                    "§7§omodel! Invented by the famous",
                    "§7§oThomas Pickson."
            ));
      }

      @Override
      public int getBreakingPower() {
            return 1;
      }
}
