package net.swofty.types.generic.item.items.minion.upgrade.upgrade;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.MinionUpgradeItem;
import net.swofty.types.generic.item.impl.recipes.MinionUpgradeSpeedItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.List;

public class MinionExpander implements CustomSkyBlockItem, MinionUpgradeItem, Enchanted, MinionUpgradeSpeedItem {

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return List.of(
                "§7This item can be used as a",
                "§7minion upgrade. Increases the",
                "§7effective radius of the minion",
                "§7by §a1§7 extra block.",
                "",
                "§7Increases the speed of your",
                "§7 minion by §a5%§7."
        );
    }

    @Override
    public int getPercentageSpeedIncrease() {
        return 5;
    }
}
