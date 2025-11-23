package net.swofty.anticheat.loader.spigot.packets;

import com.comphenix.protocol.events.PacketContainer;
import net.swofty.anticheat.engine.SwoftyPlayer;
import net.swofty.anticheat.event.packet.RotationPacket;
import net.swofty.anticheat.event.packet.SwoftyPacket;
import net.swofty.anticheat.loader.LoaderPacketHandler;

import java.util.UUID;

public class SpigotHandlerRotationPacket extends LoaderPacketHandler {

    @Override
    public Class<? extends SwoftyPacket> getHandledPacketClass() {
        return RotationPacket.class;
    }

    @Override
    public SwoftyPacket buildSwoftyPacket(UUID uuid, Object loaderPacket) {
        if (!(loaderPacket instanceof PacketContainer packet)) return null;

        float yaw = packet.getFloat().read(0);
        float pitch = packet.getFloat().read(1);
        boolean onGround = packet.getBooleans().read(0);

        return new RotationPacket(SwoftyPlayer.players.get(uuid), yaw, pitch, onGround);
    }

    @Override
    public Object buildLoaderPacket(UUID uuid, SwoftyPacket swoftyPacket) {
        return null;
    }
}
