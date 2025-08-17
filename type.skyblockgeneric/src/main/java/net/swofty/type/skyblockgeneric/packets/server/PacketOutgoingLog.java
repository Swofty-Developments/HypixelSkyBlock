package net.swofty.type.skyblockgeneric.packets.server;

import net.minestom.server.network.packet.server.ServerPacket;
import net.minestom.server.network.packet.server.configuration.FinishConfigurationPacket;
import net.swofty.type.generic.packet.HypixelPacketServerListener;

public class PacketOutgoingLog extends HypixelPacketServerListener {
    @Override
    public Class<? extends ServerPacket> getPacket() {
        return FinishConfigurationPacket.class;
    }

    @Override
    public void run(ServerPacket packet) {

    }
}
