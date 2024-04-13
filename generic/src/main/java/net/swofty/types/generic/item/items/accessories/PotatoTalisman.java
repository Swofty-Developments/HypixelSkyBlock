package net.swofty.types.generic.item.items.accessories;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PotatoTalisman implements Talisman , NotFinishedYet {
    @Override
    public List<String> getTalismanDisplay() {
        return List.of(
                "§7Your potato minions farm §a5%",
                "§a§7faster while wearing on your",
                "§7island.",
                "",
                "§7§8Forged by the Potato King",
                "§8after winning the Great",
                "§8Potato War."
        );
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "2fc11c51-700f-35d7-8e19-71d8549daef9";
    }
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .with(ItemStatistic.SPEED, 1D)
                .build();
    }
}
