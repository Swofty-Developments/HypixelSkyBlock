package net.swofty.types.generic.event.actions.custom;

import net.minestom.server.event.Event;
import net.swofty.commons.Songs;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.PlayerRegionChangeEvent;
import net.swofty.types.generic.noteblock.SkyBlockSong;
import net.swofty.types.generic.noteblock.SkyBlockSongsHandler;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.StatisticDisplayReplacement;
import net.swofty.types.generic.utility.StringUtility;

import java.util.List;

@EventParameters(description = "Plays songs in their respective regions.",
        node = EventNodes.CUSTOM,
        requireDataLoaded = false,
        isAsync = true)
public class ActionRegionPlaySong extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerRegionChangeEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        PlayerRegionChangeEvent event = (PlayerRegionChangeEvent) tempEvent;
        SkyBlockPlayer player = event.getPlayer();
        SkyBlockSongsHandler songHandler = player.getSongHandler();

        if (event.getTo() != null && event.getFrom() != null
                && !event.getTo().equals(event.getFrom())) {
            RegionType to = event.getTo();
            List<Songs> songs = to.getSongs();

            if (songs == null || songs.isEmpty()) return;

            Songs randomSong = songs.get((int) (Math.random() * songs.size()));
            songHandler.setPlayerSong(new SkyBlockSong(randomSong));

            player.setDisplayReplacement(StatisticDisplayReplacement.builder()
                    .ticksToLast(20)
                    .purpose(StatisticDisplayReplacement.Purpose.MUSIC)
                    .display("§2" + StringUtility.toNormalCase(randomSong.name()))
                    .build(), StatisticDisplayReplacement.DisplayType.MANA);
        }
    }
}
