package net.swofty.type.hub;

import net.minestom.server.coordinate.Pos;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.entity.hologram.PlayerHolograms;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.calendar.CalendarEvent;
import net.swofty.type.skyblockgeneric.calendar.SkyBlockCalendar;
import net.swofty.type.skyblockgeneric.elections.ElectionData;
import net.swofty.type.skyblockgeneric.elections.ElectionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ElectionDisplay {

    private static final Map<HypixelPlayer, PlayerHolograms.ExternalPlayerHologram> holos = new ConcurrentHashMap<>();

    public static void addAndUpdate() {
        Set<UUID> loadedUuids = HypixelGenericLoader.getLoadedPlayers().stream()
                .map(HypixelPlayer::getUuid)
                .collect(Collectors.toSet());
        holos.keySet().removeIf(p -> !loadedUuids.contains(p.getUuid()));

        HypixelGenericLoader.getLoadedPlayers().forEach(player -> {
            Locale l = player.getLocale();
            long timeLeft = SkyBlockCalendar.ticksUntilEvent(CalendarEvent.ELECTION_CLOSE);
            String timeLeftFormatted = StringUtility.formatTimeLeft(timeLeft * 50L);

            List<String> message = new ArrayList<>(List.of(
                I18n.string("gui_election.display.title", l),
                I18n.string("gui_election.display.year", l, Map.of("year", String.valueOf(SkyBlockCalendar.getYear()))),
                I18n.string("gui_election.display.time_left", l, Map.of("time", timeLeftFormatted))
            ));

            String vote = ElectionManager.getPlayerVote(player.getUuid());
            if (vote != null) {
                ElectionData data = ElectionManager.getElectionData();
                ElectionData.CandidateData candidateData = data.getCandidates().stream()
                        .filter(c -> c.getMayorName().equals(vote))
                        .findFirst().orElse(null);
                if (candidateData != null) {
                    message.add(I18n.string("gui_election.display.your_vote", l,
                            Map.of("candidate", candidateData.getColoredName())));
                    message.add(I18n.string("gui_election.display.click_switch", l));
                }
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
