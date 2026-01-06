package net.swofty.type.skyblockgeneric.levels.unlocks;

import lombok.Getter;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.skyblockgeneric.levels.SkyBlockLevelUnlock;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SkyBlockLevelStatisticUnlock extends SkyBlockLevelUnlock {
    private final ItemStatistics statistics;

    public SkyBlockLevelStatisticUnlock(ItemStatistics statistics) {
        this.statistics = statistics;
    }

    @Override
    public UnlockType type() {
        return UnlockType.STATISTIC;
    }

    @Override
    public ItemStack.Builder getItemDisplay(SkyBlockPlayer player, int level) {
        List<String> statisticsDisplay = new ArrayList<>();
        statistics.getStatisticsAdditive().forEach((key, value) -> {
            if (value > 0)
                statisticsDisplay.add("§8 +§a" + value + key.getSuffix() + " " + key.getFullDisplayName());
        });

        if (statisticsDisplay.isEmpty()) {
            statisticsDisplay.add("§8No statistics unlocked");
        }

        List<String> lore = new ArrayList<>();
        for (int i = 1; i < statisticsDisplay.size(); i++) {
            lore.add(statisticsDisplay.get(i));
        }
        lore.add("§8Level " + level);

        return ItemStackCreator.getStack(statisticsDisplay.getFirst(), Material.APPLE, 1, lore);
    }

    @Override
    public List<String> getDisplay(SkyBlockPlayer player, int level) {
        ArrayList<String> lore = new ArrayList<>();
        statistics.getStatisticsAdditive().forEach((key, value) -> {
            if (value > 0)
                lore.add("§8 +§a" + value + key.getSuffix() + " " + key.getFullDisplayName());
        });
        return lore;
    }
}
