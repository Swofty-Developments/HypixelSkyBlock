package net.swofty.packet.packets.client.anticheat;

import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.client.ClientPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerPositionPacket;
import net.swofty.packet.SkyBlockPacketClientListener;
import net.swofty.user.AntiCheatHandler;
import net.swofty.user.SkyBlockPlayer;

import java.util.HashMap;

public class PacketListenerAirJump extends SkyBlockPacketClientListener {
    public static HashMap<Player, Boolean> isDropping = new HashMap<>();
    public static HashMap<Player, Double> yLevel = new HashMap<>();
    public static HashMap<Player, Boolean> hasChangedVelocity = new HashMap<>();
    public static HashMap<Player, String> lastLoggedMessage = new HashMap<>();

    @Override
    public Class<? extends ClientPacket> getPacket() {
        return ClientPlayerPositionPacket.class;
    }

    @Override
    public void run(PlayerPacketEvent event, ClientPacket packet, SkyBlockPlayer player) {
        ClientPlayerPositionPacket positionPacket = (ClientPlayerPositionPacket) packet;
        Double newY = positionPacket.position().y();
        AntiCheatHandler handler = player.getAntiCheatHandler();
        Double distance = handler.getDistanceFromClosestBlockBelow();

        boolean isOnGround = distance == 1.0;

        if (isOnGround || player.getGameMode() == GameMode.CREATIVE) {
            yLevel.remove(player);
            hasChangedVelocity.put(player, false);
            return;
        }

        if (!yLevel.containsKey(player)) {
            yLevel.put(player, newY);
            hasChangedVelocity.put(player, false);
            return;
        }

        boolean yIncreased = newY > yLevel.get(player);
        if (newY.equals(yLevel.get(player))) return;

        if (yIncreased) {
            if (hasChangedVelocity.get(player)) {
                player.teleport(player.getPosition().sub(0, distance - 1, 0));
                event.setCancelled(true);
                return;
            }
            isDropping.put(player, false);
        } else {
            isDropping.put(player, true);
            hasChangedVelocity.put(player, true);
        }

        yLevel.put(player, newY);
    }
}
