package net.swofty.types.generic.event.actions.custom;

import net.swofty.commons.Songs;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.custom.PlayerRegionChangeEvent;
import net.swofty.types.generic.noteblock.SkyBlockSong;
import net.swofty.types.generic.noteblock.SkyBlockSongsHandler;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.user.SkyBlockActionBar;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.commons.StringUtility;

import java.util.List;

public class ActionRegionPlaySong implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.CUSTOM , requireDataLoaded = false , isAsync = true)
    public void run(PlayerRegionChangeEvent event) {
        SkyBlockPlayer player = event.getPlayer();
        SkyBlockSongsHandler songHandler = player.getSongHandler();

        if (event.getTo() != null && event.getFrom() != null
                && !event.getTo().equals(event.getFrom())) {
            RegionType to = event.getTo();
            List<Songs> songs = to.getSongs();

            if (songs == null || songs.isEmpty()) return;

            Songs randomSong = songs.get((int) (Math.random() * songs.size()));
            songHandler.setPlayerSong(new SkyBlockSong(randomSong));

            SkyBlockActionBar.getFor(player).addReplacement(
                    SkyBlockActionBar.BarSection.MANA,
                    new SkyBlockActionBar.DisplayReplacement(
                            "§2" + StringUtility.toNormalCase(randomSong.name()),
                            20,
                            2
                    )
            );
        }
    }
}
