package net.swofty.packet.packets.client.anticheat;

import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.client.ClientPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerPositionPacket;
import net.swofty.packet.SkyBlockPacketClientListener;
import net.swofty.user.AntiCheatHandler;
import net.swofty.user.SkyBlockPlayer;

import java.util.EnumSet;
import java.util.HashMap;

public class PacketListenerAirJump extends SkyBlockPacketClientListener {
    public static HashMap<Player, PlayerData> playerData = new HashMap<>();

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

        PlayerData data = getPlayerData(player);

        if (isInFluid(player)) return;

        boolean isOnGround = distance == 1.0;

        if (isOnGround || player.getGameMode() == GameMode.CREATIVE) {
            data.yLevel = null;
            data.hasChangedVelocity = false;
            return;
        }

        if (data.yLevel == null) {
            data.yLevel = newY;
            data.hasChangedVelocity = false;
            return;
        }

        boolean yIncreased = newY > data.yLevel;
        if (newY.equals(data.yLevel)) return;

        if (yIncreased) {
            if (data.hasChangedVelocity) {
                player.teleport(player.getPosition().sub(0, distance - 1, 0));
                event.setCancelled(true);
                return;
            }
            data.isDropping = false;
        } else {
            data.isDropping = true;
            data.hasChangedVelocity = true;
        }

        data.yLevel = newY;
    }

    private boolean isInFluid(SkyBlockPlayer player) {
        Block blockBelow = player.getInstance().getBlock(player.getPosition().sub(0, 1, 0));
        Block block = player.getInstance().getBlock(player.getPosition());

        return block == Block.WATER || block == Block.LAVA || block == Block.CAULDRON ||
                blockBelow == Block.WATER || blockBelow == Block.LAVA || blockBelow == Block.WATER_CAULDRON || blockBelow == Block.LAVA_CAULDRON;
    }

    private PlayerData getPlayerData(SkyBlockPlayer player) {
        return playerData.computeIfAbsent(player, p -> new PlayerData());
    }

    public static class PlayerData {
        public boolean isDropping;
        public Double yLevel;
        public boolean hasChangedVelocity;
    }
}
