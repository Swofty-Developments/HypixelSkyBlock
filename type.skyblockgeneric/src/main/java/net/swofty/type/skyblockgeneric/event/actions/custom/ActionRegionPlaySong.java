package net.swofty.type.skyblockgeneric.event.actions.custom;

import net.swofty.commons.Songs;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.event.custom.PlayerRegionChangeEvent;
import net.swofty.type.skyblockgeneric.noteblock.SkyBlockSong;
import net.swofty.type.skyblockgeneric.noteblock.SkyBlockSongsHandler;
import net.swofty.type.skyblockgeneric.region.RegionType;
import net.swofty.type.skyblockgeneric.user.SkyBlockActionBar;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;

public class ActionRegionPlaySong implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.CUSTOM , requireDataLoaded = false , isAsync = true)
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
