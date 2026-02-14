package net.swofty.type.bedwarsgame.events;

import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.commons.bedwars.BedwarsLevelUtil;
import net.swofty.type.generic.data.datapoints.DatapointLeaderboardLong;
import net.swofty.type.generic.data.handlers.BedWarsDataHandler;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;

public class ActionPlayerDataSpawn implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER_DATA, requireDataLoaded = false, isAsync = true)
    public void run(PlayerSpawnEvent event) {
        if (!event.isFirstSpawn()) return;
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        BedWarsDataHandler handler = BedWarsDataHandler.getUser(player.getUuid());
        handler.runOnLoad(player);

        DatapointLeaderboardLong dp = handler.get(BedWarsDataHandler.Data.EXPERIENCE, DatapointLeaderboardLong.class);
        player.setLevel(BedwarsLevelUtil.calculateLevel(dp.getValue()));
        player.setExp((float) BedwarsLevelUtil.calculateExperienceSinceLastLevel(dp.getValue()) / BedwarsLevelUtil.calculateMaxExperienceFromExperience(dp.getValue()));

        // should be handled in BedWarsPlayer
        //List<HypixelPlayer> players = HypixelGenericLoader.getLoadedPlayers();
        //Collection<Player> playerCollection = players.stream().map(p -> (Player) p).toList();
        //PacketSendingUtils.sendGroupedPacket(playerCollection, new PlayerInfoRemovePacket(player.getUuid()));
    }
}