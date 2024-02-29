package net.swofty.types.generic.packet.packets.client.anticheat;

import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.client.ClientPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerPositionPacket;
import net.swofty.types.generic.packet.SkyBlockPacketClientListener;
import net.swofty.types.generic.user.AntiCheatHandler;
import net.swofty.types.generic.user.SkyBlockPlayer;

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
        double newY = positionPacket.position().y();
        AntiCheatHandler handler = player.getAntiCheatHandler();
        Double distance = handler.getDistanceFromClosestBlockBelow();

        PlayerData data = getPlayerData(player);

        if (isInFluid(player)) return;

        boolean isOnGround = (distance <= 1.0);

        if (isOnGround || player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
            data.startY = null;
            data.highestY = null;
            return;
        }

        if (data.startY == null) {
            data.startY = newY;
            data.highestY = newY;
            return;
        }

        data.highestY = Math.max(newY, data.highestY);

        if (data.highestY - data.startY > 0.5) {
            player.teleport(player.getPosition().sub(0, 1, 0));
        }
    }

    @Override
    public boolean overrideMinestomProcessing() {
        return false;
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
        public Double startY;
        public Double highestY;
    }
}
