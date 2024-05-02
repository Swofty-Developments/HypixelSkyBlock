package net.swofty.types.generic.item.items.accessories;

import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.custom.PlayerRegionChangeEvent;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.user.statistics.TemporaryConditionalStatistic;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MineAffinityTalisman implements Talisman, SkyBlockEventClass {
    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "d9563fdc4acab6db324b21bc43b238fe465e530a6327e7eef11d2d0c4ea";
    }

    @Override
    public List<String> getTalismanDisplay() {
        return List.of("§7Increases your §f✦ Speed §7by",
                "§a+10 §7while held in the",
                "§bMine§7, §6Gold Mine§7, §bDeep",
                "§bCaverns§7, §2Dwarven Mines§7, and",
                "§5Crystal Hollows§7."
        );
    }


    @SkyBlockEvent(node = EventNodes.CUSTOM , requireDataLoaded = true)
    public void run(PlayerRegionChangeEvent event) {
        SkyBlockPlayer player = event.getPlayer();

        if (event.getTo() == null || !event.getTo().equals(RegionType.COAL_MINE) &&
                !event.getTo().equals(RegionType.GOLD_MINE) &&
                !event.getTo().equals(RegionType.DEEP_CAVERN) &&
                !event.getTo().equals(RegionType.DWARVEN_MINES) ||
                player.getRegion() == null
        ) return;
        String region = player.getRegion().getName();

        player.getStatistics().boostStatistic(TemporaryConditionalStatistic.builder()
                .withStatistics((z) -> ItemStatistics.builder().withBase(ItemStatistic.SPEED, 10D).build())
                .withExpiry(newPlayer -> {
                    return newPlayer.getRegion() != null && newPlayer.getRegion().getName().equals(region);
                })
                .build());
    }
}
