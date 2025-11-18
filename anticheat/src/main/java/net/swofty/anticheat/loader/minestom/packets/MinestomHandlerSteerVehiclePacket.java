package net.swofty.anticheat.loader.minestom.packets;

import net.minestom.server.network.packet.client.play.ClientSteerVehiclePacket;
import net.swofty.anticheat.event.packet.SteerVehiclePacket;
import net.swofty.anticheat.event.packet.SwoftyPacket;
import net.swofty.anticheat.loader.LoaderPacketHandler;

import java.util.UUID;

public class MinestomHandlerSteerVehiclePacket
        extends LoaderPacketHandler<ClientSteerVehiclePacket> {

    @Override
    public SwoftyPacket buildSwoftyPacket(UUID uuid, ClientSteerVehiclePacket packet) {
        byte flags = packet.flags();
        boolean jump = (flags & 0x01) != 0;
        boolean unmount = (flags & 0x02) != 0;

        return new SteerVehiclePacket(uuid, packet.sideways(), packet.forward(), jump, unmount);
    }

    @Override
    public ClientSteerVehiclePacket buildLoaderPacket(UUID uuid, SwoftyPacket packet) {
        return null;
    }

    @Override
    public Class<ClientSteerVehiclePacket> getHandledPacketClass() {
        return ClientSteerVehiclePacket.class;
    }
}
