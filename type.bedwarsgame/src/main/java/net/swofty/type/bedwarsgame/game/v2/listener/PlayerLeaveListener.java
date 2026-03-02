package net.swofty.type.bedwarsgame.game.v2.listener;

import net.kyori.adventure.text.Component;
import net.swofty.commons.ServerType;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.game.game.event.PlayerLeaveGameEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

import java.util.Random;

public class PlayerLeaveListener implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onPlayerLeave(PlayerLeaveGameEvent event) {
        BedWarsPlayer player = (BedWarsPlayer) event.player();
        player.sendTo(ServerType.BEDWARS_LOBBY);
        BedWarsGame game = TypeBedWarsGameLoader.getGameById(event.gameId());
        if (game == null) return;
        if (!game.getState().isWaiting()) return;

        game.broadcastMessage(Component.text("§k" + player.getFakeUuid().toString().substring(0, new Random().nextInt(10) + 4) + " §ehas quit!"));
    }
}
