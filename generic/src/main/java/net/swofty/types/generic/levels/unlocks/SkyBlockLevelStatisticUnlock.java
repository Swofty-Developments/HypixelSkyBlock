package net.swofty.types.generic.levels.unlocks;

import lombok.Getter;
import net.minestom.server.item.ItemStack;
import net.swofty.types.generic.levels.SkyBlockLevelUnlock;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

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
        return null;
    }

    @Override
    public List<String> getDisplay(SkyBlockPlayer player, int level) {
        ArrayList<String> lore = new ArrayList<>();
        statistics.getStatisticsAdditive().forEach((key, value) -> {
            if (value > 0)
                lore.add("§8 +§a" + value + key.getSuffix() + " " + key.getDisplayColor() + key.getSymbol() + " " + key.getDisplayName());
        });
        return lore;
    }
}
