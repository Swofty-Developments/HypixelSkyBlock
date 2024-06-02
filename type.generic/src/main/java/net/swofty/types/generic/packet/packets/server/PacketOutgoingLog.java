package net.swofty.types.generic.packet.packets.server;

import net.minestom.server.network.packet.server.ServerPacket;
import net.minestom.server.network.packet.server.configuration.FinishConfigurationPacket;
import net.swofty.types.generic.packet.SkyBlockPacketServerListener;

public class PacketOutgoingLog extends SkyBlockPacketServerListener {
    @Override
    public Class<? extends ServerPacket> getPacket() {
        return FinishConfigurationPacket.class;
    }

    @Override
    public void run(ServerPacket packet) {

    }
}
