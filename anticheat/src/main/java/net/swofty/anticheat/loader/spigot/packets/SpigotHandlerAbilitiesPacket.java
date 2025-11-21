package net.swofty.anticheat.loader.spigot.packets;

import com.comphenix.protocol.events.PacketContainer;
import net.swofty.anticheat.event.packet.AbilitiesPacket;
import net.swofty.anticheat.event.packet.SwoftyPacket;
import net.swofty.anticheat.loader.LoaderPacketHandler;

import java.util.UUID;

public class SpigotHandlerAbilitiesPacket extends LoaderPacketHandler {

    @Override
    public Class<? extends SwoftyPacket> getHandledPacketClass() {
        return AbilitiesPacket.class;
    }

    @Override
    public SwoftyPacket buildSwoftyPacket(UUID uuid, Object loaderPacket) {
        if (!(loaderPacket instanceof PacketContainer packet)) return null;

        byte flags = packet.getBytes().read(0);
        boolean invulnerable = (flags & 0x01) != 0;
        boolean flying = (flags & 0x02) != 0;
        boolean allowFlight = (flags & 0x04) != 0;
        boolean creativeMode = (flags & 0x08) != 0;

        float flySpeed = packet.getFloat().size() > 0 ? packet.getFloat().read(0) : 0.05f;
        float walkSpeed = packet.getFloat().size() > 1 ? packet.getFloat().read(1) : 0.1f;

        return new AbilitiesPacket(uuid, invulnerable, flying, allowFlight, creativeMode, flySpeed, walkSpeed);
    }

    @Override
    public Object buildLoaderPacket(UUID uuid, SwoftyPacket swoftyPacket) {
        return null;
    }
}
