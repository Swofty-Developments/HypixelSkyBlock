package net.swofty.type.bedwarsgame.events;

import net.minestom.server.event.player.PlayerPacketOutEvent;
import net.minestom.server.network.packet.server.ServerPacket;
import net.minestom.server.network.packet.server.play.BlockBreakAnimationPacket;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class ActionBlockBreakAnimation implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false, isAsync = true) // should it be async? - ARI
    public void run(PlayerPacketOutEvent event) {
        BedWarsPlayer player = (BedWarsPlayer) event.getPlayer();
        BedWarsGame game = TypeBedWarsGameLoader.getGameById(player.getGameId());
        if (game == null || !game.getReplayManager().isRecording()) {
            return;
        }

        ServerPacket packet = event.getPacket();
        if (packet instanceof BlockBreakAnimationPacket p) {
            game.getReplayManager().recordBlockBreakAnimation(p);
        }
    }

}
