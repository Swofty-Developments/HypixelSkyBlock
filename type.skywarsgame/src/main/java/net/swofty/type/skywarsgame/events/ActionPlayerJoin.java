package net.swofty.type.skywarsgame.events;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.swofty.commons.ServerType;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.redis.service.RedisGameMessage;
import net.swofty.type.generic.utility.MathUtility;
import net.swofty.type.skywarsgame.TypeSkywarsGameLoader;
import net.swofty.type.skywarsgame.game.SkywarsGame;
import net.swofty.type.skywarsgame.game.SkywarsGameStatus;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;
import org.tinylog.Logger;

public class ActionPlayerJoin implements HypixelEventClass {
    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void onJoin(AsyncPlayerConfigurationEvent event) {
        SkywarsPlayer player = (SkywarsPlayer) event.getPlayer();
        Logger.info("Player " + player.getUsername() + " joined the server from origin server " + player.getOriginServer());

        event.setSpawningInstance(HypixelConst.getEmptyInstance());
        player.setRespawnPoint(new Pos(0, 100, 0));

        MathUtility.delay(() -> tryJoinGame(player, false), 15);
    }

    private void tryJoinGame(SkywarsPlayer player, boolean isRetry) {
        if (!player.isOnline()) return;

        String assignedGameId = RedisGameMessage.game.remove(player.getUuid());
        if (assignedGameId == null) {
            if (!isRetry) {
                Logger.info("No game assignment found for " + player.getUsername() + ", retrying in 1 second...");
                MathUtility.delay(() -> tryJoinGame(player, true), 20);
                return;
            }
            player.sendMessage("§cNo game assignment found! Returning to lobby...");
            player.sendTo(ServerType.SKYWARS_LOBBY);
            return;
        }

        SkywarsGame assignedGame = TypeSkywarsGameLoader.getGameById(assignedGameId);
        if (assignedGame == null) {
            player.sendMessage("§cThe assigned game no longer exists! Returning to lobby...");
            player.sendTo(ServerType.SKYWARS_LOBBY);
            return;
        }

        if (assignedGame.getGameStatus() != SkywarsGameStatus.WAITING) {
            player.sendMessage("§cThe game has already started! Returning to lobby...");
            player.sendTo(ServerType.SKYWARS_LOBBY);
            return;
        }

        if (assignedGame.getPlayers().size() >= assignedGame.getGameType().getMaxPlayers()) {
            player.sendMessage("§cThe game is full! Returning to lobby...");
            player.sendTo(ServerType.SKYWARS_LOBBY);
            return;
        }

        assignedGame.join(player);
    }
}
