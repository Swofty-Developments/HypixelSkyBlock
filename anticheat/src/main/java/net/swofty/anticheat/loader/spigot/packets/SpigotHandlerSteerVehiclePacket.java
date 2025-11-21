package net.swofty.anticheat.loader.spigot.packets;

import com.comphenix.protocol.events.PacketContainer;
import net.swofty.anticheat.event.packet.SteerVehiclePacket;
import net.swofty.anticheat.event.packet.SwoftyPacket;
import net.swofty.anticheat.loader.LoaderPacketHandler;

import java.util.UUID;

public class SpigotHandlerSteerVehiclePacket extends LoaderPacketHandler {

    @Override
    public Class<? extends SwoftyPacket> getHandledPacketClass() {
        return SteerVehiclePacket.class;
    }

    @Override
    public SwoftyPacket buildSwoftyPacket(UUID uuid, Object loaderPacket) {
        if (!(loaderPacket instanceof PacketContainer packet)) return null;

        float sideways = packet.getFloat().read(0);
        float forward = packet.getFloat().read(1);

        byte flags = packet.getBytes().read(0);
        boolean jump = (flags & 0x01) != 0;
        boolean unmount = (flags & 0x02) != 0;

        return new SteerVehiclePacket(uuid, sideways, forward, jump, unmount);
    }

    @Override
    public Object buildLoaderPacket(UUID uuid, SwoftyPacket swoftyPacket) {
        return null;
    }
}
