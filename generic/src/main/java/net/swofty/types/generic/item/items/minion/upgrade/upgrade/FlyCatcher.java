package net.swofty.types.generic.item.items.minion.upgrade.upgrade;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.MinionUpgradeItem;
import net.swofty.types.generic.item.impl.recipes.MinionUpgradeSpeedItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.List;

public class FlyCatcher implements CustomSkyBlockItem, MinionUpgradeItem, Enchanted, MinionUpgradeSpeedItem {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return List.of(
                "§7This item can be used as a",
                "§7minion upgrade. Prevents your",
                "§7minions from getting distracted",
                "§7by flies. Increases the speed of",
                "§7your minion by §a20%§7."
        );
    }

    @Override
    public int getPercentageSpeedIncrease() {
        return 20;
    }
}
