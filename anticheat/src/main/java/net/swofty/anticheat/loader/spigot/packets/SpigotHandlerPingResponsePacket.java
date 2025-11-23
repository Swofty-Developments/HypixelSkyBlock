package net.swofty.anticheat.loader.spigot.packets;

import com.comphenix.protocol.events.PacketContainer;
import net.swofty.anticheat.engine.SwoftyPlayer;
import net.swofty.anticheat.event.packet.PingResponsePacket;
import net.swofty.anticheat.event.packet.SwoftyPacket;
import net.swofty.anticheat.loader.LoaderPacketHandler;

import java.util.UUID;

public class SpigotHandlerPingResponsePacket extends LoaderPacketHandler {

    @Override
    public Class<? extends SwoftyPacket> getHandledPacketClass() {
        return PingResponsePacket.class;
    }

    @Override
    public SwoftyPacket buildSwoftyPacket(UUID uuid, Object loaderPacket) {
        if (!(loaderPacket instanceof PacketContainer packet)) return null;

        int pingId = packet.getIntegers().read(0);
        return new PingResponsePacket(SwoftyPlayer.players.get(uuid), pingId);
    }

    @Override
    public Object buildLoaderPacket(UUID uuid, SwoftyPacket swoftyPacket) {
        return null;
    }
}
