package net.swofty.type.hub;

import net.minestom.server.coordinate.Pos;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.entity.hologram.PlayerHolograms;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.calendar.CalendarEvent;
import net.swofty.type.skyblockgeneric.calendar.SkyBlockCalendar;
import net.swofty.type.skyblockgeneric.elections.ElectionManager;
import net.swofty.type.skyblockgeneric.elections.SkyBlockMayor;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElectionDisplay {

    private static final Map<HypixelPlayer, PlayerHolograms.ExternalPlayerHologram> holos = new HashMap<>();

    public static void addAndUpdate() {
        HypixelGenericLoader.getLoadedPlayers().forEach(player -> {
            long timeLeft = SkyBlockCalendar.ticksUntilEvent(CalendarEvent.ELECTION_CLOSE);
            String timeLeftFormatted = StringUtility.formatTimeLeft(timeLeft * 50L);

            Logger.info(timeLeftFormatted + " until election close for player " + timeLeft);

            List<String> message = new ArrayList<>(List.of(
                "§e§lMAYOR ELECTIONS",
                "§bYear " + SkyBlockCalendar.getYear(),
                "§eTime left: §a" + timeLeftFormatted
            ));

            String vote = ElectionManager.getPlayerVote(player.getUuid());
            if (vote != null) {
                SkyBlockMayor mayor = SkyBlockMayor.valueOf(vote);
                String colouredName = mayor.getColor() + mayor.getDisplayName();
                message.add("§eYour vote: §f" + colouredName);
                message.add("§e§lCLICK TO SWITCH");
            }

            if (!holos.containsKey(player)) {
                PlayerHolograms.ExternalPlayerHologram holo = PlayerHolograms.ExternalPlayerHologram.builder()
                    .player(player)
                    .text(message.toArray(new String[0]))
                    .pos(new Pos(0.5, 51.5, 34.5))
                    .spacing(0.3d)
                    .instance(player.getInstance())
                    .build();

                PlayerHolograms.addExternalPlayerHologram(holo);
                holos.put(player, holo);
                return;
            }

            PlayerHolograms.updateExternalPlayerHologramText(holos.get(player), message.toArray(new String[0]));
        });
    }

}
