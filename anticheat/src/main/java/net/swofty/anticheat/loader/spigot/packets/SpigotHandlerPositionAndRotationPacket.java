package net.swofty.anticheat.loader.spigot.packets;

import com.comphenix.protocol.events.PacketContainer;
import net.swofty.anticheat.engine.SwoftyPlayer;
import net.swofty.anticheat.event.packet.PositionAndRotationPacket;
import net.swofty.anticheat.event.packet.SwoftyPacket;
import net.swofty.anticheat.loader.LoaderPacketHandler;
import net.swofty.anticheat.math.Pos;

import java.util.UUID;

public class SpigotHandlerPositionAndRotationPacket extends LoaderPacketHandler {

    @Override
    public Class<? extends SwoftyPacket> getHandledPacketClass() {
        return PositionAndRotationPacket.class;
    }

    @Override
    public SwoftyPacket buildSwoftyPacket(UUID uuid, Object loaderPacket) {
        if (!(loaderPacket instanceof PacketContainer packet)) return null;

        double x = packet.getDoubles().read(0);
        double y = packet.getDoubles().read(1);
        double z = packet.getDoubles().read(2);
        float yaw = packet.getFloat().read(0);
        float pitch = packet.getFloat().read(1);
        boolean onGround = packet.getBooleans().read(0);

        Pos position = new Pos(x, y, z, yaw, pitch);
        return new PositionAndRotationPacket(SwoftyPlayer.players.get(uuid), position, onGround);
    }

    @Override
    public Object buildLoaderPacket(UUID uuid, SwoftyPacket swoftyPacket) {
        // Not needed for client packets
        return null;
    }
}
