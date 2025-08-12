package net.swofty.type.skyblockgeneric.packet.packets.client;

import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.network.packet.client.ClientPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerDiggingPacket;
import net.swofty.type.generic.event.HypixelEventHandler;
import net.swofty.type.skyblockgeneric.event.custom.PlayerDamageSkyBlockBlockEvent;
import net.swofty.type.skyblockgeneric.packet.SkyBlockPacketClientListener;
import SkyBlockPlayer;

public class PacketListenerPlayerDig extends SkyBlockPacketClientListener {
    @Override
    public Class<? extends ClientPacket> getPacket() {
        return ClientPlayerDiggingPacket.class;
    }

    @Override
    public void run(PlayerPacketEvent event, ClientPacket packet, SkyBlockPlayer player) {
        ClientPlayerDiggingPacket digPacket = (ClientPlayerDiggingPacket) packet;
        HypixelEventHandler.callCustomEvent(new PlayerDamageSkyBlockBlockEvent(
                player,
                digPacket.blockPosition(),
                digPacket.status()));
    }

    @Override
    public boolean overrideMinestomProcessing() {
        return false;
    }
}
