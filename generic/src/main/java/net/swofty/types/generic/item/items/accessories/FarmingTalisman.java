package net.swofty.types.generic.item.items.accessories;

import net.minestom.server.event.Event;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.PlayerRegionChangeEvent;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.region.SkyBlockRegion;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.user.statistics.TemporaryConditionalStatistic;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@EventParameters(description = "Farming talisman ability",
        node = EventNodes.CUSTOM,
        requireDataLoaded = true)
public class FarmingTalisman extends SkyBlockEvent implements Talisman {
    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "9af328c87b068509aca9834eface197705fe5d4f0871731b7b21cd99b9fddc";
    }

    @Override
    public List<String> getTalismanDisplay() {
        return List.of("§7Increases your §f✦ Speed §7by",
                "§a+10 §7while held in the",
                "§bFarm§7, §bThe Barn§7,",
                "§eMushroom Dessert§7, and",
                "§bGarden§7."
        );
    }

    @Override
    public Class<? extends Event> getEvent() {
        return PlayerRegionChangeEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        PlayerRegionChangeEvent event = (PlayerRegionChangeEvent) tempEvent;
        SkyBlockPlayer player = event.getPlayer();

        if (event.getTo() == null || !event.getTo().equals(RegionType.FARM) &&
                !event.getTo().equals(RegionType.THE_BARN) &&
                !event.getTo().equals(RegionType.MUSHROOM_DESERT) ||
                player.getRegion() == null
        ) return;
        String region = player.getRegion().getName();

        player.getStatistics().boostStatistic(TemporaryConditionalStatistic.builder()
                .withStatistics((z) -> ItemStatistics.builder().withAdditive(ItemStatistic.SPEED, 10D).build())
                .withExpiry(newPlayer -> {
                    return newPlayer.getRegion() != null && newPlayer.getRegion().getName().equals(region);
                })
                .build());
    }
}
