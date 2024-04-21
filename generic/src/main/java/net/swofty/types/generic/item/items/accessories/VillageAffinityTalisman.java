package net.swofty.types.generic.item.items.accessories;

import net.minestom.server.event.Event;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
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

@EventParameters(description = "Village Affinity Talisman ability",
        node = EventNodes.CUSTOM,
        requireDataLoaded = true)
public class VillageAffinityTalisman extends SkyBlockEvent implements Talisman {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerRegionChangeEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        PlayerRegionChangeEvent event = (PlayerRegionChangeEvent) tempEvent;
        SkyBlockPlayer player = event.getPlayer();

        if (event.getTo() == null || !event.getTo().equals(RegionType.VILLAGE) || player.getRegion() == null
        ) return;
        String region = player.getRegion().getName();

        player.getStatistics().boostStatistic(TemporaryConditionalStatistic.builder()
                .withStatistics((z) -> ItemStatistics.builder().withAdditive(ItemStatistic.SPEED, 10D).build())
                .withExpiry(newPlayer -> {
                    return newPlayer.getRegion() != null && newPlayer.getRegion().getName().equals(region);
                })
                .build());
    }

    @Override
    public List<String> getTalismanDisplay() {
        return List.of("§7Increases your §f✦ Speed §7by",
                "§a+10 §7while held in the",
                "§bVillage§7."
        );
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "9c11d6c79b8a1f18902d783cdda4bdfb9d47337b73791028a126a6e6cf101def";
    }
}
