package net.swofty.packet.packets.client;

import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.network.packet.client.ClientPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerDiggingPacket;
import net.swofty.event.SkyBlockEvent;
import net.swofty.event.custom.PlayerDamageSkyBlockBlockEvent;
import net.swofty.packet.SkyBlockPacketClientListener;
import net.swofty.user.SkyBlockPlayer;

public class PacketListenerPlayerDig extends SkyBlockPacketClientListener {
    @Override
    public Class<? extends ClientPacket> getPacket() {
        return ClientPlayerDiggingPacket.class;
    }

    @Override
    public void run(PlayerPacketEvent event, ClientPacket packet, SkyBlockPlayer player) {
        ClientPlayerDiggingPacket digPacket = (ClientPlayerDiggingPacket) packet;
        SkyBlockEvent.callSkyBlockEvent(new PlayerDamageSkyBlockBlockEvent(
                player,
                digPacket.blockPosition(),
                digPacket.status()));
    }
}
