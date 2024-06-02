package net.swofty.types.generic.packet.packets.client;

import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.network.packet.client.ClientPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerDiggingPacket;
import net.swofty.types.generic.event.SkyBlockEventHandler;
import net.swofty.types.generic.event.custom.PlayerDamageSkyBlockBlockEvent;
import net.swofty.types.generic.packet.SkyBlockPacketClientListener;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class PacketListenerPlayerDig extends SkyBlockPacketClientListener {
    @Override
    public Class<? extends ClientPacket> getPacket() {
        return ClientPlayerDiggingPacket.class;
    }

    @Override
    public void run(PlayerPacketEvent event, ClientPacket packet, SkyBlockPlayer player) {
        ClientPlayerDiggingPacket digPacket = (ClientPlayerDiggingPacket) packet;
        SkyBlockEventHandler.callSkyBlockEvent(new PlayerDamageSkyBlockBlockEvent(
                player,
                digPacket.blockPosition(),
                digPacket.status()));
    }

    @Override
    public boolean overrideMinestomProcessing() {
        return false;
    }
}
